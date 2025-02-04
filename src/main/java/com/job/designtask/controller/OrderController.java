package com.job.designcodingtask.controller;

import com.job.designcodingtask.model.dto.OrderRequestDto;
import com.job.designcodingtask.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public String receiveOrder(@RequestBody OrderRequestDto orderRequest) {
        orderService.processOrder(orderRequest);
        return "Order received and processed.";
    }
}
