package com.banking.bankingapplication.service.serviceimpl;

import com.banking.bankingapplication.entities.PasswordResetToken;
import com.banking.bankingapplication.repositories.PasswordResetTokenRepository;
import com.banking.bankingapplication.service.PasswordResetTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
@Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;
    @Override
    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
        return passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }
}
