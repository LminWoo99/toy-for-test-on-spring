package com.example.demo.user.service;

public interface MailSender {
    void send(String email, String title, String content);
}
