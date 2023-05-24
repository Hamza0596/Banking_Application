package com.banking.bankingapplication.service.serviceimpl;

import com.banking.bankingapplication.constant.SecurityConstant;
import com.banking.bankingapplication.domain.UserPrincipal;
import com.banking.bankingapplication.dtos.UserDto;
import com.banking.bankingapplication.entities.Users;
import com.banking.bankingapplication.enums.Role;
import com.banking.bankingapplication.exceptions.EmailExistException;
import com.banking.bankingapplication.exceptions.UserNotFoundException;
import com.banking.bankingapplication.exceptions.UsernameExistException;
import com.banking.bankingapplication.mappers.BankingMapper;
import com.banking.bankingapplication.repositories.UserRepository;
import com.banking.bankingapplication.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import static org.apache.commons.lang3.StringUtils.EMPTY;


import javax.transaction.Transactional;
import java.util.Date;
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


    @Override
    public UserDto register(UserDto userDto) throws EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail( EMPTY, userDto.getUserName(),userDto.getEmail());
        String password = generatePassword();
        System.out.println(password);
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
        Users user=userRepository.findByUserName(username);
        if(user==null){
            throw new UserNotFoundException("user not found by userName/Email:"+username);
        }else {
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal= new UserPrincipal(user);
            return userPrincipal;

        }
    }
}
