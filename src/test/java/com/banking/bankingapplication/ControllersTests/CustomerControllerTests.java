package com.banking.bankingapplication.ControllersTests;

import com.banking.bankingapplication.controllers.CustomerController;
import com.banking.bankingapplication.dtos.CustomerDto;
import com.banking.bankingapplication.mappers.BankingMapper;
import com.banking.bankingapplication.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(CustomerController.class)
 class CustomerControllerTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @MockBean
    BankingMapper bankingMapper;
    @Test
     void testGetAllCustomers() throws Exception {
        List<CustomerDto> customerDtoList = new ArrayList<>();
        CustomerDto customerDto1 = new CustomerDto();
        CustomerDto customerDto2 = new CustomerDto();
        customerDtoList.add(customerDto1);
        customerDtoList.add(customerDto2);
        when(customerService.getAllCustomers()).thenReturn(customerDtoList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customer/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(customerDtoList.size()));
    }

    @Test
    void geCustomerById() throws Exception {
        Long customerId=1L;
        CustomerDto customerDto1 = new CustomerDto();
        customerDto1.setId(customerId);
        when(customerService.customer(customerId)).thenReturn(customerDto1);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customer/customers/{id}",customerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(customerId));

    }

    @Test
     void testCreateCustomer() throws Exception {
        CustomerDto customerDto = new CustomerDto();
        when(customerService.createCustomer(any(CustomerDto.class))).thenReturn(customerDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customer/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": null,\"name\": \"John\",\"email\": \"johndoe@test.com\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(customerDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(customerDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customerDto.getEmail()));
    }
}
