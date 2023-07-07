package com.banking.bankingapplication.service.serviceimpl;

import com.banking.bankingapplication.constant.FileConstant;
import com.banking.bankingapplication.domain.UserPrincipal;
import com.banking.bankingapplication.dtos.ResetPasswordDto;
import com.banking.bankingapplication.dtos.UserDto;
import com.banking.bankingapplication.entities.PasswordResetToken;
import com.banking.bankingapplication.entities.Users;
import com.banking.bankingapplication.enums.Role;
import com.banking.bankingapplication.exceptions.*;
import com.banking.bankingapplication.mappers.BankingMapper;
import com.banking.bankingapplication.repositories.UserRepository;
import com.banking.bankingapplication.service.PasswordResetTokenService;
import com.banking.bankingapplication.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


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
    @Autowired
    PasswordResetTokenService passwordResetTokenService;

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




    @Override
    public Page<UserDto> getUsers(int pageNumber, int size) {
        return bankingMapper.fromCustomerListToCustomerPageDto(userRepository.findAllByOrderByCreationDateDesc(PageRequest.of(pageNumber,size)));
    }

    @Override
    public UserDto user(Long id)   {
        return BankingMapper.fromCustomer(userRepository.findById(id).orElseThrow(()->new UserNotFoundException("No user was found with this id")));
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
    public boolean deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
            return true; // Suppression réussie
        } catch (Exception e) {
            // Gérer toute exception éventuelle lors de la suppression
            e.printStackTrace();
            return false; // Échec de la suppression
        }
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

            return new UserPrincipal(user);
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
    public UserDto addNewUser(String firstName, String lastName, String userName, String email, String job, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        validateNewUsernameAndEmail(EMPTY,userName,email);
        UserDto userDto=new UserDto();
        String password = generatePassword();
        String encodedPassword= bCryptPasswordEncoder.encode(password);
        userDto.setPassword(encodedPassword);
        userDto.setUserName(userName);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setEmail(email);
        userDto.setJob(job);
        userDto.setCreationDate(new Date());
        userDto.setActive(true);
        userDto.setNotLocked(true);
        userDto.setRoles(getRoleEnumName(role).name());
        userDto.setAuthorities(getRoleEnumName(role).getAuthorities());
        userDto.setProfileImageUrl(getTemporaryProfileImageUrl(userName));
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
    @Override
    public UserDto updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException, NotAnImageFileException {
        try {
            UserDto user = validateNewUsernameAndEmail(username, null, null);
            saveProfileImage(BankingMapper.fromCustomerDto(user), profileImage);
            return user;
        } catch (UserNotFoundException | EmailExistException | UsernameExistException | NotAnImageFileException e) {
            // Handle specific exceptions here or rethrow them if necessary
            throw e;
        } catch (IOException e) {
            // Handle IOException here
            // For example, log the error or throw a custom exception
            throw new IOException("Error occurred while updating profile image: " + e.getMessage(), e);
        }
    }

    @Override
    public UserDto validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
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
                throw new EmailExistException("EMAIL_ALREADY_EXISTS");}return currentUser;} else {
            if(userByNewUsername != null) {
                throw new UsernameExistException("USERNAME_ALREADY_EXISTS");}
            if(userByNewEmail != null) {
                throw new EmailExistException("EMAIL_ALREADY_EXISTS");}
            return null;
        }
    }

    private void saveProfileImage(Users user, MultipartFile profileImage) throws IOException, NotAnImageFileException {
        if (profileImage != null) {
            try {
                if (!Arrays.asList(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                    throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
                }
                Path userFolder = Paths.get(USER_FOLDER + user.getUserName()).toAbsolutePath().normalize();
                if (!Files.exists(userFolder)) {
                    Files.createDirectories(userFolder);
                }
                Files.deleteIfExists(Paths.get(userFolder.toString(), user.getUserName() + DOT + JPG_EXTENSION));
                Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUserName() + DOT + JPG_EXTENSION), StandardCopyOption.REPLACE_EXISTING);
                user.setProfileImageUrl(setProfileImageUrl(user.getUserName()));
                userRepository.save(user);
            } catch (IOException e) {
                // Handle IOException here
                // For example, log the error or throw a custom exception
                throw new IOException("Error occurred while saving profile image: " + e.getMessage(), e);
            } catch (NotAnImageFileException e) {
                // Handle NotAnImageFileException here
                // For example, log the error or throw a custom exception
                throw e;
            }
        }
    }



    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(FileConstant.USER_IMAGE_PATH + username + FileConstant.FORWARD_SLASH
                + username + FileConstant.DOT + JPG_EXTENSION).toUriString();
    }

    @Override
    public UserDto updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail,String job, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws EmailExistException, UsernameExistException, IOException, NotAnImageFileException {
        UserDto currentUser=validateNewUsernameAndEmail(currentUsername , newUsername,newEmail);
        if(currentUser!=null){
        currentUser.setUserName(newUsername != null ? newUsername : currentUser.getUserName());
        currentUser.setFirstName(newFirstName != null ? newFirstName : currentUser.getFirstName());
        currentUser.setLastName(newLastName != null ? newLastName : currentUser.getLastName());
        currentUser.setEmail(newEmail);
        currentUser.setJob(job);
        currentUser.setCreationDate(new Date());
        currentUser.setActive(isActive);
        currentUser.setNotLocked(isNonLocked);
        currentUser.setRoles(getRoleEnumName(role).name());
        currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        currentUser.setProfileImageUrl(getTemporaryProfileImageUrl(newUsername));
        userRepository.save(BankingMapper.fromCustomerDto(currentUser));
        saveProfileImage(BankingMapper.fromCustomerDto(currentUser),profileImage);
        }
        return currentUser;


    }



    @Override
    public void chagePassword(ResetPasswordDto resetPasswordDto ) throws EmailNotFoundException, PasswordDoNotMatcheException {
        Users user = userRepository.findByEmail(resetPasswordDto.getEmail());
        if (user == null) {
            throw new EmailNotFoundException("NO_USER_FOUND_BY_EMAIL" + resetPasswordDto.getEmail());
        }
        if (bCryptPasswordEncoder.matches(resetPasswordDto.getOldPassword(), user.getPassword())) {
            String encodedNewPassword = bCryptPasswordEncoder.encode(resetPasswordDto.getNewPassword());
            user.setPassword(encodedNewPassword);
            userRepository.save(user);
            mailingService.sendEmail(resetPasswordDto.getEmail(), resetPasswordDto.getNewPassword(), "your new password");
        }else {
            throw new PasswordDoNotMatcheException("Incorrecet old paswword");
        }

}

    @Override
    public void sendEmailResetPassword(String email) throws EmailNotFoundException {
        Optional<Users> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        Users user = userOptional.orElseThrow(() -> new EmailNotFoundException("NO_USER_FOUND_BY_EMAIL " +email));
        String token = generateUniqueToken();

        // Créez un objet PasswordResetToken
        LocalDateTime expirationDate = LocalDateTime.now().plusSeconds(60); // Expiration dans 1 heure
        PasswordResetToken resetToken = new PasswordResetToken(token, expirationDate, user);
        passwordResetTokenService.save(resetToken);
        mailingService.sendEmail(user.getEmail(), "http://localhost:4200/resetpassword/"+email+"/"+token, "Reseting password");

    }

    private String generateUniqueToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @Override
    public void resetPassword(String email,String token) throws EmailNotFoundException, RestTokenExpiredException {
        Users user=userRepository.findByEmail(email);
        PasswordResetToken resetToken= passwordResetTokenService.findByToken(token);
        if(user==null ){
            throw  new EmailNotFoundException("NO_USER_FOUND_BY_EMAIL " +email);
        }
        if( resetToken.isExpired()){
            throw  new RestTokenExpiredException("reset token expired");
        }

        String password = generatePassword();
        String encodedNewPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
        mailingService.sendEmail(user.getEmail(), password, password);
        /*sssssssss*/

    }


}
