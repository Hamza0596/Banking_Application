package com.banking.bankingapplication;

import com.banking.bankingapplication.Entities.CurrentAccount;
import com.banking.bankingapplication.Entities.Customer;
import com.banking.bankingapplication.Entities.SavingAccount;
import com.banking.bankingapplication.Enum.AccountStatus;
import com.banking.bankingapplication.Exceptions.UserNotFoundException;
import com.banking.bankingapplication.Repositories.BankAccountRepository;
import com.banking.bankingapplication.Repositories.CustomerRepository;
import com.banking.bankingapplication.Service.BankAccountService;
import com.banking.bankingapplication.Service.CustomerService;
import com.banking.bankingapplication.Service.ServiceImpl.BankAccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class BankingApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }
    @Autowired
    CustomerRepository customerRepositorys;
    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    BankAccountService bankAccountService;
    @Autowired
    com.banking.bankingapplication.Service.CustomerService customerService;
    @Override
    public void run(String... args) throws Exception {

        bankAccountService.debit("94eca887-3815-4825-a1b0-1e0f6c55e1d8",200,"vvv");

    }
}
