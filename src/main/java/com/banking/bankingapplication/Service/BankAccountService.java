package com.banking.bankingapplication.Service;

import com.banking.bankingapplication.Dtos.BankAccountDto;
import com.banking.bankingapplication.Entities.BankAccount;
import com.banking.bankingapplication.Exceptions.BalanceNotFoundException;
import com.banking.bankingapplication.Exceptions.BankAccountNotFoundException;
import com.banking.bankingapplication.Exceptions.UserNotFoundException;

import java.util.List;
public interface BankAccountService {
    public BankAccountDto createBankAccount(Long customerId, double balance, String type) throws UserNotFoundException;
    public List<BankAccountDto> getBankAccountsByUserId(Long id) ;
    public void deleteBankAccount(String id);
    public BankAccountDto getBankAccount(String bankId) throws BankAccountNotFoundException;
    public void debit(String accountId, double amount , String description) throws BalanceNotFoundException, BankAccountNotFoundException;
    public void credit(String accountId, double amount , String description) throws BankAccountNotFoundException;
    public void Trasfer(String AccountIdSource , String accoundIdDestinatin, double amount) throws BankAccountNotFoundException;



}
