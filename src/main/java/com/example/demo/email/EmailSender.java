package com.example.demo.email;

public interface EmailSender {
    void sendEmail(String to, String subject, String body);
}
