package com.banking.bankingapplication.ControllersTests;

import com.banking.bankingapplication.controllers.UserController;
import com.banking.bankingapplication.dtos.UserDto;
import com.banking.bankingapplication.mappers.BankingMapper;
import com.banking.bankingapplication.service.UserService;
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

@WebMvcTest(UserController.class)
 class UserControllerTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    BankingMapper bankingMapper;
    @Test
     void testGetAllCustomers() throws Exception {
        List<UserDto> userDtoList = new ArrayList<>();
        UserDto userDto1 = new UserDto();
        UserDto userDto2 = new UserDto();
        userDtoList.add(userDto1);
        userDtoList.add(userDto2);
        when(userService.getUsers(0,5).getContent()).thenReturn(userDtoList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customer/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(userDtoList.size()));
    }

    @Test
    void geCustomerById() throws Exception {
        Long customerId=1L;
        UserDto userDto1 = new UserDto();
        userDto1.setId(customerId);
        when(userService.user(customerId)).thenReturn(userDto1);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customer/customers/{id}",customerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(customerId));

    }

    @Test
     void testCreateCustomer() throws Exception {
        UserDto userDto = new UserDto();
        when(userService.register(any(UserDto.class))).thenReturn(userDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customer/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": null,\"name\": \"John\",\"email\": \"johndoe@test.com\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(userDto.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userDto.getEmail()));
    }
}
