package com.banking.bankingapplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class GloblaExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handelUserNotFoundException(UserNotFoundException exception  ){
        ErrorDetails  errorDetails = new ErrorDetails( new Date(), exception.getMessage(),"utilisateur non trouvé avec la reference fournis");
        return  new ResponseEntity<Object>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
