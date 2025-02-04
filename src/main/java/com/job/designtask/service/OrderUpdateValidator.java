package com.job.designtask.service;

import com.job.designtask.exception.OrderUpdateError;
import com.job.designtask.model.OrderRequest;
import com.job.designtask.model.dto.OrderRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class OrderUpdateValidator {

    private void validateReceiverEmailMatch(OrderRequest existingOrder, OrderRequestDTO orderRequestDto) {
        if (!existingOrder.getReceiverEmail().equals(orderRequestDto.getReceiverEmail())) {
            throw new OrderUpdateError("Receiver email does not match");
        }
    }

    private void validateReceiverCountryCodeMatch(OrderRequest existingOrder, OrderRequestDTO orderRequestDto) {
        if (!existingOrder.getReceiverCountryCode().equals(orderRequestDto.getReceiverCountryCode())) {
            throw new OrderUpdateError("Receiver country code does not match");
        }
    }

    private void validateSenderCountryCodeMatch(OrderRequest existingOrder, OrderRequestDTO orderRequestDto) {
        if (!existingOrder.getSenderCountryCode().equals(orderRequestDto.getSenderCountryCode())) {
            throw new OrderUpdateError("Sender country code does not match");
        }
    }

    private void validateStatusCodeIsDifferent(OrderRequest existingOrder, OrderRequestDTO orderRequestDto) {
        if (existingOrder.getStatusCode() == orderRequestDto.getStatusCode()) {
            throw new OrderUpdateError("Status code is the same");
        }
    }

    public void validateOrderUpdate(OrderRequest existingOrder, OrderRequestDTO orderRequestDto) {

        validateReceiverEmailMatch(existingOrder, orderRequestDto);
        validateReceiverCountryCodeMatch(existingOrder, orderRequestDto);
        validateSenderCountryCodeMatch(existingOrder, orderRequestDto);
        validateStatusCodeIsDifferent(existingOrder, orderRequestDto);
    }

}

