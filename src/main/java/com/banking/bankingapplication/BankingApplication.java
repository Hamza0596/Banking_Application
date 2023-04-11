package com.banking.bankingapplication;

import com.banking.bankingapplication.repositories.BankAccountRepository;
import com.banking.bankingapplication.repositories.CustomerRepository;
import com.banking.bankingapplication.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
    com.banking.bankingapplication.service.CustomerService customerService;
    @Override
    public void run(String... args) throws Exception {

        bankAccountService.debit("94eca887-3815-4825-a1b0-1e0f6c55e1d8",200,"vvv");

    }
}
