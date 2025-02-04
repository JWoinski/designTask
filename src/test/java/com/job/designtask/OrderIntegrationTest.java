package com.job.designtask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.designtask.model.dto.OrderRequestDTO;
import com.job.designtask.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//class OrderIntegrationTest {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void testFullOrderCreationFlow() throws Exception {
//        OrderRequestDTO orderRequest = new OrderRequestDTO("SHIP123", "test@example.com", "US", "UK", 0);
//
//        HttpEntity<OrderRequestDTO> request = new HttpEntity<>(orderRequest);
//        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/api/orders", request, String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(orderRepository.findByShipmentNumber("SHIP123")).isPresent();
//    }
//}
