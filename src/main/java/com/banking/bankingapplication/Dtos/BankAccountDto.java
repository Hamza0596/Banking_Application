package com.banking.bankingapplication.Dtos;

import com.banking.bankingapplication.Entities.AccountOperations;
import com.banking.bankingapplication.Entities.Customer;
import com.banking.bankingapplication.Enum.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class BankAccountDto  {
    private String id;
    private double balnce;
    private Date createdAt;
    private AccountStatus status;
    private Customer customer;
}
