package com.banking.bankingapplication.service.serviceimpl;

import com.banking.bankingapplication.dtos.AccountHistoryDto;
import com.banking.bankingapplication.dtos.AccountOperationDto;
import com.banking.bankingapplication.dtos.BankAccountDto;
import com.banking.bankingapplication.entities.*;
import com.banking.bankingapplication.enums.AccountStatus;
import com.banking.bankingapplication.enums.OperationType;
import com.banking.bankingapplication.exceptions.InsufisantSoldeException;
import com.banking.bankingapplication.exceptions.BankAccountNotFoundException;
import com.banking.bankingapplication.exceptions.UserNotFoundException;
import com.banking.bankingapplication.mappers.BankingMapper;
import com.banking.bankingapplication.repositories.AccountOperationRepository;
import com.banking.bankingapplication.repositories.BankAccountRepository;
import com.banking.bankingapplication.repositories.UserRepository;
import com.banking.bankingapplication.service.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private  BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private UserRepository customerRepository;
    private BankingMapper bankingMapper;
    @Override
    public BankAccountDto createBankAccount(Long customerId, double balance, String type) throws UserNotFoundException {

        Users user = customerRepository.findById(customerId).orElseThrow(()->new UserNotFoundException("user not found"));
        BankAccount bankAccount;
        if(type.equals("CURR")){
            CurrentAccount currentAccount = new CurrentAccount();
            currentAccount.setId(UUID.randomUUID().toString());
            currentAccount.setCreatedAt(new Date());
            currentAccount.setOverDraft(0);
            currentAccount.setStatus(AccountStatus.CREATED);
            currentAccount.setBalnce(balance);
            currentAccount.setUser(user);
            bankAccount=currentAccount;
        }
        else {
            SavingAccount savingAccount = new SavingAccount();
            savingAccount.setId(UUID.randomUUID().toString());
            savingAccount.setCreatedAt(new Date());
            savingAccount.setIntersetRating(0);
            savingAccount.setStatus(AccountStatus.CREATED);
            savingAccount.setBalnce(balance);
            savingAccount.setUser(user);
            bankAccount=savingAccount;
        }

        return bankingMapper.fromBankAccount(bankAccountRepository.save(bankAccount)) ;
    }

    @Override
    public List<BankAccountDto> getBankAccountsByUserId(Long id)   {
        List<BankAccount> bankAccounts=bankAccountRepository.findByUserId(id).stream().map(bA->{
            if(bA instanceof CurrentAccount){
                return (CurrentAccount) bA;
            }
            else {
                return (SavingAccount)bA;
            }

        }).collect(Collectors.toList());

        return bankingMapper.fromBankAccountListToBankAccountDto(bankAccounts);
    }

    @Override
    public boolean deleteBankAccount(String id) {
        try {
            if(!bankAccountRepository.findById(id).isPresent()){
                return false;
            }else{
            bankAccountRepository.deleteById(id);
            return true;}
        }catch (Exception e){
            return false;}}

    @Override
    public BankAccountDto getBankAccount(String bankId) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(bankId).orElseThrow(()->new BankAccountNotFoundException("No account was found with this number"));

        if(bankAccount instanceof CurrentAccount){
            return bankingMapper.fromBankAccount((CurrentAccount) bankAccount);

        }else {
            return bankingMapper.fromBankAccount((SavingAccount) bankAccount);
        }
    }
    @Override
    public void debit(String accountId, double amount, String description) throws InsufisantSoldeException, BankAccountNotFoundException {
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
        } else if (((initialBalance-amount)<0)&& bankAccount instanceof CurrentAccount) {
            ((CurrentAccount) bankAccount).setOverDraft((initialBalance-amount)+((CurrentAccount) bankAccount).getOverDraft());
            debitOperation.setAmount(amount);
            bankAccount.setBalnce(0);
            bankAccountRepository.save(bankAccount);
            accountOperationRepository.save(debitOperation);
        } else if (((initialBalance-amount)<0)&&bankAccount instanceof SavingAccount)
            throw new InsufisantSoldeException("Solde inssufisant");
    }

    @Override
    public ResponseEntity<String> verifySolde(String accountId, double amount, String description) {
        BankAccountDto bankAccountDto= getBankAccount(accountId);
        BankAccount bankAccount=bankingMapper.fromBankAcountDto(bankAccountDto);
        double initialBalance= bankAccount.getBalnce();
        if(((initialBalance-amount)>=0)){
            return new ResponseEntity<>("{\"message\": \"Sufficient balance\"}", HttpStatus.OK);
        } else if (((initialBalance-amount)<0)&& bankAccount instanceof CurrentAccount) {
            return new ResponseEntity<>("{\"message\": \"Insufficient balance\"}", HttpStatus.OK);
        }return new ResponseEntity<>("{\"message\": \"You have a saving account, you cant have overdraft\"}", HttpStatus.OK);}

    @Autowired
    RestTemplate restTemplate;
    @Override
    public List<Post> getPosts() {
        return  Arrays.asList( restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts",Post[].class)).stream().map(x->{
            x.setTitle("ok");
            return x;
        }).collect(Collectors.toList());
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
        if(bankAccount instanceof CurrentAccount && ((CurrentAccount) bankAccount).getOverDraft()<0){
          double somme=amount+ ((CurrentAccount) bankAccount).getOverDraft();
            if (somme > 0) {
                bankAccount.setBalnce(somme);
                ((CurrentAccount) bankAccount).setOverDraft(0);
            } else {
                ((CurrentAccount) bankAccount).setOverDraft(somme);}
        }
        else {
            bankAccount.setBalnce(bankAccount.getBalnce()+amount);
        }
        bankAccountRepository.save(bankAccount);
        accountOperationRepository.save(creditOperation);
    }

    @Override
    public void transfer(String accountIdSource, String accoundIdDestinatin, double amount) throws BankAccountNotFoundException, InsufisantSoldeException {
        debit(accountIdSource,amount,"Trasnsfer to"+" "+accoundIdDestinatin);
        credit(accoundIdDestinatin,amount,"Trasnsfer from "+accountIdSource);
    }
    @Override
    public List<AccountOperationDto> accoutntHistory(String accountId) {
        return bankingMapper.fromAccountOperationListToAccountOperationDtoList(accountOperationRepository.findAccountOperationsByBankAccountIdOrderByOperationDateDesc(accountId));
    }
    @Override
    public AccountHistoryDto getAccountHistory(String accountId, int page , int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("now bank account found with this id"));
        Page<AccountOperations> accountOperations=accountOperationRepository.findAccountOperationsByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page,size));
        List<AccountOperationDto> operationDtos= accountOperationRepository.findAccountOperationsByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page,size)).getContent().stream()
                .map(operations -> {
                    AccountOperationDto accountOperationDto=new AccountOperationDto();
                    BeanUtils.copyProperties(operations,accountOperationDto);
                    return accountOperationDto;
                }).collect(Collectors.toList());
        AccountHistoryDto accountHistoryDto = new AccountHistoryDto();
        accountHistoryDto.setAccountOperationDtos(operationDtos);
        accountHistoryDto.setAccountId(accountId);
        accountHistoryDto.setBalance(bankAccount.getBalnce());
        accountHistoryDto.setType(bankAccount.getClass().getSimpleName());
        accountHistoryDto.setSize(size);
        accountHistoryDto.setCurrentPage(page);
        accountHistoryDto.setTotalPages(accountOperations.getTotalPages());

    return accountHistoryDto;
    }

    @Override
    public List<BankAccountDto> getBankAccounts() {
        List<BankAccount> bankAccounts=bankAccountRepository.findAll().stream().map(bA->{
            if(bA instanceof CurrentAccount){
                return (CurrentAccount) bA;
            }else {
                return (SavingAccount)bA;
            }
        }).collect(Collectors.toList());

        return bankingMapper.fromBankAccountListToBankAccountDto(bankAccounts);
    }




}

