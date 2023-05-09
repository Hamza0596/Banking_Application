package com.banking.bankingapplication;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@TestPropertySource("classpath:application-test.properties")

class BankingApplicationTests {

    @Test
    void getBankAccountById(){

    }

}
