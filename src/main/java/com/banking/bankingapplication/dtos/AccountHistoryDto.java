package com.banking.bankingapplication.dtos;

import lombok.Data;

import java.util.List;
@Data
public class AccountHistoryDto {
    private List<AccountOperationDto> accountOperationDtos;
    private String accountId;
    private double balance;
    private String type;
    private int currentPage;
    private int totalPages;
    private int size;

}
