package com.job.designtask.service.email;

public interface EmailService {
    void sendEmail(String to, String subject, String body, int code);
}