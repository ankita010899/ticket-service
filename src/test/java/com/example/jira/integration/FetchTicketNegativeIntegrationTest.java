package com.example.jira.integration;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

public class FetchTicketNegativeIntegrationTest extends BaseIntegrationTest{

    @Test
    public void tryToFetchNonExistingTicket() {
        // when
        WebTestClient.ResponseSpec responseBody = requestHelper.tryToFetchTicket("non-existing-id");

        // then
        responseBody
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Ticket not found with id: non-existing-id")
                .jsonPath("$.timeStamp").isNotEmpty();
    }
}
