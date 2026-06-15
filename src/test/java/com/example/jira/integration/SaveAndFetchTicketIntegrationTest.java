package com.example.jira.integration;

import com.example.jira.dto.ApiResponse;
import com.example.jira.dto.TicketRequest;
import com.example.jira.dto.TicketResponse;
import com.example.jira.model.TicketStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SaveAndFetchTicketIntegrationTest extends BaseIntegrationTest{

    @Test
    public void saveAndFetchTicket() {
        // given
        TicketRequest request = TicketRequest.builder()
                .description("Test Ticket")
                .status(TicketStatus.OPEN.toString())
                .storyPoints(5)
                .build();

        // when
        ApiResponse response = requestHelper.createTicket(request);

        // then
        String ticketId = response.getId();
        Assertions.assertEquals("Ticket created successfully!", response.getMessage());

        // when
        TicketResponse fetchedTicketResponse = requestHelper.fetchTicket(ticketId);

        // then
        Assertions.assertEquals(ticketId, fetchedTicketResponse.getId());
        Assertions.assertEquals("Test Ticket", fetchedTicketResponse.getDescription());
        Assertions.assertEquals("OPEN", fetchedTicketResponse.getStatus());
        Assertions.assertEquals(5, fetchedTicketResponse.getStoryPoints());
    }

}
