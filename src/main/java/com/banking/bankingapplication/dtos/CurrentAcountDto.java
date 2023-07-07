package com.banking.bankingapplication.dtos;

import com.banking.bankingapplication.entities.Users;
import com.banking.bankingapplication.enums.AccountStatus;
import lombok.Data;

import java.util.Date;
@Data
public class CurrentAcountDto extends BankAccountDto{

    private String id;
    private double balnce;
    private Date createdAt;
    private AccountStatus status;
    private Users user;
    private double overDraft;

}
