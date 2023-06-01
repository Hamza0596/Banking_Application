package com.banking.bankingapplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordChangeRequestDto {
    String username;
    String oldPassword;
    String newPassword;

}
