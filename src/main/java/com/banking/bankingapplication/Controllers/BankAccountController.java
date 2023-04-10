package com.banking.bankingapplication.Controllers;

import com.banking.bankingapplication.Dtos.AccountOperationDto;
import com.banking.bankingapplication.Dtos.BankAccountDto;
import com.banking.bankingapplication.Entities.BankAccount;
import com.banking.bankingapplication.Exceptions.BalanceNotFoundException;
import com.banking.bankingapplication.Exceptions.BankAccountNotFoundException;
import com.banking.bankingapplication.Exceptions.UserNotFoundException;
import com.banking.bankingapplication.Mappers.BankingMapper;
import com.banking.bankingapplication.Repositories.BankAccountRepository;
import com.banking.bankingapplication.Service.BankAccountService;
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
    public BankAccountDto createBankAccount(@RequestParam Long customerId, @RequestParam double balance, @RequestParam String type) throws UserNotFoundException {

        return bankAccountService.createBankAccount(customerId,balance,type);
    }
    @GetMapping("/bankAccounts/{id}")
    public List<BankAccountDto> getBankAccountsByUserId(@PathVariable Long id){
        return bankAccountService.getBankAccountsByUserId(id);
    }

    @GetMapping("/{id}")
    public BankAccountDto getBankAccountById(@PathVariable String id) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(id);
    }

    @PostMapping("debit")
    public  void debit(@RequestParam String accountId ,@RequestParam  Double amount, @RequestParam  String description ) throws BalanceNotFoundException, BankAccountNotFoundException {


            bankAccountService.debit(accountId,amount,description);

    }


    @GetMapping("/history/{accountId}")
    public List<AccountOperationDto> accountHistory(@PathVariable String accountId ){
        return  bankAccountService.accoutntHistory(accountId);
    }

}
