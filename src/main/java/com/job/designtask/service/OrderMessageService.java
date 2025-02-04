package com.job.designtask.service;

import com.job.designtask.model.dto.OrderRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderMessageService {
    private static final String ORDER_TOPIC = "order-requests";
    private final KafkaTemplate<String, OrderRequestDTO> kafkaTemplate;

    @Async("asyncTaskExecutor")
    public void processOrder(OrderRequestDTO orderRequest) {
        kafkaTemplate.send(ORDER_TOPIC, orderRequest);
    }
}