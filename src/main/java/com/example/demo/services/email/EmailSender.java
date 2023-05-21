package com.example.demo.services.email;

public interface EmailSender {
    void sendEmail(String to, String subject, String body);
}
