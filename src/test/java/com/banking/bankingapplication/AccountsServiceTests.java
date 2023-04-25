package com.banking.bankingapplication;

import com.banking.bankingapplication.dtos.AccountHistoryDto;
import com.banking.bankingapplication.dtos.AccountOperationDto;
import com.banking.bankingapplication.dtos.BankAccountDto;
import com.banking.bankingapplication.dtos.CurrentAcountDto;
import com.banking.bankingapplication.entities.AccountOperations;
import com.banking.bankingapplication.entities.BankAccount;
import com.banking.bankingapplication.entities.CurrentAccount;
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

 class AccountsServiceTests {
    @Autowired
    BankAccountService bankAccountService;
    @Autowired
    AccountOperationRepository accountOperationRepository;


    @Test

     void getCurrentAccountByUserId(){
        assertEquals(2L,bankAccountService.getBankAccountsByUserId(2L).get(0).getCustomer().getId());
    }


    @Test

     void getSavingAccountByUserId(){
        assertEquals("SavingAccount",bankAccountService.getBankAccountsByUserId(1L).get(1).getType());
    }


    @Test

     void getBankAccountById(){
        assertEquals(200,bankAccountService.getBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25036").getBalnce());

    }

    @Test

     void deleteAccout(){
        assertTrue(bankAccountService.deleteBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25035"));

    }
    @Test
     void deleteInexistingAccout(){
        assertFalse(bankAccountService.deleteBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25035iy"));
    }


    @Test
    void debitWithsufisantBalance(){
        bankAccountService.debit("24aa50b7-af9a-4245-a658-f0ba2ac25038",100,"debit test");
        assertEquals(100,bankAccountService.getBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25038").getBalnce());
        List<AccountOperations> operations = accountOperationRepository.findAll();
        assertEquals(1,operations.size());
        AccountOperations accountOperations=operations.get(0);
        assertEquals(100,accountOperations.getAmount());
        assertEquals("debit test",accountOperations.getDescription());
        assertEquals(OperationType.DEBIT,accountOperations.getType());
    }

    @Test
     void debitWithInsufisantBalceAndCurentAccount(){

        bankAccountService.debit("24aa50b7-af9a-4245-a658-f0ba2ac25037",300,"debit test insufisante balance and currentAccount");
        CurrentAcountDto currentAccountDto= (CurrentAcountDto) bankAccountService.getBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25037");
        assertEquals(currentAccountDto.getOverDraft(),-100);
    }

    @Test
     void debitWithInsufisantBalanceAndSavingAccount(){
        assertThrows(BalanceNotFoundException.class, () -> bankAccountService.debit("24aa50b7-af9a-4245-a658-f0ba2ac25038", 1500.0, "Test debit"));

    }

    @Test
     void creditCurrentAccountWithOutOverDreaft(){
        bankAccountService.credit("24aa50b7-af9a-4245-a658-f0ba2ac25037",300,"credit test");
        assertEquals(500,bankAccountService.getBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25037").getBalnce());

    }

    @Test
     void creditCurrentAccountWithNegativeOverDreaft(){
        bankAccountService.credit(bankAccountService.getBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25034").getId(),100,"credit test");
        CurrentAcountDto bankAccountDto = (CurrentAcountDto)( bankAccountService.getBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25034"));

        assertEquals(0,bankAccountDto.getBalnce());
        assertEquals(bankAccountDto.getOverDraft(),-100);

    }

    @Test
     void creditCurrentAccountWithNegativeOverDraftAndAmmoutsupDraft(){
        bankAccountService.credit(bankAccountService.getBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25034").getId(),500,"credit test");
        CurrentAcountDto bankAccountDto = (CurrentAcountDto)( bankAccountService.getBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25034"));

        assertEquals(300,bankAccountDto.getBalnce());
        assertEquals(0,bankAccountDto.getOverDraft());

    }

    @Test
     void transfer(){
        bankAccountService.transfer("24aa50b7-af9a-4245-a658-f0ba2ac25038","24aa50b7-af9a-4245-a658-f0ba2ac25037",200);
        assertEquals(0,bankAccountService.getBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25038").getBalnce());
        assertEquals(400,bankAccountService.getBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25037").getBalnce());

    }

    @Test
     void accountHistory(){
        bankAccountService.credit(bankAccountService.getBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25034").getId(),500,"credit test");
        OperationType type= bankAccountService.accoutntHistory("24aa50b7-af9a-4245-a658-f0ba2ac25034").get(0).getType();
        assertEquals(OperationType.CREDIT,type);

    }

    @Test
     void getAccountHistory(){
        bankAccountService.credit(bankAccountService.getBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25037").getId(),500,"credit test");
        bankAccountService.debit("24aa50b7-af9a-4245-a658-f0ba2ac25037",10,"debit test insufisante balance and currentAccount");
        AccountHistoryDto accountHistoryDto=bankAccountService.getAccountHistory("24aa50b7-af9a-4245-a658-f0ba2ac25037",0,2);
        assertEquals( 690,bankAccountService.getBankAccount("24aa50b7-af9a-4245-a658-f0ba2ac25037").getBalnce());
        assertEquals(690,accountHistoryDto.getBalance());
        assertEquals(500,accountHistoryDto.getAccountOperationDtos().get(0).getAmount());


    }


    @Test
     void addCurrentAccount(){
        bankAccountService.createBankAccount(1L,80,"CURR");
        assertEquals(80,bankAccountService.createBankAccount(1L,80,"CURR").getBalnce());    }

    @Test
     void addSaveAccount(){
        bankAccountService.createBankAccount(1L,80,"SAVE");
        assertEquals(80,bankAccountService.createBankAccount(1L,80,"CURR").getBalnce());    }




}

