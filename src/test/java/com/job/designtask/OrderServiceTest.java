package com.job.designtask;

import com.job.designtask.model.OrderRequest;
import com.job.designtask.model.dto.OrderRequestDTO;
import com.job.designtask.repository.OrderRepository;
import com.job.designtask.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderService orderService;

    @Captor
    private ArgumentCaptor<OrderRequest> orderRequestCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenValidOrderRequest_thenSavesToDatabase() {
        // given
        OrderRequestDTO orderRequestDto = OrderRequestDTO.builder()
                .shipmentNumber("SHIP123")
                .receiverEmail("test@example.com")
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(1)
                .build();

        OrderRequest expectedOrder = OrderRequest.builder()
                .shipmentNumber("SHIP123")
                .receiverEmail("test@example.com")
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(1)
                .build();

        when(modelMapper.map(orderRequestDto, OrderRequest.class)).thenReturn(expectedOrder);

        // when
        orderService.saveOrderLog(orderRequestDto);

        // then
        verify(orderRepository, times(1)).save(orderRequestCaptor.capture());
        OrderRequest savedOrder = orderRequestCaptor.getValue();

        // Porównanie wyników
        assertEquals(expectedOrder.getShipmentNumber(), savedOrder.getShipmentNumber());
        assertEquals(expectedOrder.getReceiverEmail(), savedOrder.getReceiverEmail());
        assertEquals(expectedOrder.getReceiverCountryCode(), savedOrder.getReceiverCountryCode());
        assertEquals(expectedOrder.getSenderCountryCode(), savedOrder.getSenderCountryCode());
        assertEquals(expectedOrder.getStatusCode(), savedOrder.getStatusCode());
    }

    @Test
    void whenNullOrderRequest_thenDoesNotSaveToDatabase() {
        // given
        OrderRequestDTO orderRequestDto = null;

        // when
        orderService.saveOrderLog(orderRequestDto);

        // then
        verify(orderRepository, times(0)).save(any(OrderRequest.class));
    }
}
