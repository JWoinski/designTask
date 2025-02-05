package com.job.designtask.model;

import com.job.designtask.model.dto.OrderRequestDTO;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ConverterDTO implements Converter<OrderRequestDTO, OrderRequest> {
    @Override
    public OrderRequest convert(MappingContext<OrderRequestDTO, OrderRequest> mappingContext) {
        OrderRequestDTO command = mappingContext.getSource();
        return OrderRequest.builder()
                .shipmentNumber(command.getShipmentNumber())
                .receiverEmail(command.getReceiverEmail())
                .receiverCountryCode(command.getReceiverCountryCode())
                .senderCountryCode(command.getSenderCountryCode())
                .statusCode(command.getStatusCode())
                .receivedAt(LocalDateTime.now())
                .build();
    }
}

