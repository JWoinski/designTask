package com.job.designtask.service;

import com.job.designtask.exception.OrderProcessingException;
import com.job.designtask.exception.OrderUpdateError;
import com.job.designtask.model.OrderRequest;
import com.job.designtask.model.dto.OrderRequestDTO;
import com.job.designtask.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderUpdateValidator orderUpdateValidator;
    @Transactional
    public void processOrderUpdate(OrderRequestDTO orderRequestDto) {
        Optional<OrderRequest> existingOrder = orderRepository.findByShipmentNumber(orderRequestDto.getShipmentNumber());
        existingOrder.ifPresentOrElse(
                order -> updateExistingOrder(order, orderRequestDto),
                () -> createNewOrder(orderRequestDto)
        );
    }

    private void updateExistingOrder(OrderRequest order, OrderRequestDTO orderRequestDto) {
            //TODO jebie sie walidacja
            orderUpdateValidator.validateOrderUpdate(order, orderRequestDto);
            order.setStatusCode(orderRequestDto.getStatusCode());
            orderRepository.save(order);
    }

    private void createNewOrder(OrderRequestDTO orderRequestDto) {
        OrderRequest order = OrderRequest.builder()
                .shipmentNumber(orderRequestDto.getShipmentNumber())
                .receiverEmail(orderRequestDto.getReceiverEmail())
                .receiverCountryCode(orderRequestDto.getReceiverCountryCode())
                .senderCountryCode(orderRequestDto.getSenderCountryCode())
                .statusCode(orderRequestDto.getStatusCode())
                .build();
        orderRepository.save(order);
        System.out.println("Nowe zamówienie zostało utworzone: " + orderRequestDto.getShipmentNumber());
    }
}