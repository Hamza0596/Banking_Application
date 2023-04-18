package com.banking.bankingapplication.exceptions;

public class BalanceNotFoundException extends RuntimeException{
    public  BalanceNotFoundException(String message){
        super(message);
    }
}
