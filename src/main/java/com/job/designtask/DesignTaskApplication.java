package com.job.designtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DesignTaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(DesignTaskApplication.class, args);
    }
}