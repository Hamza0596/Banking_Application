package com.banking.bankingapplication.service.serviceimpl;

import com.banking.bankingapplication.constant.FileConstant;
import com.banking.bankingapplication.domain.UserPrincipal;
import com.banking.bankingapplication.dtos.PasswordChangeRequestDto;
import com.banking.bankingapplication.dtos.UserDto;
import com.banking.bankingapplication.entities.Users;
import com.banking.bankingapplication.enums.Role;
import com.banking.bankingapplication.exceptions.*;
import com.banking.bankingapplication.mappers.BankingMapper;
import com.banking.bankingapplication.repositories.UserRepository;
import com.banking.bankingapplication.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.banking.bankingapplication.constant.FileConstant.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;


import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Date;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.MediaType.*;


@Service
@Transactional
public class UserServiceImpl implements UserService , UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BankingMapper bankingMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    LoginAttemptService loginAttemptService;
    @Autowired
    MailingService mailingService;


    @Override
    public UserDto register(UserDto userDto) throws EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail( EMPTY, userDto.getUserName(),userDto.getEmail());
        String password = generatePassword();
        String encodedPassword= bCryptPasswordEncoder.encode(password);
        UserDto newUserDto= new UserDto();
        newUserDto.setUserName(userDto.getUserName());
        newUserDto.setFirstName(userDto.getFirstName());
        newUserDto.setLastName(userDto.getLastName());
        newUserDto.setEmail(userDto.getEmail());
        newUserDto.setPassword(encodedPassword);
        newUserDto.setJob(userDto.getJob());
        newUserDto.setUserName(userDto.getUserName());
        newUserDto.setCreationDate(new Date());
        newUserDto.setActive(true);
        newUserDto.setNotLocked(true);
        newUserDto.setRoles(Role.ROLE_USER.name());
        newUserDto.setAuthorities(Role.ROLE_USER.getAuthorities());
        mailingService.sendEmail(userDto.getEmail(),password,"password");
        return BankingMapper.fromCustomer(userRepository.save(BankingMapper.fromCustomerDto(newUserDto)));
    }

    private String generatePassword() {
       return RandomStringUtils.randomAlphabetic(10);
     }

    private UserDto validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        UserDto userByNewUsername = findUserByUserName (newUsername);
        UserDto userByNewEmail = findUserByEmail(newEmail);
        if(StringUtils.isNotBlank(currentUsername)) {
            UserDto currentUser = findUserByUserName(currentUsername);
            if(currentUser == null) {
                throw new UserNotFoundException("NO_USER_FOUND_BY_USERNAME" + currentUsername);
            }
            if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistException("USERNAME_ALREADY_EXISTS");
            }
            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException("EMAIL_ALREADY_EXISTS");
            }
            return currentUser;
        } else {
            if(userByNewUsername != null) {
                throw new UsernameExistException("USERNAME_ALREADY_EXISTS");
            }
            if(userByNewEmail != null) {
                throw new EmailExistException("EMAIL_ALREADY_EXISTS");
            }
            return null;
        }
    }


    @Override
    public Page<UserDto> getUsers(int pageNumber, int size) {
        return bankingMapper.fromCustomerListToCustomerPageDto(userRepository.findAll(PageRequest.of(pageNumber,size)));
    }

    @Override
    public UserDto user(Long id)   {
        return bankingMapper.fromCustomer(userRepository.findById(id).orElseThrow(()->new UserNotFoundException("No user was found with this id")));
    }

    @Override
    public UserDto findUserByUserName(String userName) {
        return BankingMapper.fromCustomer(userRepository.findByUserName(userName));
    }

    @Override
    public UserDto findUserByEmail(String email) {
        return BankingMapper.fromCustomer(userRepository.findByEmail(email));
    }

    @Override
    public Page<UserDto> filter(String query, int pageNumber, int size) {
        return bankingMapper.fromCustomerListToCustomerPageDto( userRepository.findByQuery(query, PageRequest.of(pageNumber,size)));
    }



    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUserName(username);
        if (user == null) {
            return null;
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            return userPrincipal;
        }
    }


    private void validateLoginAttempt(Users user) {
        if(user.isNotLocked()) {
            if(loginAttemptService.hasExceededMaxAttempts(user.getUserName())) {
                user.setNotLocked(false);
            } else {
                user.setNotLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUserName());
        }


    }
    @Override
    public UserDto addNewUser(String firstName, String lastName, String username, String email, String job, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        validateNewUsernameAndEmail(EMPTY,username,email);
        UserDto userDto=new UserDto();
        String password = generatePassword();
        String encodedPassword= bCryptPasswordEncoder.encode(password);
        userDto.setPassword(encodedPassword);
        userDto.setUserName(username);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setEmail(email);
        userDto.setJob(job);
        userDto.setCreationDate(new Date());
        userDto.setActive(true);
        userDto.setNotLocked(true);
        userDto.setRoles(getRoleEnumName(role).name());
        userDto.setAuthorities(getRoleEnumName(role).getAuthorities());
        userDto.setProfileImageUrl(getTemporaryProfileImageUrl(username));
        Users user= userRepository.save(BankingMapper.fromCustomerDto(userDto));
        saveProfileImage(user,profileImage);
        mailingService.sendEmail(userDto.getEmail(),password,"Your Password ");
    return userDto;
    }
    private Role getRoleEnumName(String role){
        return Role.valueOf(role.toUpperCase());
    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(FileConstant.DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }
    private void saveProfileImage(Users user, MultipartFile profileImage) throws IOException, NotAnImageFileException {
        if (profileImage != null) {
            if(!Arrays.asList(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
            }
            Path userFolder = Paths.get(USER_FOLDER + user.getUserName()).toAbsolutePath().normalize();
            if(!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder.toString(), user.getUserName() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUserName() + DOT + JPG_EXTENSION), StandardCopyOption.REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(user.getUserName()));
            userRepository.save(user);
        }
    }


    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(FileConstant.USER_IMAGE_PATH + username + FileConstant.FORWARD_SLASH
                + username + FileConstant.DOT + JPG_EXTENSION).toUriString();
    }

    @Override
    public UserDto updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail,String job, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws EmailExistException, UsernameExistException, IOException, NotAnImageFileException {
        UserDto currentUser=validateNewUsernameAndEmail(currentUsername , newUsername,newEmail);
        currentUser.setUserName(newUsername != null ? newUsername : currentUser.getUserName());
        currentUser.setFirstName(newFirstName != null ? newFirstName : currentUser.getFirstName());
        currentUser.setLastName(newLastName != null ? newLastName : currentUser.getLastName());
        currentUser.setEmail(newEmail);
        currentUser.setJob(job);
        currentUser.setCreationDate(new Date());
        currentUser.setActive(true);
        currentUser.setNotLocked(true);
        currentUser.setRoles(getRoleEnumName(role).name());
        currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        currentUser.setProfileImageUrl(getTemporaryProfileImageUrl(newUsername));
        userRepository.save(BankingMapper.fromCustomerDto(currentUser));
        saveProfileImage(BankingMapper.fromCustomerDto(currentUser),profileImage);

        return currentUser;
    }
    @Override
    public UserDto updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException, NotAnImageFileException {
        UserDto user = validateNewUsernameAndEmail(username, null, null);
        saveProfileImage(BankingMapper.fromCustomerDto(user), profileImage);
        return user;
    }


    @Override
    public void resetPassword(String email) throws EmailNotFoundException {
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException("NO_USER_FOUND_BY_EMAIL" + email);
        }
        String password = generatePassword();
        String encodedPassword= bCryptPasswordEncoder.encode(password);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        mailingService.sendEmail(email, password, "your new password");

}
    private String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }


}
