package com.banking.bankingapplication.ControllersTests;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.banking.bankingapplication.configuration.SecurityConfiguration;
import com.banking.bankingapplication.controllers.UserController;
import com.banking.bankingapplication.dtos.UserDto;
import com.banking.bankingapplication.enums.Role;
import com.banking.bankingapplication.filter.JwtAccessDeniedHandler;
import com.banking.bankingapplication.mappers.BankingMapper;
import com.banking.bankingapplication.service.UserService;
import com.banking.bankingapplication.utility.JWTTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.banking.bankingapplication.constant.SecurityConstant.*;
import static com.banking.bankingapplication.constant.SecurityConstant.EXPIRATION_TIME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {JwtAccessDeniedHandler.class})
@ExtendWith(SpringExtension.class)

 class UserControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    BankingMapper bankingMapper;


/*

    @Test
     void testGetAllCustomers() throws Exception {
        int pageNumber=0;
        int size=5;
        List<UserDto> userDtoList = new ArrayList<>();
        UserDto userDto1 = new UserDto();
        UserDto userDto2 = new UserDto();
        userDtoList.add(userDto1);
        userDtoList.add(userDto2);
        Mockito.when(userService.getUsers(pageNumber,size).getContent()).thenReturn(userDtoList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/users/{pageNumber}/{size}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(userDtoList.size()));
    }



    @Test
    void geCustomerById() throws Exception {
        String token;
        token = getToken();
        Long customerId=30L;
        UserDto userDto1 = new UserDto();
        userDto1.setId(customerId);
        userDto1.setUserName("Hamzawi");
        userDto1.setRoles(Role.ROLE_USER.name());
        userDto1.setEmail("hamzabouachir@yahoo.com");
        given(userService.user(customerId)).willReturn(userDto1);
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/user/users/{id}",customerId).header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(customerId));

    }

    public String getToken() throws Exception {
        String email = "foulen@gmail.com";
        Algorithm algorithm = Algorithm.HMAC512("HamzewiKeepGoingBroSahitBroTbarkalahAlikBro".getBytes());
        String[] roles = new String[1];
        roles[0]="ROLE_USER";
        String access_token = JWT.create()
                .withIssuer(GET_ARRAYS_LLC).withAudience(GET_ARRAYS_ADMINISTRATION)
                .withIssuedAt(new Date()).withSubject("Hamzawi")
                .withArrayClaim(AUTHORITIES, roles).withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);

        return "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJhdWQiOiJVc2VyIE1hbmFnZW1lbnQgUG9ydGFsIiwic3ViIjoiSGFtemF3aSIsImlzcyI6IkdldCBBcnJheXMsIExMQyIsIkF1dGhvcml0aWVzIjpbInVzZXI6cmVhZCJdLCJleHAiOjE2ODg4MzAyNTgsImlhdCI6MTY4ODM5ODI1OH0.U_ja7Gfp2Brbwl-dl4QR4A2_gTvXZMch1jIW8m3mW74pTbUc2IAfIe5lgynsz--35VOrHjPZCF5c7nniWmhutA";
    }


    @Test
     void testCreateCustomer() throws Exception {
        UserDto userDto = new UserDto();
        hello
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


*/

}
