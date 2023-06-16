package com.banking.bankingapplication.service;

import com.banking.bankingapplication.entities.PasswordResetToken;

public interface PasswordResetTokenService {


    public PasswordResetToken save(PasswordResetToken passwordResetToken);
    public PasswordResetToken findByToken (String token);
}
