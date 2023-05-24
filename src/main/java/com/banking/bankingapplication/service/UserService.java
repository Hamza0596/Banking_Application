package com.banking.bankingapplication.service;

import com.banking.bankingapplication.dtos.UserDto;
import com.banking.bankingapplication.exceptions.EmailExistException;
import com.banking.bankingapplication.exceptions.UsernameExistException;
import org.springframework.data.domain.Page;

public interface UserService {

    public UserDto register(UserDto userDto) throws EmailExistException, UsernameExistException;
      public Page<UserDto> getUsers(int pageNumber, int size);
    public UserDto user(Long id) ;

    public UserDto findUserByUserName(String firstName);
    public UserDto findUserByEmail(String email);

    Page<UserDto> filter(String query, int pageNumber, int size);

    public void deleteUser(Long userId);


}
