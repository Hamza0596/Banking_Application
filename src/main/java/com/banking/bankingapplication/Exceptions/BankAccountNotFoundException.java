package com.banking.bankingapplication.Exceptions;

public class BankAccountNotFoundException extends  Exception{
    public BankAccountNotFoundException(String message){
        super(message);
    }
}
