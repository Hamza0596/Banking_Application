package com.banking.bankingapplication.exceptions;

public class BankAccountNotFoundException extends  RuntimeException{
    public BankAccountNotFoundException(String message){
        super(message);
    }
}
