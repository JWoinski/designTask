package com.job.designtask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.designtask.config.KafkaConfig;
import com.job.designtask.model.ApiResponse;
import com.job.designtask.model.dto.OrderRequestDTO;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.yml")
class OrderControllerTest extends KafkaConfig {

    private final MockMvc mockMvc;
    private final DataBaseCleaner databaseCleaner;
    private final ObjectMapper objectMapper;


    @Autowired
    public OrderControllerTest(MockMvc mockMvc,
                               DataBaseCleaner databaseCleaner,
                               ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.databaseCleaner = databaseCleaner;
        this.objectMapper = objectMapper;
    }

    @AfterEach
    void tearDown() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Test
    void whenValidInput_thenReturnsSuccessAndSavesToDatabase() throws Exception {
        // given
        OrderRequestDTO orderRequest = OrderRequestDTO.builder()
                .shipmentNumber("SHIP123")
                .receiverEmail("test@example.com")
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(1)
                .build();

        // expected JSON response
        String expectedResponse = objectMapper.writeValueAsString(
                new ApiResponse<>("Order has been received.")
        );
        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void whenInvalidEmail_thenReturns400() throws Exception {
        // given
        OrderRequestDTO orderRequest = OrderRequestDTO.builder()
                .shipmentNumber("SHIP123")
                .receiverEmail("invalid-email")
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(1)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.receiverEmail").value("Email is not valid"));
    }

    @Test
    void whenInvalidReceiverCountryCode_thenReturns400() throws Exception {
        // given
        OrderRequestDTO orderRequest = OrderRequestDTO.builder()
                .shipmentNumber("SHIP123")
                .receiverEmail("invalid-email")
                .receiverCountryCode("")
                .senderCountryCode("UK")
                .statusCode(1)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.receiverCountryCode").value("Country code of receiver is required"));
    }

    @Test
    void whenInvalidSenderCountryCode_thenReturns400() throws Exception {
        // given
        OrderRequestDTO orderRequest = OrderRequestDTO.builder()
                .shipmentNumber("SHIP123")
                .receiverEmail("invalid-email")
                .receiverCountryCode("US")
                .senderCountryCode("")
                .statusCode(1)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.senderCountryCode").value("Country code of sender is required"));
    }

    @Test
    void whenInvalidEmailAndShimpmentNumber_thenReturns400() throws Exception {
        // given
        OrderRequestDTO orderRequest = OrderRequestDTO.builder()
                .shipmentNumber("")
                .receiverEmail("invalid-email")
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(1)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.receiverEmail").value("Email is not valid"))
                .andExpect(jsonPath("$.shipmentNumber").value("Shipment number is required"));
    }


    @Test
    void whenMissingShipmentNumber_thenReturns400() throws Exception {
        // given
        OrderRequestDTO orderRequest = OrderRequestDTO.builder()
                // missing shipmentNumber
                .receiverEmail("test@example.com")
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(1)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.shipmentNumber").value("Shipment number is required"));
    }

    @Test
    void whenMissingReceiverEmail_thenReturns400() throws Exception {
        // given
        OrderRequestDTO orderRequest = OrderRequestDTO.builder()
                .shipmentNumber("SHIP123")
                //missing receiver email
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(1)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.receiverEmail").value("Email of receiver is required"));
    }

    @Test
    void whenMissingReceiverCountryCode_thenReturns400() throws Exception {
        // given
        OrderRequestDTO orderRequest = OrderRequestDTO.builder()
                .shipmentNumber("SHIP123")
                .receiverEmail("test@example.com")
                //missing reciver country code
                .senderCountryCode("UK")
                .statusCode(1)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.receiverCountryCode").value("Country code of receiver is required"));
    }

    @Test
    void whenMissingSenderCountryCode_thenReturns400() throws Exception {
        // given
        OrderRequestDTO orderRequest = OrderRequestDTO.builder()
                .shipmentNumber("SHIP123")
                .receiverEmail("test@example.com")
                .receiverCountryCode("US")
                //missing sender country code
                .statusCode(1)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.senderCountryCode").value("Country code of sender is required"));
    }

    @Test
    void whenMissingStatusCode_thenReturns400() throws Exception {
        // given
        OrderRequestDTO orderRequest = OrderRequestDTO.builder()
                .shipmentNumber("SHIP123")
                .receiverEmail("test@example.com")
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(-1)
                .build();
        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("Status code must be at least 0"));
    }
}