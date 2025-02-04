package com.job.designtask.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private int statusCode;
}