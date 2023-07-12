package com.banking.bankingapplication;

import com.banking.bankingapplication.ControllersTests.SmtpServerRule;
import com.banking.bankingapplication.domain.UserPrincipal;
import com.banking.bankingapplication.dtos.ResetPasswordDto;
import com.banking.bankingapplication.dtos.UserDto;
import com.banking.bankingapplication.entities.Users;
import com.banking.bankingapplication.exceptions.*;
import com.banking.bankingapplication.mappers.BankingMapper;
import com.banking.bankingapplication.repositories.UserRepository;
import com.banking.bankingapplication.service.UserService;
import com.banking.bankingapplication.service.serviceimpl.LoginAttemptService;
import com.banking.bankingapplication.service.serviceimpl.MailingService;
import com.banking.bankingapplication.service.serviceimpl.UserServiceImpl;
import com.icegreen.greenmail.util.GreenMailUtil;
import jakarta.mail.MessagingException;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Message;


@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
 class UserServiceTests {

    @Autowired
    UserService userService;

    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    MailingService mailingService;

    @Autowired
    UserRepository userRepository;
    private GreenMail greenMail;



    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
     void getAllCustomers() {
        assertEquals("karim", userService.getUsers(0,3).getContent().get(0).getFirstName());
    }

    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

    void Customer() {
        assertEquals("Hamzaaa", userService.user(1L).getFirstName());
    }

    @Test
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCustomer() throws EmailExistException, UsernameExistException {
        UserDto userDto = new UserDto();
        userDto.setFirstName("Mohamed");
        userDto.setEmail("john.doe@example.com");

        assertEquals("Mohamed", userService.register(userDto).getFirstName());
    }

    @Test
   void validateNewUsernameAndEmailWhenNullUser() throws EmailExistException, UsernameExistException {
        assertThrows(UserNotFoundException.class, () -> {
            userServiceImpl.validateNewUsernameAndEmail("Hamza","Hamzawi","hamzabouachir@yahoo.com");
        },"NO_USER_FOUND_BY_USERNAMEHamza");
    }

    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void validateNewUsernameAndEmailWhenUserNameAlreadyExists() throws EmailExistException, UsernameExistException {

        assertThrows(UsernameExistException.class, () -> {
            userServiceImpl.validateNewUsernameAndEmail("hmoz","fdod","hamzabouachir@yahoo.com");
        },"USERNAME_ALREADY_EXISTS");
    }


    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void validateNewUsernameAndEmailWhenEmailAlreadyExists() throws EmailExistException, UsernameExistException {
        assertThrows(EmailExistException.class, () -> {
            userServiceImpl.validateNewUsernameAndEmail("karim","karim","hamzabouachir@yahoo.com");
        },"EMAIL_ALREADY_EXISTS");
    }


    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void validateNewUsernameAndEmail() throws EmailExistException, UsernameExistException {
      UserDto user= new UserDto();
      user.setId(5L);
      user.setFirstName("karim");
      user.setLastName("karim");
      user.setUserName("karim");
      user.setEmail("karim@yahoo.com");
      user.setJob("ing");
      user.setActive(true);
      user.setNotLocked(true);
        assertEquals(user,userServiceImpl.validateNewUsernameAndEmail("karim","karimo","karim@yahoo.com"));
    }




    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void validateNewUsernameAndEmailWhenCurentUserNameIsEmptyAndNewUserNameExist() throws EmailExistException, UsernameExistException {
        assertThrows(EmailExistException.class, () -> {
            userServiceImpl.validateNewUsernameAndEmail("","karimo","hamzabouachir@yahoo.com");

        },"EMAIL_ALREADY_EXISTS");
    }


    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void validateNewUsernameAndEmailWhenWhenCurentUserNameIsEmptyAndNewUserEmailExist() throws EmailExistException, UsernameExistException {
        assertThrows(EmailExistException.class, () -> {
            userServiceImpl.validateNewUsernameAndEmail("","karimo","hamzabouachir@yahoo.com");

        },"EMAIL_ALREADY_EXISTS");
    }

    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void validateNewUsernameAndEmailWhenNewCurrentUserNameIsEmpty() throws EmailExistException, UsernameExistException {

        assertEquals(null,userServiceImpl.validateNewUsernameAndEmail("","kiki","kikibouachir@yahoo.com"));
    }



    @Test
    void loadUserByUsernameWhenUserIsNull(){
        assertEquals(null,  userServiceImpl.loadUserByUsername("amor"));

    }
    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void loadUserByUsernameWhenUserIsPresent(){
        UserDto user= new UserDto();
        user.setId(1L);
        user.setFirstName("Hamzaaa");
        user.setLastName("BOUACHIR");
        user.setUserName("hmoz");
        user.setEmail("hamzabouachir@yahoo.com");
        user.setJob("ing");
        user.setActive(true);
        user.setNotLocked(true);
        assertEquals(new UserPrincipal(BankingMapper.fromCustomerDto(user)).getUsername(),  userServiceImpl.loadUserByUsername("hmoz").getUsername());

    }

    @Test
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addNewUser() throws EmailExistException, IOException, UsernameExistException, NotAnImageFileException {
        userService.addNewUser("ali","bouzayen","aloulou","hamza.bouachir@talan.com","ing","ROLE_USER",true,true,null);
        assertEquals(1,userService.getUsers(0,5).getContent().size());
    }

    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteUser(){
        assertTrue( userService.deleteUser(1L));
        assertTrue( userService.deleteUser(2L));
        assertTrue( userService.deleteUser(3L));
        assertTrue( userService.deleteUser(5L));

    }

    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateUser() throws EmailExistException, IOException, UsernameExistException, NotAnImageFileException {
        userService.updateUser("hmoz","zouzou","zouzou","hmoza","hamzabouachir@yahoo.com","ing","ROLE_USER",true,true,null);
        assertEquals("zouzou",userService.user(1L).getFirstName());
    }

    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void chagePasswordWhenUserDosentExist() throws PasswordDoNotMatcheException, EmailNotFoundException {
        ResetPasswordDto resetPasswordDto= new ResetPasswordDto();
        resetPasswordDto.setEmail("kk@gmail.com");
        assertThrows(EmailNotFoundException.class, () -> {
            userService.chagePassword(resetPasswordDto);

        },"NO_USER_FOUND_BY_EMAIL" + resetPasswordDto.getEmail());

    }

    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void chagePasswordWhenUserExist() throws PasswordDoNotMatcheException, EmailNotFoundException {
        ResetPasswordDto resetPasswordDto= new ResetPasswordDto();
        resetPasswordDto.setEmail("hamzabouachir@yahoo.com");
        resetPasswordDto.setOldPassword("gCKkBqheQf");
        resetPasswordDto.setNewPassword("hamza");
        userService.chagePassword(resetPasswordDto);
        BCryptPasswordEncoder bCryptPasswordEncoder= new BCryptPasswordEncoder();

       assertEquals(true,bCryptPasswordEncoder.matches("hamza", userService.user(1L).getPassword()));
    }

    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void chagePasswordWhenUserExistAndWrongOldPassword() throws PasswordDoNotMatcheException, EmailNotFoundException {
        ResetPasswordDto resetPasswordDto= new ResetPasswordDto();
        resetPasswordDto.setEmail("hamzabouachir@yahoo.com");
        resetPasswordDto.setOldPassword("gCKkBqheQf");
        resetPasswordDto.setNewPassword("hamza");
        userService.chagePassword(resetPasswordDto);

        assertThrows(PasswordDoNotMatcheException.class, () -> {
            userService.chagePassword(resetPasswordDto);

        },"Incorrecet old paswword");
    }





}
