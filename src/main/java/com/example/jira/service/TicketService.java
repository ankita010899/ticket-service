package com.example.jira.service;

import com.example.jira.dto.ApiResponse;
import com.example.jira.dto.TicketRequest;
import com.example.jira.dto.TicketResponse;
import com.example.jira.exceptions.TicketNotFoundException;
import com.example.jira.model.Ticket;
import com.example.jira.model.TicketStatus;
import com.example.jira.repository.TicketsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketsRepository ticketsRepository;

    public ApiResponse createAndSaveTicket(TicketRequest ticketRequest) {
        Ticket response = ticketsRepository.save(buildRequest(ticketRequest));
        return ApiResponse.builder()
                .message("Ticket created successfully!")
                .id(response.getId())
                .build();
    }

    public TicketResponse fetchTicketById(String id) {
        return ticketsRepository.findById(id)
                .map(
                ticket -> TicketResponse.builder()
                        .id(ticket.getId())
                        .description(ticket.getDescription())
                        .status(ticket.getStatus().name())
                        .storyPoints(ticket.getStoryPoints())
                        .build()
                ).orElseThrow(() -> new TicketNotFoundException(id, "Ticket not found"));
    }

    private Ticket buildRequest(TicketRequest ticketRequest) {
        return Ticket.builder()
                .id(UUID.randomUUID().toString())
                .description(ticketRequest.getDescription())
                .status(ticketRequest.getStatus()!=null ? TicketStatus.valueOf(ticketRequest.getStatus()) : TicketStatus.OPEN) // TODO whats the use of Builder default
                .storyPoints(ticketRequest.getStoryPoints())
                .userMetadata("Custom meta data")
                .build();
    }
}
