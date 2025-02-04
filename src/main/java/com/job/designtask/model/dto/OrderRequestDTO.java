package com.job.designcodingtask.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    @NotEmpty(message = "Shipment number is required")
    private String shipmentNumber;
    @NotEmpty(message = "Receiver email should not be empty")
    @Email(message = "Receiver email should be a valid email address")
    private String receiverEmail;

    @NotEmpty(message = "Receiver country code is required")
    private String receiverCountryCode;

    @NotEmpty(message = "Sender country code is required")
    private String senderCountryCode;

    @NotNull(message = "Status code is required")
    private int statusCode;
}
