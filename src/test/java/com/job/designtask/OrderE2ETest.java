package com.job.designtask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.designtask.model.dto.OrderRequestDTO;
import com.job.designtask.repository.OrderRepository;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DataBaseCleaner dataBaseCleaner;

    @BeforeEach
    void tearDown() throws LiquibaseException {
        dataBaseCleaner.cleanUp();
    }

    @Test
    void whenOrderIsSubmitted_thenItIsSavedToDatabase() throws Exception {
        // given
        OrderRequestDTO orderRequestDto = OrderRequestDTO.builder()
                .shipmentNumber("SHIP123")
                .receiverEmail("test@example.com")
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(1)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isOk());

        // then - sprawdzamy, czy zamówienie zostało zapisane w bazie danych
        assertThat(orderRepository.findAll())
                .anyMatch(order -> order.getShipmentNumber().equals("SHIP123") &&
                        order.getReceiverEmail().equals("test@example.com") &&
                        order.getReceiverCountryCode().equals("US") &&
                        order.getSenderCountryCode().equals("UK") &&
                        order.getStatusCode() == 1);
    }

    @Test
    void whenOrderIsNotSubmittedEmptyShipmentNumber_thenItNotSavedToDatabase() throws Exception {
        // given
        OrderRequestDTO orderRequestDto = OrderRequestDTO.builder()
                .shipmentNumber("")
                .receiverEmail("test@example.com")
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(1)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.shipmentNumber").value("Shipment number is required"));

        // then - sprawdzamy, czy zamówienie nie zostało zapisane w bazie danych
        assertThat(orderRepository.findAll())
                .noneMatch(order -> order.getShipmentNumber().isEmpty() &&
                        order.getReceiverEmail().equals("test@example.com") &&
                        order.getReceiverCountryCode().equals("US") &&
                        order.getSenderCountryCode().equals("UK") &&
                        order.getStatusCode() == 1);
    }

    @Test
    void whenOrderIsNotSubmittedEmptyReceiverEmail_thenItNotSavedToDatabase() throws Exception {
        // given
        OrderRequestDTO orderRequestDto = OrderRequestDTO.builder()
                .shipmentNumber("123EXP")
                .receiverEmail("")
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(1)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.receiverEmail").value("Email of receiver is required"));

        // then - sprawdzamy, czy zamówienie nie zostało zapisane w bazie danych
        assertThat(orderRepository.findAll())
                .noneMatch(order -> order.getShipmentNumber().equals("123EXP") &&
                        order.getReceiverEmail().isEmpty() &&
                        order.getReceiverCountryCode().equals("US") &&
                        order.getSenderCountryCode().equals("UK") &&
                        order.getStatusCode() == 1);
    }

    @Test
    void whenOrderIsNotSubmittedEmptyReceiverCountryCode_thenItNotSavedToDatabase() throws Exception {
        // given
        OrderRequestDTO orderRequestDto = OrderRequestDTO.builder()
                .shipmentNumber("123EXP")
                .receiverEmail("test@gmail.com")
                .receiverCountryCode("")
                .senderCountryCode("UK")
                .statusCode(1)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.receiverCountryCode").value("Country code of receiver is required"));

        // then - sprawdzamy, czy zamówienie nie zostało zapisane w bazie danych
        assertThat(orderRepository.findAll())
                .noneMatch(order -> order.getShipmentNumber().equals("123EXP") &&
                        order.getReceiverEmail().equals("test@gmail.com") &&
                        order.getReceiverCountryCode().isEmpty() &&
                        order.getSenderCountryCode().equals("UK") &&
                        order.getStatusCode() == 1);
    }

    @Test
    void whenOrderIsNotSubmittedEmptySenderCountryCode_thenItNotSavedToDatabase() throws Exception {
        // given
        OrderRequestDTO orderRequestDto = OrderRequestDTO.builder()
                .shipmentNumber("123EXP")
                .receiverEmail("test@gmail.com")
                .receiverCountryCode("US")
                .senderCountryCode("")
                .statusCode(1)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.senderCountryCode").value("Country code of sender is required"));

        // then - sprawdzamy, czy zamówienie nie zostało zapisane w bazie danych
        assertThat(orderRepository.findAll())
                .noneMatch(order -> order.getShipmentNumber().equals("123EXP") &&
                        order.getReceiverEmail().equals("test@gmail.com") &&
                        order.getReceiverCountryCode().equals("US") &&
                        order.getSenderCountryCode().isEmpty() &&
                        order.getStatusCode() == 1);
    }

    @Test
    void whenOrderIsNotSubmittedStatusCodeBelow0_thenItNotSavedToDatabase() throws Exception {
        // given
        OrderRequestDTO orderRequestDto = OrderRequestDTO.builder()
                .shipmentNumber("123EXP")
                .receiverEmail("test@gmail.com")
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(-1)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("Status code must be at least 0"));

        // then - sprawdzamy, czy zamówienie nie zostało zapisane w bazie danych
        assertThat(orderRepository.findAll())
                .noneMatch(order -> order.getShipmentNumber().equals("123EXP") &&
                        order.getReceiverEmail().equals("test@gmail.com") &&
                        order.getReceiverCountryCode().equals("US") &&
                        order.getSenderCountryCode().equals("UK"));
    }

    @Test
    void whenOrderIsNotSubmittedStatusCodeAbove100_thenItNotSavedToDatabase() throws Exception {
        // given
        OrderRequestDTO orderRequestDto = OrderRequestDTO.builder()
                .shipmentNumber("123EXP")
                .receiverEmail("test@gmail.com")
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(101)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("Status code must be at most 100"));

        // then - sprawdzamy, czy zamówienie nie zostało zapisane w bazie danych
        assertThat(orderRepository.findAll())
                .noneMatch(order -> order.getShipmentNumber().equals("123EXP") &&
                        order.getReceiverEmail().equals("test@gmail.com") &&
                        order.getReceiverCountryCode().equals("US") &&
                        order.getSenderCountryCode().equals("UK"));
    }

    @Test
    void whenOrderIsNotSubmittedNullSenderCountryCode_thenItNotSavedToDatabase() throws Exception {
        // given
        OrderRequestDTO orderRequestDto = OrderRequestDTO.builder()
                .shipmentNumber("123EXP")
                .receiverEmail("test@gmail.com")
                .receiverCountryCode("US")
//               missing sender code
                .statusCode(100)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.senderCountryCode").value("Country code of sender is required"));

        // then - sprawdzamy, czy zamówienie nie zostało zapisane w bazie danych
        assertThat(orderRepository.findAll())
                .noneMatch(order -> order.getShipmentNumber().equals("123EXP") &&
                        order.getReceiverEmail().equals("test@gmail.com") &&
                        order.getReceiverCountryCode().equals("US") &&
                        order.getStatusCode() == 100);
    }

    @Test
    void whenOrderIsNotSubmittedNullReceiverCountryCode_thenItNotSavedToDatabase() throws Exception {
        // given
        OrderRequestDTO orderRequestDto = OrderRequestDTO.builder()
                .shipmentNumber("123EXP")
                .receiverEmail("test@gmail.com")
                // missing receiver country code
                .senderCountryCode("UK")
                .statusCode(100)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.receiverCountryCode").value("Country code of receiver is required"));

        // then - sprawdzamy, czy zamówienie nie zostało zapisane w bazie danych
        assertThat(orderRepository.findAll())
                .noneMatch(order -> order.getShipmentNumber().equals("123EXP") &&
                        order.getReceiverEmail().equals("test@gmail.com") &&
                        order.getSenderCountryCode().equals("US") &&
                        order.getStatusCode() == 100);
    }

    @Test
    void whenOrderIsNotSubmittedNullReceiverEmail_thenItNotSavedToDatabase() throws Exception {
        // given
        OrderRequestDTO orderRequestDto = OrderRequestDTO.builder()
                .shipmentNumber("123EXP")
//               missing receiver email
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(100)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.receiverEmail").value("Email of receiver is required"));

        // then - sprawdzamy, czy zamówienie nie zostało zapisane w bazie danych
        assertThat(orderRepository.findAll())
                .noneMatch(order -> order.getShipmentNumber().equals("123EXP") &&
                        order.getReceiverCountryCode().equals("US") &&
                        order.getSenderCountryCode().equals("UK") &&
                        order.getStatusCode() == 100);
    }

    @Test
    void whenOrderIsNotSubmittedNullShipmentNumber_thenItNotSavedToDatabase() throws Exception {
        // given
        OrderRequestDTO orderRequestDto = OrderRequestDTO.builder()
//                .shipmentNumber("123EXP") missing shipment number
                .receiverEmail("test@gmail.com")
                .receiverCountryCode("US")
                .senderCountryCode("UK")
                .statusCode(100)
                .build();

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.shipmentNumber").value("Shipment number is required"));

        // then - sprawdzamy, czy zamówienie nie zostało zapisane w bazie danych
        assertThat(orderRepository.findAll())
                .noneMatch(order -> order.getReceiverEmail().equals("test@gmail.com") &&
                        order.getReceiverCountryCode().equals("US") &&
                        order.getSenderCountryCode().equals("UK") &&
                        order.getStatusCode() == 100);
    }

}

