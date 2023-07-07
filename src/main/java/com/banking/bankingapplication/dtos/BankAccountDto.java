package com.banking.bankingapplication.dtos;

import com.banking.bankingapplication.entities.Users;
import com.banking.bankingapplication.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BankAccountDto  {
    private String id;
    private double balnce;
    private Date createdAt;
    private AccountStatus status;
    private Users user;
    private  String type;
}
