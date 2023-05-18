package com.banking.bankingapplication;

import com.banking.bankingapplication.dtos.CustomerDto;
import com.banking.bankingapplication.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
 class CustomerServiceTests {

    @Autowired
    CustomerService customerService;

    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
     void getAllCustomers() {
        assertEquals("Hamzaaa",customerService.getAllCustomers(0,3).getContent().get(0).getFirstName());
    }

    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
     void Customer() {
        assertEquals("Hamzaaa",customerService.customer(1L).getFirstName());
    }

    @Test
     void createCustomer() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(1L);
        customerDto.setFirstName("Mohamed");
        customerDto.setEmail("john.doe@example.com");

        assertEquals("Mohamed",customerService.createCustomer(customerDto).getFirstName());
    }

}
