package com.banking.bankingapplication;

import com.banking.bankingapplication.repositories.BankAccountRepository;
import com.banking.bankingapplication.repositories.CustomerRepository;
import com.banking.bankingapplication.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);


    }

}
