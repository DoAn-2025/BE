package com.doan2025.webtoeic.service;

public interface EmailService {
    void sendEmail(String toEmail, String subject, String body);
}
