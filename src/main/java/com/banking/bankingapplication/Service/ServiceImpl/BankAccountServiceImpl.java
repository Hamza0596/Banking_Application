package com.banking.bankingapplication.Service.ServiceImpl;

import com.banking.bankingapplication.Dtos.BankAccountDto;
import com.banking.bankingapplication.Entities.*;
import com.banking.bankingapplication.Enum.AccountStatus;
import com.banking.bankingapplication.Enum.OperationType;
import com.banking.bankingapplication.Exceptions.BankAccountNotFoundException;
import com.banking.bankingapplication.Exceptions.UserNotFoundException;
import com.banking.bankingapplication.Mappers.BankingMapper;
import com.banking.bankingapplication.Repositories.AccountOperationRepository;
import com.banking.bankingapplication.Repositories.BankAccountRepository;
import com.banking.bankingapplication.Repositories.CustomerRepository;
import com.banking.bankingapplication.Service.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private  BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private   CustomerRepository customerRepository;
    private BankingMapper bankingMapper;
    @Override
    public BankAccountDto createBankAccount(Long customerId, double balance, String type) throws UserNotFoundException {
        log.info("hello"+customerId);

        Customer customer = customerRepository.findById(customerId).orElseThrow(()->new UserNotFoundException("user not found"));

        BankAccount bankAccount = new BankAccount();
        if(type=="CURR"){
            CurrentAccount currentAccount = new CurrentAccount();
            currentAccount.setId(UUID.randomUUID().toString());
            currentAccount.setCreatedAt(new Date());
            currentAccount.setOverDraft(0);
            currentAccount.setStatus(AccountStatus.CREATED);
            currentAccount.setBalnce(balance);
            currentAccount.setCustomer(customer);
            bankAccount=currentAccount;
        } if (type=="SAVE") {
            SavingAccount savingAccount = new SavingAccount();
            savingAccount.setId(UUID.randomUUID().toString());
            savingAccount.setCreatedAt(new Date());
            savingAccount.setIntersetRating(0);
            savingAccount.setStatus(AccountStatus.CREATED);
            savingAccount.setBalnce(balance);
            savingAccount.setCustomer(customer);
            bankAccount= savingAccount;
        }
        return bankingMapper.fromBankAcount(bankAccountRepository.save(bankAccount)) ;
    }

    @Override
    public List<BankAccountDto> getBankAccountsByUserId(Long id) throws UserNotFoundException {
            List<BankAccount> bankAccounts=bankAccountRepository.findByCustomerId(id);
            if(bankAccounts.isEmpty()){
                throw new UserNotFoundException("waaaaaaaaaaaaaa");
            }
        return  bankingMapper.fromBankAccountListToBankAccountDto(bankAccounts) ;
    }

    @Override
    public void deleteBankAccount(String id) {
        bankAccountRepository.deleteById(id);

    }

    @Override
    public BankAccountDto getBankAccount(String bankId) throws BankAccountNotFoundException {
        return  bankingMapper.fromBankAcount(bankAccountRepository.findById(bankId).orElseThrow(()->new BankAccountNotFoundException("No account was found with this number")));
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccountDto bankAccountDto= getBankAccount(accountId);
        BankAccount bankAccount=bankingMapper.fromBankAcountDto(bankAccountDto);
        AccountOperations  debitOperation= new AccountOperations();
        debitOperation.setBankAccount(bankAccount);
        debitOperation.setOperationDate(new Date());
        debitOperation.setType(OperationType.DEBIT);
        debitOperation.setDescription(description);
        double initialBalance= bankAccount.getBalnce();
        if(((initialBalance-amount)>=0)){
            debitOperation.setAmount(amount);
            bankAccount.setBalnce(bankAccount.getBalnce()-amount);
            bankAccountRepository.save(bankAccount);
            accountOperationRepository.save(debitOperation);
        } else if (((initialBalance-amount)<0)&&bankAccount instanceof CurrentAccount) {
            ((CurrentAccount) bankAccount).setOverDraft(Math.abs(initialBalance-amount));
            debitOperation.setAmount(amount);
            bankAccountRepository.save(bankAccount);
            accountOperationRepository.save(debitOperation);

        } else if (((initialBalance-amount)<0)&&bankAccount instanceof SavingAccount)
            throw new BankAccountNotFoundException("Solde inssufisant");

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {

        BankAccountDto bankAccountDto= getBankAccount(accountId);
        BankAccount bankAccount=bankingMapper.fromBankAcountDto(bankAccountDto);
        AccountOperations creditOperation = new AccountOperations();
        creditOperation.setBankAccount(bankAccount);
        creditOperation.setOperationDate(new Date());
        creditOperation.setType(OperationType.CREDIT);
        creditOperation.setDescription(description);
        creditOperation.setAmount(amount);
        bankAccount.setBalnce(bankAccount.getBalnce()+amount);
        bankAccountRepository.save(bankAccount);
        accountOperationRepository.save(creditOperation);

    }

    @Override
    public void Trasfer(String AccountIdSource, String accoundIdDestinatin, double amount) throws BankAccountNotFoundException {
        debit(AccountIdSource,amount,"Trasnsfer to"+" "+accoundIdDestinatin);
        credit(accoundIdDestinatin,amount,"Trasnsfer from "+AccountIdSource);

    }
}
