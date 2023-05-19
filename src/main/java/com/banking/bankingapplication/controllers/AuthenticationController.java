package com.banking.bankingapplication.controllers;

import com.banking.bankingapplication.dtos.AuthResponse;
import com.banking.bankingapplication.dtos.LoginDto;
import com.banking.bankingapplication.dtos.RegisterDto;
import com.banking.bankingapplication.dtos.RegisterResponse;
import com.banking.bankingapplication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterDto request) {
        String msg = service.register(request);
        if (msg ==null)
            return new ResponseEntity<>(new RegisterResponse("Un compte avec cet email existe déja"),
                    HttpStatus.CONFLICT);
        return new ResponseEntity<>(new RegisterResponse(msg), HttpStatus.CREATED);

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody LoginDto request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}
