package com.banking.bankingapplication;

import com.banking.bankingapplication.dtos.AccountHistoryDto;
import com.banking.bankingapplication.dtos.BankAccountDto;
import com.banking.bankingapplication.dtos.CurrentAcountDto;
import com.banking.bankingapplication.entities.AccountOperations;
import com.banking.bankingapplication.entities.BankAccount;
import com.banking.bankingapplication.enums.OperationType;
import com.banking.bankingapplication.exceptions.InsufisantSoldeException;
import com.banking.bankingapplication.repositories.AccountOperationRepository;
import com.banking.bankingapplication.service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        assertEquals(2L,bankAccountService.getBankAccountsByUserId(2L).get(0).getUser().getId());
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
        assertThrows(InsufisantSoldeException.class, () -> bankAccountService.debit("24aa50b7-af9a-4245-a658-f0ba2ac25038", 1500.0, "Test debit"));

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
        assertEquals(10,accountHistoryDto.getAccountOperationDtos().get(0).getAmount());


    }


    @Test
     void addCurrentAccount(){
        bankAccountService.createBankAccount(1L,80,"CURR");
        List<BankAccountDto> accounts =bankAccountService.getBankAccounts();
        assertEquals(7,accounts.size());

    }

    @Test
     void addSaveAccount(){
        BankAccountDto newAccount=bankAccountService.createBankAccount(1L,80,"SAVE");
        List<BankAccountDto> accounts =bankAccountService.getBankAccounts();
        assertEquals(7,accounts.size());
        assertEquals(80,newAccount.getBalnce());    }

@Test
    void verifySoldWhenSufficientBalance(){

    assertEquals(new ResponseEntity<>("{\"message\": \"Sufficient balance\"}", HttpStatus.OK),bankAccountService.verifySolde("24aa50b7-af9a-4245-a658-f0ba2ac25038", 100, "test")) ;
}

    @Test
    void verifySoldWhenInsufficientBalanceAndCurrentAccount(){

        assertEquals(new ResponseEntity<>("{\"message\": \"Insufficient balance\"}", HttpStatus.OK),bankAccountService.verifySolde("24aa50b7-af9a-4245-a658-f0ba2ac25037", 500, "test")) ;
    }



    @Test
    void verifySoldWhenInsufficientBalanceAndSavingAccount(){

        assertEquals(new ResponseEntity<>("{\"message\": \"You have a saving account, you cant have overdraft\"}", HttpStatus.OK),bankAccountService.verifySolde("24aa50b7-af9a-4245-a658-f0ba2ac25038", 4000, "test")) ;
    }


}

