package com.banking.bankingapplication;

import com.banking.bankingapplication.dtos.BankAccountDto;
import com.banking.bankingapplication.entities.AccountOperations;
import com.banking.bankingapplication.enums.OperationType;
import com.banking.bankingapplication.exceptions.BalanceNotFoundException;
import com.banking.bankingapplication.repositories.AccountOperationRepository;
import com.banking.bankingapplication.repositories.BankAccountRepository;
import com.banking.bankingapplication.service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@Sql(scripts="/Customer_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts="/BankAccount_data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts="/Delete_Account_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts="/Delete_Customer_data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class AccountsServiceTests {
    @Autowired
    BankAccountService bankAccountService;
    @Autowired
    AccountOperationRepository accountOperationRepository;


    @Test

    public void getAccountByUserId(){
        assertEquals(bankAccountService.getBankAccountsByUserId(2L).get(0).getCustomer().getId(),2L);
    }



    @Test

    public void getBankAccountByid(){
        assertEquals(bankAccountService.getBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25036").getBalnce(),200);

    }

    @Test

    public void deleteAccout(){
        assertTrue(bankAccountService.deleteBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25035"));
    }


    @Test
    public void debitWithsufisantBalance(){
        bankAccountService.debit("24aa50b7-af9a-4245-a658-f0ba2ac25038",100,"debit test");
        assertEquals(bankAccountService.getBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25038").getBalnce(),100);
        List<AccountOperations> operations = accountOperationRepository.findAll();
        assertEquals(operations.size(),1);
        AccountOperations accountOperations=operations.get(0);
        assertEquals(accountOperations.getAmount(),100);
        assertEquals(accountOperations.getDescription(),"debit test");
        assertEquals(accountOperations.getType(), OperationType.DEBIT);


    }

    @Test
    public void debitWithInsufisantBalanceAndSavingAccount(){
        assertThrows(BalanceNotFoundException.class, () -> bankAccountService.debit("24aa50b7-af9a-4245-a658-f0ba2ac25038", 1500.0, "Test debit"));

    }





}

