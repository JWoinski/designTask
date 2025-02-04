package com.job.designtask.controller;

import com.job.designtask.model.dto.OrderRequestDTO;
import com.job.designtask.service.OrderMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderMessageService orderMessageService;

    @PostMapping
    public String handleOrder(@Valid @RequestBody OrderRequestDTO orderRequest) {
        orderMessageService.processOrder(orderRequest);
        return "Order received and processed.";
    }
}