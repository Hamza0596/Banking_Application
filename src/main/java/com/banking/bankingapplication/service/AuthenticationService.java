package com.banking.bankingapplication.service;

import com.banking.bankingapplication.dtos.AuthResponse;
import com.banking.bankingapplication.dtos.LoginDto;
import com.banking.bankingapplication.dtos.RegisterDto;

public interface AuthenticationService {

    public String register(RegisterDto request);

    public AuthResponse authenticate(LoginDto request);

}

