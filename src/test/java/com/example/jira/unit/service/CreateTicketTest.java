package com.example.jira.unit.service;

import com.example.jira.dto.ApiResponse;
import com.example.jira.dto.TicketRequest;
import com.example.jira.model.Ticket;
import com.example.jira.model.TicketStatus;
import com.example.jira.repository.TicketsRepository;
import com.example.jira.service.TicketService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateTicketTest {

    @Mock
    private TicketsRepository ticketsRepository;
    @InjectMocks
    private TicketService ticketService;

    @Test
    public void testCreateTicket() {
        // given
        String expectedId = "1fac98d7-2d45-4fa7-a35f-2bf492bdd9fd";
        Ticket ticket = Ticket.builder()
                .id(expectedId)
                .description("This is a test ticket")
                .status(TicketStatus.OPEN)
                .storyPoints(5)
                .build();

        when(ticketsRepository.save(any(Ticket.class))).thenReturn(ticket);

        // when
        TicketRequest request = TicketRequest.builder()
                .description("This is a test ticket")
                .status(TicketStatus.OPEN.toString())
                .storyPoints(5)
                .build();
        ApiResponse response = ticketService.createAndSaveTicket(request);

        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getMessage()).isEqualTo("Ticket created successfully!");
        Assertions.assertThat(response.getId()).isEqualTo(expectedId);

    }
}
