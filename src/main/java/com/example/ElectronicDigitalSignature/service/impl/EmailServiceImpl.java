package com.example.ElectronicDigitalSignature.service.impl;

import com.example.ElectronicDigitalSignature.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender emailSender;

    @Override
    public void sendEmail(String password, String email, String code) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "utf-8");
            message.setContent("Code: " + code  + ", Password: " + password + " , http://localhost:8080/download-key" , "text/html");
            helper.setTo(email);
            helper.setSubject("Жаңа аккаунт құрылды");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        emailSender.send(message);
    }
}
