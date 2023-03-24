package com.example.LibraryProjectWebApp.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SendEmailService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String firstName, String lastName, String bookInfo, String date){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("library.grodno@gmail.com");
        String body = String.format("Good afternoon, %s %s. Your book: %s is expired %s. Please return the book to the library!!!",
                firstName,
                lastName,
                bookInfo,
                date

        );

        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject("Overdue book");
        simpleMailMessage.setText(body);

        javaMailSender.send(simpleMailMessage);
        log.info("Email has been sent to  " + to);

    }

    public void send(String to, String subject, String message){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("library.grodno@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);

        javaMailSender.send(simpleMailMessage);
        log.info("Email has been sent to  " + to);
    }
}
