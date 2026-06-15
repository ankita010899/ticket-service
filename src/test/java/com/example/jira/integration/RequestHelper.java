package com.example.jira.integration;

import com.example.jira.dto.ApiResponse;
import com.example.jira.dto.TicketRequest;
import com.example.jira.dto.TicketResponse;
import lombok.AllArgsConstructor;
import org.springframework.test.web.reactive.server.WebTestClient;

@AllArgsConstructor
public class RequestHelper {
    private WebTestClient webTestClient;

    public ApiResponse createTicket(TicketRequest ticketRequest){
        return webTestClient.post()
                .uri("/api/ticket/create")
                .bodyValue(ticketRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ApiResponse.class)
                .returnResult()
                .getResponseBody();

    }

    public TicketResponse fetchTicket(String id) {
        return tryToFetchTicket(id)
                .expectStatus().isOk()
                .expectBody(TicketResponse.class)
                .returnResult()
                .getResponseBody();
    }

    public WebTestClient.ResponseSpec tryToFetchTicket(String id) {
        return webTestClient.get()
                .uri("/api/ticket/fetch/{id}", id)
                .exchange();
    }
}
