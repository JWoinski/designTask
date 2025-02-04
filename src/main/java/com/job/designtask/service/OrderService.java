package com.job.designcodingtask.service;

import com.job.designcodingtask.model.OrderRequest;
import com.job.designcodingtask.model.dto.OrderRequestDto;
import com.job.designcodingtask.repository.OrderRepository;
import com.job.designcodingtask.service.email.MockEmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {
    private final KafkaTemplate<String, OrderRequestDto> kafkaTemplate;
    private final OrderRepository orderRepository;
    private final MockEmailService mockEmailService;

    private static final String ORDER_TOPIC = "order-requests";

    public void processOrder(OrderRequestDto orderRequest) {
        kafkaTemplate.send(ORDER_TOPIC, orderRequest);
        System.out.println("Order processed: " + orderRequest);
        System.out.println(kafkaTemplate.send(ORDER_TOPIC, orderRequest));
    }
    public void saveOrder(OrderRequestDto orderRequestDto) {
        OrderRequest orderRequest = OrderRequest.builder()
                .shipmentNumber(orderRequestDto.getShipmentNumber())
                .receiverEmail(orderRequestDto.getReceiverEmail())
                .receiverCountryCode(orderRequestDto.getReceiverCountryCode())
                .senderCountryCode(orderRequestDto.getSenderCountryCode())
                .statusCode(orderRequestDto.getStatusCode())
                .build();
        orderRepository.save(orderRequest);
    }

    public void sendEmailAsync(String to, String subject, String body) {
        mockEmailService.sendEmail(to, subject, body);
    }

}
