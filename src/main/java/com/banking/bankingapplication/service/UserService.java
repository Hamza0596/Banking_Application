package com.banking.bankingapplication.service;

import com.banking.bankingapplication.dtos.ResetPasswordDto;
import com.banking.bankingapplication.dtos.UserDto;
import com.banking.bankingapplication.exceptions.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

    public UserDto register(UserDto userDto) throws EmailExistException, UsernameExistException;
      public Page<UserDto> getUsers(int pageNumber, int size);
    public UserDto user(Long id) ;

    public UserDto findUserByUserName(String firstName);
    public UserDto findUserByEmail(String email);

    Page<UserDto> filter(String query, int pageNumber, int size);

    public void deleteUser(Long userId);
    UserDto addNewUser(String firstName, String lastName, String userName, String email,String job, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;
    UserDto updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail,String job, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws EmailExistException, UsernameExistException, IOException, NotAnImageFileException;
    void chagePassword(ResetPasswordDto resetPasswordDto ) throws EmailNotFoundException, PasswordDoNotMatcheException;
   void sendEmailResetPassword(String email) throws EmailNotFoundException;
   void resetPassword(String email, String token) throws EmailNotFoundException, RestTokenExpiredException;
    public UserDto updateProfileImage(String username, MultipartFile profileImage) throws EmailExistException, UsernameExistException, IOException, NotAnImageFileException;
    UserDto validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException ;


    }
