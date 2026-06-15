package com.example.jira.unit.service;


import com.example.jira.dto.TicketResponse;
import com.example.jira.model.Ticket;
import com.example.jira.repository.TicketsRepository;
import com.example.jira.service.TicketService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetchTicketTest {

    @Mock
    private TicketsRepository ticketsRepository;
    @InjectMocks
    private TicketService ticketService;

    @Test
    public void fetchTicketByIdSuccess() {
        // given
        String expectedId = UUID.randomUUID().toString();
        when(ticketsRepository.findById(expectedId))
                .thenReturn(Optional.of(
                        Ticket.builder()
                                .id(expectedId)
                                .description("This is a test ticket")
                                .status(com.example.jira.model.TicketStatus.OPEN)
                                .storyPoints(5)
                                .userMetadata("Custom meta data")
                                .build())
                );

        // when
        TicketResponse response = ticketService.fetchTicketById(expectedId);

        // then
        Assertions.assertEquals(expectedId, response.getId());
        Assertions.assertNotNull(response.getDescription());
        Assertions.assertNotNull(response.getStatus());
        Assertions.assertNotNull(response.getStoryPoints());
    }

    @Test
    public void fetchTicketByIdFailure() {
        // given
        String exampleId = UUID.randomUUID().toString();
        when(ticketsRepository.findById(exampleId)).thenReturn(Optional.empty());

        // when
        RuntimeException exp = Assertions.assertThrows(RuntimeException.class, () -> ticketService.fetchTicketById(exampleId));

        // then
        Assertions.assertEquals("Ticket not found with id: " + exampleId, exp.getMessage());
    }
}
