package com.banking.bankingapplication.controllers;

import com.banking.bankingapplication.dtos.AccountHistoryDto;
import com.banking.bankingapplication.dtos.AccountOperationDto;
import com.banking.bankingapplication.dtos.BankAccountDto;
import com.banking.bankingapplication.mappers.BankingMapper;
import com.banking.bankingapplication.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/bankaccount")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private BankingMapper bankingMapper;

    @PostMapping("/bankAccount")
    public BankAccountDto createBankAccount(@RequestParam Long customerId, @RequestParam double balance, @RequestParam String type)  {

        return bankAccountService.createBankAccount(customerId,balance,type);
    }
    @GetMapping("/bankaccounts/users/{userId}")
    public List<BankAccountDto> getBankAccountsByUserId(@PathVariable Long userId){
        return bankAccountService.getBankAccountsByUserId(userId);
    }

    @GetMapping("bankAccounts/{bankAccountId}")
    public BankAccountDto getBankAccountById(@PathVariable String bankAccountId)  {
        return bankAccountService.getBankAccount(bankAccountId);
    }

    @PostMapping("debit")
    public  void debit(@RequestParam String accountId ,@RequestParam  Double amount, @RequestParam  String description ) {
            bankAccountService.debit(accountId,amount,description);
    }

    @PostMapping("credit")
    public void credit(@RequestParam String accountId,@RequestParam double amount, @RequestParam String description)  {
        bankAccountService.credit(accountId,amount,description);
    }

    @PostMapping("transfert")
    public void transfer(@RequestParam  String accountIdSource , @RequestParam  String accoundIdDestinatin,@RequestParam  double amount)  {
        bankAccountService.transfer(accountIdSource,accoundIdDestinatin,amount);

    }


    @GetMapping("/history/{accountId}")
    public List<AccountOperationDto> accountHistory(@PathVariable String accountId ){
        return  bankAccountService.accoutntHistory(accountId);
    }

    @GetMapping("accounts/{accountId}/pageoperations")
    public AccountHistoryDto getAccountHistory(@PathVariable String accountId,
                                                     @RequestParam(name = "page",defaultValue = "0") int page ,
                                                     @RequestParam (name = "size",defaultValue = "3")int size)  {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }

    @DeleteMapping("delete/{id}")
    public boolean deleteAccountById(@PathVariable String id){
        return bankAccountService.deleteBankAccount(id);
    }


}
