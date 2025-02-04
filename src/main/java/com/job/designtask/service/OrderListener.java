package com.job.designtask.service;

import com.job.designtask.exception.OrderProcessingException;
import com.job.designtask.model.dto.OrderRequestDTO;
import com.job.designtask.service.email.MockEmailService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderListener {
    private static final String ORDER_REQUEST_TOPIC = "order-requests";
    private static final String ORDER_REQUEST_GROUP = "order-request-group";
    private static final String ORDER_REQUEST_CONTAINER_FACTORY = "kafkaListenerContainerFactory";
    private final OrderService orderService;
    ;
    private final MockEmailService emailService;

    @KafkaListener(
            topics = ORDER_REQUEST_TOPIC,
            groupId = ORDER_REQUEST_GROUP,
            containerFactory = ORDER_REQUEST_CONTAINER_FACTORY
    )
    public void onMessage(ConsumerRecord<String, OrderRequestDTO> record) {
        OrderRequestDTO orderRequestDto = record.value();
        if (orderRequestDto == null) {
            throw new OrderProcessingException("Received null OrderRequestDto");
        }
        orderService.processOrderUpdate(orderRequestDto);
        emailService.sendEmail(
                orderRequestDto.getReceiverEmail(),
                "Order confirmation",
                "Your order " + orderRequestDto.getShipmentNumber() + " has been received",
                orderRequestDto.getStatusCode()
        );
    }
}