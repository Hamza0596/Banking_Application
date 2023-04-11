package com.banking.bankingapplication.dtos;

import com.banking.bankingapplication.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountOperationDto {
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}
