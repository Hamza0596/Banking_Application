package com.banking.bankingapplication.ControllersTests;

import com.banking.bankingapplication.controllers.BankAccountController;
import com.banking.bankingapplication.dtos.AccountHistoryDto;
import com.banking.bankingapplication.dtos.AccountOperationDto;
import com.banking.bankingapplication.dtos.BankAccountDto;
import com.banking.bankingapplication.mappers.BankingMapper;
import com.banking.bankingapplication.service.BankAccountService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(BankAccountController.class)
 class BankAccountControllerTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    BankAccountService bankAccountService;

    @MockBean
    BankingMapper bankingMapper;

    @Test
    void getBankAccountsByUserId() throws Exception {
        Long userId =1L;
        BankAccountDto bankAccountDto1= new BankAccountDto();
        BankAccountDto bankAccountDto2=new BankAccountDto();
        List<BankAccountDto> bankAccountDtos=new ArrayList<>();
        bankAccountDtos.add(bankAccountDto1);
        bankAccountDtos.add(bankAccountDto2);
        when(bankAccountService.getBankAccountsByUserId(userId)).thenReturn(bankAccountDtos);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/bankaccount/bankaccounts/users/{userId}",userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(bankAccountDtos.size()));


    }

    @Test
    void getBankAccountById() throws Exception {
        String bankAccountId = "hello";
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setId(bankAccountId);
        when(bankAccountService.getBankAccount(bankAccountDto.getId())).thenReturn(bankAccountDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/bankaccount/bankAccounts/{bankAccountId}", bankAccountId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bankAccountId));
    }

    @Test
    void debit() throws Exception {
        String accountId = "123";
        Double amount = 50.0;
        String description = "Test debit";

        // Configurer le comportement du mock de service
        doNothing().when(bankAccountService).debit(accountId, amount, description);

        // Effectuer une requête POST à l'endpoint "/debit"
        mockMvc.perform(MockMvcRequestBuilders.post("/api/bankaccount/debit")
                        .param("accountId", accountId)
                        .param("amount", amount.toString())
                        .param("description", description))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Vérifier que la méthode "debit" du service a été appelée avec les bons arguments
        verify(bankAccountService, times(1)).debit(accountId, amount, description);
    }

    @Test
    void credit() throws Exception {
        String accountId = "123";
        Double amount = 50.0;
        String description = "Test credit";

        // Configurer le comportement du mock de service
        doNothing().when(bankAccountService).credit(accountId, amount, description);

        // Effectuer une requête POST à l'endpoint "/debit"
        mockMvc.perform(MockMvcRequestBuilders.post("/api/bankaccount/credit")
                        .param("accountId", accountId)
                        .param("amount", amount.toString())
                        .param("description", description))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Vérifier que la méthode "debit" du service a été appelée avec les bons arguments
        verify(bankAccountService, times(1)).credit(accountId, amount, description);
    }




    @Test
    void transfer() throws Exception {
        String accountId1 = "123";
        Double amount = 50.0;
        String accountId2 = "T345";

        // Configurer le comportement du mock de service
        doNothing().when(bankAccountService).transfer(accountId1,accountId2,amount);

        // Effectuer une requête POST à l'endpoint "/debit"
        mockMvc.perform(MockMvcRequestBuilders.post("/api/bankaccount/transfert")
                        .param("accountIdSource", accountId1)
                        .param("accoundIdDestinatin", accountId2)
                        .param("amount", amount.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Vérifier que la méthode "debit" du service a été appelée avec les bons arguments
        verify(bankAccountService, times(1)).transfer(accountId1,accountId2,amount);
    }

    @Test
    void AccountHistory() throws Exception {
        String accountId="1234";
        AccountOperationDto accountOperationDto1 = new AccountOperationDto();
        AccountOperationDto accountOperationDto2=new AccountOperationDto();
        List<AccountOperationDto> accountOperationDtos= new ArrayList<>();
        accountOperationDtos.add(accountOperationDto1);
        accountOperationDtos.add(accountOperationDto2);
        when(bankAccountService.accoutntHistory(accountId)).thenReturn(accountOperationDtos);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/bankaccount/history/{accountId}",accountId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(accountOperationDtos.size()));

    }

    @Test
    void getAccountHistory() throws Exception {
        AccountHistoryDto accountHistoryDto = new AccountHistoryDto();
        String accountId="123";
        Integer page=1;
        Integer size=3;
        when(bankAccountService.getAccountHistory(accountId,page,size)).thenReturn(accountHistoryDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/bankaccount/accounts/{accountId}/pageoperations",accountId)
                .param("page",page.toString())
                .param("size",size.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    void deleteAccountById() throws Exception {
        String id="123";
        when(bankAccountService.deleteBankAccount(id)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/bankaccount/delete/{id}",id))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }



}
