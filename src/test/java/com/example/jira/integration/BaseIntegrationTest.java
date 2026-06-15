package com.example.jira.integration;

import com.example.jira.TicketApplication;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TicketApplication.class)
public class BaseIntegrationTest {

    @LocalServerPort
    int port;
    public WebTestClient webTestClient;
    public RequestHelper requestHelper;

    @BeforeEach
    public void setup() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
        requestHelper = new RequestHelper(webTestClient);
    }
}
