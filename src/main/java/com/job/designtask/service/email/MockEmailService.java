package com.job.designtask.service.email;

import org.springframework.stereotype.Service;

@Service
public class MockEmailService implements EmailService {
    @Override
    public void sendEmail(String to, String subject, String body, int code) {
        String statusDescription = getStatusDescription(code);
        System.out.println("Email to " + to + " has been sent.");
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        System.out.println("Order status: " + statusDescription);
    }

    private String getStatusDescription(int code) {
        return switch (code) {
            case 0 -> "New order received";
            case 10 -> "Order is being prepared";
            case 20 -> "Order is being packed";
            case 50 -> "Order has been shipped";
            case 100 -> "Order has been delivered";
            default -> "Unknown status";
        };
    }
}