package com.job.designtask.service;

import com.job.designtask.exception.OrderProcessingException;
import com.job.designtask.model.dto.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderListenerService{
    private final OrderService orderService;
    private static final String ORDER_REQUEST_TOPIC = "order-requests";
    private static final String ORDER_REQUEST_GROUP = "order-request-group";
    private static final String ORDER_REQUEST_CONTAINER_FACTORY = "kafkaListenerContainerFactory";;

    @KafkaListener(
            topics = ORDER_REQUEST_TOPIC,
            groupId = ORDER_REQUEST_GROUP,
            containerFactory = ORDER_REQUEST_CONTAINER_FACTORY
    )
    public void onMessage(ConsumerRecord<String, OrderRequestDto> record) {
        try {
            OrderRequestDto orderRequestDto = record.value();
            if (orderRequestDto == null) {
                throw new OrderProcessingException("Received null OrderRequestDto");
            }

            orderService.saveOrder(orderRequestDto);
            orderService.sendEmailAsync(
                    orderRequestDto.getReceiverEmail(),
                    "Order confirmation",
                    "Your order " + orderRequestDto.getShipmentNumber() + " has been received"
            );
        } catch (Exception e) {
            throw new OrderProcessingException("Failed to process order");
        }
    }
}