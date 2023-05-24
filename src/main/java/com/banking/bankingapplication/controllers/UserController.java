package com.banking.bankingapplication.controllers;

import com.banking.bankingapplication.constant.SecurityConstant;
import com.banking.bankingapplication.domain.UserPrincipal;
import com.banking.bankingapplication.dtos.UserDto;
import com.banking.bankingapplication.exceptions.EmailExistException;
import com.banking.bankingapplication.exceptions.UsernameExistException;
import com.banking.bankingapplication.mappers.BankingMapper;
import com.banking.bankingapplication.service.UserService;
import com.banking.bankingapplication.utility.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    BankingMapper bankingMapper;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @GetMapping("/users/{pageNumber}/{size}")
    public Page<UserDto> users(@PathVariable int pageNumber , @PathVariable int size){
        return userService.getUsers(pageNumber, size);
    }


    @GetMapping("/users/{id}")
    public UserDto getUserById(@PathVariable Long id)  {
        return userService.user(id);
    }

    @PostMapping("/register")
    public UserDto Register(@RequestBody UserDto userDto) throws EmailExistException, UsernameExistException {
           System.out.println("okkkkkk");
           return userService.register(userDto);}


    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto user) {
        authenticate(user.getUserName(), user.getPassword());
        UserDto loginUser = userService.findUserByUserName(user.getUserName());
        UserPrincipal userPrincipal = new UserPrincipal(BankingMapper.fromCustomerDto(loginUser));
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    /*@PostMapping("users/update/{id}")
    public UserDto createUser(@RequestBody UserDto userDto, @PathVariable Long id){
        userDto.setId(id);
        return userService.register(userDto);}*/


    @GetMapping("/search/{pageNumber}/{size}")
    public Page<UserDto> userFilter(@RequestParam String query , @PathVariable int pageNumber, @PathVariable int size ){
        return userService.filter(query,pageNumber,size);

    }

    @DeleteMapping ("/delete/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        userService.deleteUser(userId);

    }
}

