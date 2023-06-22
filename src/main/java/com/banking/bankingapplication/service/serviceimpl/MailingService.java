package com.banking.bankingapplication.service.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;


@Service
public class MailingService {
    @Autowired
    JavaMailSender javaMailSender;

    public void sendEmail(String emailAdresse, String body ,String subject){
        SimpleMailMessage confirmationMessage = new SimpleMailMessage();
        confirmationMessage.setTo(emailAdresse);
        confirmationMessage.setText(body);
        confirmationMessage.setSubject(subject);
        javaMailSender.send(confirmationMessage);
    }


}
