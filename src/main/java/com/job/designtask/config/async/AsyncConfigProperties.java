package com.job.designtask.config.async;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.async.thread-pool")
public class AsyncConfigProperties {

    private int coreSize;
    private int maxSize;
    private int queueCapacity;
    private String threadNamePrefix;
    private boolean waitForTasks;
}