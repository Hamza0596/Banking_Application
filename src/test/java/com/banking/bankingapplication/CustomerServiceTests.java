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
public class CustomerServiceTests {

    @Autowired
    CustomerService customerService;

    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllCustomers() {
        assertEquals(customerService.getAllCustomers().get(0).getName(),"Hamzaaa");
    }

    @Test
    @Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void Customer() {
        assertEquals(customerService.customer(1L).getName(),"Hamzaaa");
    }

    @Test
    public void createCustomer() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(1L);
        customerDto.setName("Mohamed");
        customerDto.setEmail("john.doe@example.com");

        assertEquals(customerService.createCustomer(customerDto).getName(),"Mohamed");
    }

}
