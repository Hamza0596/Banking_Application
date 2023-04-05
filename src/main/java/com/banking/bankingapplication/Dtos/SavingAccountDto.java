package com.banking.bankingapplication.Dtos;

import com.banking.bankingapplication.Entities.Customer;
import com.banking.bankingapplication.Enum.AccountStatus;
import lombok.Data;

import java.util.Date;
@Data

public class SavingAccountDto extends BankAccountDto {
    private String id;
    private double balnce;
    private Date createdAt;
    private AccountStatus status;
    private Customer customer;
    private double intersetRating;
}
