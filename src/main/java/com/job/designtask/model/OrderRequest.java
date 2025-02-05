package com.job.designtask.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OrderRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderId;
    @NotBlank(message = "Shipment number is required")
    private String shipmentNumber;
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email of receiver is required")
    private String receiverEmail;

    @NotBlank(message = "Conutry code of receiver is required")
    private String receiverCountryCode;

    @NotBlank(message = "Country code of sender is required")
    private String senderCountryCode;

    @NotNull(message = "Status code is required")
    @Min(value = 0, message = "Status code must be at least 0")
    @Max(value = 100, message = "Status code must be at most 100")
    private int statusCode;
    private LocalDateTime receivedAt;
}