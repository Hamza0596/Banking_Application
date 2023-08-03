package com.banking.bankingapplication.service;

import com.banking.bankingapplication.dtos.AccountHistoryDto;
import com.banking.bankingapplication.dtos.AccountOperationDto;
import com.banking.bankingapplication.dtos.BankAccountDto;
import com.banking.bankingapplication.entities.Post;
import com.banking.bankingapplication.exceptions.InsufisantSoldeException;
import com.banking.bankingapplication.exceptions.BankAccountNotFoundException;
import com.banking.bankingapplication.exceptions.UserNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;
public interface BankAccountService {
    public BankAccountDto createBankAccount(Long customerId, double balance, String type) throws UserNotFoundException;
    public List<BankAccountDto> getBankAccountsByUserId(Long id) ;
    public boolean deleteBankAccount(String id);
    public BankAccountDto getBankAccount(String bankId) throws BankAccountNotFoundException;
    public void debit(String accountId, double amount , String description) throws InsufisantSoldeException, BankAccountNotFoundException;
    public void credit(String accountId, double amount , String description) throws BankAccountNotFoundException;
    public void transfer(String accountIdSource , String accoundIdDestinatin, double amount) throws BankAccountNotFoundException, InsufisantSoldeException;
    public List<AccountOperationDto> accoutntHistory(String accountId);
    public AccountHistoryDto getAccountHistory(String accountId, int page , int size) throws BankAccountNotFoundException;

    List<BankAccountDto> getBankAccounts();
    ResponseEntity<String> verifySolde(String accountId, double amount , String description);

    List<Post> getPosts();
}
