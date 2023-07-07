package com.banking.bankingapplication.exceptions;

public class PasswordDoNotMatcheException extends Exception{
    public PasswordDoNotMatcheException(String message) {
        super(message);
    }
}
