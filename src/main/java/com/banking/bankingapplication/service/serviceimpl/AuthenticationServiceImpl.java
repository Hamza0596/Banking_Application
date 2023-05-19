package com.banking.bankingapplication.service.serviceimpl;


import com.banking.bankingapplication.configuration.JwtService;
import com.banking.bankingapplication.dtos.AuthResponse;
import com.banking.bankingapplication.dtos.LoginDto;
import com.banking.bankingapplication.dtos.RegisterDto;
import com.banking.bankingapplication.entities.Customer;
import com.banking.bankingapplication.enums.Role;
import com.banking.bankingapplication.repositories.CustomerRepository;
import com.banking.bankingapplication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final CustomerRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public String register(RegisterDto request) {

        if (repository.existsByEmail(request.getEmail()))
            return null;
        Customer user = Customer.builder().firstName(request.getFirstName()).lastName(request.getLastName()).job(request.getJob()).creationDate(request.getCreationDate())
                .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).role(Role.USER)
                .build();
        repository.save(user);

        return "Le compte est créé avec succès";
    }

    @Override
    public AuthResponse authenticate(LoginDto request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Customer user = repository.findByEmail(request.getEmail()).orElseThrow(NullPointerException::new);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        return AuthResponse.builder().token(jwtService.generateToken(user)).build();
    }

}

