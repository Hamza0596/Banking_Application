package com.banking.bankingapplication;

import com.banking.bankingapplication.dtos.UserDto;
import com.banking.bankingapplication.exceptions.EmailExistException;
import com.banking.bankingapplication.exceptions.UserNotFoundException;
import com.banking.bankingapplication.exceptions.UsernameExistException;
import com.banking.bankingapplication.service.UserService;
import com.banking.bankingapplication.service.serviceimpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
 class UserServiceTests {

    @Autowired
    UserService userService;

    @Autowired
    UserServiceImpl userServiceImpl;

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
      user.setId(1L);
      user.setFirstName("Hamzaaa");
      user.setLastName("BOUACHIR");
      user.setUserName("hmoz");
      user.setEmail("hamzabouachir@yahoo.com");
      user.setJob("ing");
      user.setActive(true);
      user.setNotLocked(true);
        assertEquals(user,userServiceImpl.validateNewUsernameAndEmail("hmoz","zouzou","zouzoubouachir@yahoo.com"));
    }

    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void validateNewUsernameAndEmailWhenNewUserNameIsEmpty() throws EmailExistException, UsernameExistException {
        assertThrows(UsernameExistException.class, () -> {
            userServiceImpl.validateNewUsernameAndEmail("","karim","hamzabouachir@yahoo.com");

        },"USERNAME_ALREADY_EXISTS");
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

}
