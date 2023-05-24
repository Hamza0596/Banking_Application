package com.banking.bankingapplication;

import com.banking.bankingapplication.dtos.UserDto;
import com.banking.bankingapplication.exceptions.EmailExistException;
import com.banking.bankingapplication.exceptions.UsernameExistException;
import com.banking.bankingapplication.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
 class UserServiceTests {

    @Autowired
    UserService userService;

    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
     void getAllCustomers() {
        assertEquals("Hamzaaa", userService.getUsers(0,3).getContent().get(0).getFirstName());
    }

    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
     void Customer() {
        assertEquals("Hamzaaa", userService.user(1L).getFirstName());
    }

    @Test
     void createCustomer() throws EmailExistException, UsernameExistException {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("Mohamed");
        userDto.setEmail("john.doe@example.com");

        assertEquals("Mohamed", userService.register(userDto).getFirstName());
    }

}
