package com.banking.bankingapplication.controllers;

import com.banking.bankingapplication.constant.FileConstant;
import com.banking.bankingapplication.constant.SecurityConstant;
import com.banking.bankingapplication.domain.UserPrincipal;
import com.banking.bankingapplication.dtos.UserDto;
import com.banking.bankingapplication.exceptions.*;
import com.banking.bankingapplication.mappers.BankingMapper;
import com.banking.bankingapplication.service.UserService;
import com.banking.bankingapplication.utility.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

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
    JWTTokenProvider jwtTokenProvider;

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
        return new ResponseEntity<UserDto>(loginUser, jwtHeader, HttpStatus.OK);
    }




    @GetMapping("/search/{pageNumber}/{size}")
    public Page<UserDto> userFilter(@RequestParam String query , @PathVariable int pageNumber, @PathVariable int size ){
        return userService.filter(query,pageNumber,size);

    }

    @DeleteMapping ("/delete/{userId}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public void deleteUserById(@PathVariable Long userId) {
        userService.deleteUser(userId);

    }

    @PostMapping("/reset")
    public void changePassword( String email) throws BadCredentialsException, EmailNotFoundException {
        userService.resetPassword("hamzabouachir@yahoo.com");
    }

    @PostMapping("/add")
    public ResponseEntity<UserDto> addNewUser(@RequestParam String firstName,
                                            @RequestParam String lastName,
                                            @RequestParam String username,
                                            @RequestParam String email,
                                            @RequestParam String job,
                                            @RequestParam String role,
                                            @RequestParam boolean isActive,
                                            @RequestParam boolean isNonLocked,
                                            @RequestParam(required = false) MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        UserDto newUser = userService.addNewUser(firstName,lastName,username,email,job,role, isNonLocked, isActive, profileImage);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestParam(value = "currentUserName" ) String currentUserName,
                                              @RequestParam( required = false) String newFirstName,
                                              @RequestParam("newLastName") String newLastName,
                                              @RequestParam("newUsername") String newUsername,
                                              @RequestParam("newEmail") String newEmail,
                                              @RequestParam("newJob") String newJob,
                                              @RequestParam("newRole") String role,
                                              @RequestParam("isActive") boolean isActive,
                                              @RequestParam("isNonLocked") boolean isNonLocked,
                                              @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        UserDto updatedUser = userService.updateUser( currentUserName, newFirstName, newLastName,  newUsername,  newEmail, newJob, role, isNonLocked,  isActive,  profileImage);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


    @PostMapping("/updateProfileImage")
    public ResponseEntity<UserDto> updateProfileImage(@RequestParam("userName") String userName,
                                              @RequestParam(value = "profileImage") MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
     UserDto updatedUser=userService.updateProfileImage(userName,profileImage);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
        URL url = new URL(FileConstant.TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chunk = new byte[1024];
            while((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    @GetMapping(path = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(FileConstant.USER_FOLDER + username + FileConstant.FORWARD_SLASH + fileName));
    }

    @GetMapping("find/{userName}")
    public UserDto  getUser( @PathVariable String userName){
       return userService.findUserByUserName(userName);

    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }


}

