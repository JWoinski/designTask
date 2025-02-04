package com.job.designtask.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {
    @NotEmpty(message = "Shipment number is required")
    private String shipmentNumber;
    @Email(message = "Email is not valid")
    @NotEmpty(message = "Email cannot be empty")
    private String receiverEmail;

    @NotEmpty(message = "Receiver country code is required")
    private String receiverCountryCode;

    @NotEmpty(message = "Sender country code is required")
    private String senderCountryCode;

    @NotNull(message = "Status code is required")
    @Min(value = 0, message = "Status code must be at least 0")
    @Max(value = 100, message = "Status code must be at most 100")
    private int statusCode;
}