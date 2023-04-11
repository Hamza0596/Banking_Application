package com.banking.bankingapplication.dtos;

import com.banking.bankingapplication.entities.Customer;
import com.banking.bankingapplication.enums.AccountStatus;
import lombok.Data;

import java.util.Date;
@Data
public class CurrentAcountDto extends BankAccountDto{

    private String id;
    private double balnce;
    private Date createdAt;
    private AccountStatus status;
    private Customer customer;
    private double overDraft;

}
