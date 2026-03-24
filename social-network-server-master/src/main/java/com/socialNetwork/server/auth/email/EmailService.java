package com.socialNetwork.server.auth.email;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRegisterCodeEmail(String email, String code) {
        String subject = "Registration verification code";
        String text = buildRegisterEmailText(code);
        sendEmail(email, subject, text);
    }

    public void sendLoginCodeEmail(String email, String code) {
        String subject = "Login verification code";
        String text = buildLoginEmailText(code);
        sendEmail(email, subject, text);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    private String buildRegisterEmailText(String code) {
        return "Your registration code is: " + code +
                "\nThis code will expire in 5 minutes.";
    }

    private String buildLoginEmailText(String code) {
        return "Your login code is: " + code +
                "\nThis code will expire in 5 minutes.";
    }
}