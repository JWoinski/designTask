package com.job.designcodingtask.service.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MockEmailService extends EmailService {
    @Override
    public void sendEmail(String to, String subject, String body) {
        log.info("Sending email to {}", to);
    }
}
