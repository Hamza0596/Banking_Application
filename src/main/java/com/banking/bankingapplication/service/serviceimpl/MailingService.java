package com.banking.bankingapplication.service.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;


@Service
public class MailingService {
    @Autowired
    JavaMailSender javaMailSender;

    public void sendEmail(String emailAdresse, String body ,String Subject){
        System.out.println(emailAdresse + body+Subject);
        SimpleMailMessage ConfirmationMessage = new SimpleMailMessage();
        ConfirmationMessage.setTo(emailAdresse);
        ConfirmationMessage.setText(body);
        ConfirmationMessage.setSubject(Subject);
        javaMailSender.send(ConfirmationMessage);
    }


}
