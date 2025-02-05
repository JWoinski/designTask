package com.job.designtask.model.dto;

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
    @NotBlank(message = "Shipment number is required")
    private String shipmentNumber;
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email of receiver is required")
    private String receiverEmail;

    @NotBlank(message = "Country code of receiver is required")
    private String receiverCountryCode;

    @NotBlank(message = "Country code of sender is required")
    private String senderCountryCode;

    @NotNull(message = "Status code is required")
    @Min(value = 0, message = "Status code must be at least 0")
    @Max(value = 100, message = "Status code must be at most 100")
    private int statusCode;
}