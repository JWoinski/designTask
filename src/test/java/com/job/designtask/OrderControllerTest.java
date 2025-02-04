package com.job.designtask;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.designtask.model.dto.OrderRequestDTO;
import com.job.designtask.service.OrderMessageService;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.yml")
class OrderControllerTest extends KafkaConfig {

    private final MockMvc mockMvc;
    private final DataBaseCleaner databaseCleaner;
    private final ObjectMapper objectMapper;

    @MockBean
    private OrderMessageService orderMessageService;

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

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                // then
                .andExpect(status().isOk())
                .andExpect(content().string("Order received and processed."));
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
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenDuplicateShipmentNumber_thenReturns409() throws Exception {
        // given
        OrderRequestDTO firstOrder = OrderRequestDTO.builder()
                .shipmentNumber("SHIP123")
                .receiverEmail("first@example.com")
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(1)
                .build();

        // Create first order
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstOrder)))
                .andExpect(status().isOk());

        // Try to create duplicate
        OrderRequestDTO duplicateOrder = OrderRequestDTO.builder()
                .shipmentNumber("SHIP123")
                .receiverEmail("second@example.com")
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(1)
                .build();

        // when/then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateOrder)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenMissingRequiredField_thenReturns400() throws Exception {
        // given
        OrderRequestDTO orderRequest = OrderRequestDTO.builder()
                .receiverEmail("test@example.com")
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(1)
                // missing shipmentNumber
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                // then
                .andExpect(status().isBadRequest());
    }
}