package com.banking.bankingapplication.listner;

import com.banking.bankingapplication.service.serviceimpl.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationFailureListener {
    @Autowired
    LoginAttemptService loginAttemptService;

@EventListener
    public void onAuthenticationFail(AuthenticationFailureBadCredentialsEvent event){
        Object principal= event.getAuthentication().getPrincipal();
        if(principal instanceof  String){
            String userName= (String) principal;
            loginAttemptService.addUserToLoginAttemptCache(userName);

        }

    }
}
