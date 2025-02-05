package com.job.designtask.service;

import com.job.designtask.model.OrderRequest;
import com.job.designtask.model.dto.OrderRequestDTO;
import com.job.designtask.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public void saveOrderLog(OrderRequestDTO orderRequestDto) {
        if(orderRequestDto != null) {
            orderRepository.save(modelMapper.map(orderRequestDto,OrderRequest.class));
        }
    }
}