package com.job.designtask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.designtask.model.OrderRequest;
import com.job.designtask.model.dto.OrderRequestDTO;
import com.job.designtask.repository.OrderRepository;
import com.job.designtask.service.OrderService;
import com.job.designtask.service.OrderUpdateValidator;
import liquibase.exception.LiquibaseException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class OrderServiceTest{
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderUpdateValidator orderUpdateValidator;

    @InjectMocks
    private OrderService orderService;

    private OrderRequestDTO orderRequestDto;
    private OrderRequest existingOrder;

    @BeforeEach
    void setUp() {
        orderRequestDto = OrderRequestDTO.builder()
                .shipmentNumber("12345")
                .receiverEmail("test@example.com")
                .receiverCountryCode("PL")
                .senderCountryCode("DE")
                .statusCode(50)
                .build();

        existingOrder = OrderRequest.builder()
                .shipmentNumber("12345")
                .receiverEmail("test@example.com")
                .receiverCountryCode("PL")
                .senderCountryCode("DE")
                .statusCode(100)
                .build();
    }

    @Test
    void shouldUpdateExistingOrderWhenShipmentNumberExists() {
        when(orderRepository.findByShipmentNumber(orderRequestDto.getShipmentNumber())).thenReturn(Optional.of(existingOrder));

        orderService.processOrderUpdate(orderRequestDto);

        verify(orderUpdateValidator, times(1)).validateOrderUpdate(existingOrder, orderRequestDto);
        verify(orderRepository, times(1)).save(existingOrder);
        assertEquals(50, existingOrder.getStatusCode());
    }

    @Test
    void shouldCreateNewOrderWhenShipmentNumberDoesNotExist() {
        when(orderRepository.findByShipmentNumber(orderRequestDto.getShipmentNumber())).thenReturn(Optional.empty());

        orderService.processOrderUpdate(orderRequestDto);

        verify(orderRepository, times(1)).save(any(OrderRequest.class));
    }

//    @Test
//    void testProcessOrderUpdate_CreatesNewOrder() {
//        OrderRequestDTO dto = new OrderRequestDTO("SHIP123", "test@example.com", "US", "UK", 0);
//        when(orderRepository.findByShipmentNumber("SHIP123")).thenReturn(Optional.empty());
//
//        orderService.processOrderUpdate(dto);
//
//        verify(orderRepository, times(1)).save(any(OrderRequest.class));
//    }
//
//    @Test
//    void testProcessOrderUpdate_UpdatesExistingOrder() {
//        OrderRequest existingOrder = new OrderRequest();
//        existingOrder.setShipmentNumber("SHIP123");
//        existingOrder.setStatusCode(0);
//
//        OrderRequestDTO dto = new OrderRequestDTO("SHIP123", "test@example.com", "US", "UK", 10);
//        when(orderRepository.findByShipmentNumber("SHIP123")).thenReturn(Optional.of(existingOrder));
//
//        orderService.processOrderUpdate(dto);
//
//        verify(orderRepository, times(1)).save(existingOrder);
//    }
}

