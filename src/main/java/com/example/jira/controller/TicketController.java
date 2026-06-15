package com.example.jira.controller;

import com.example.jira.dto.ApiResponse;
import com.example.jira.dto.TicketRequest;
import com.example.jira.dto.TicketResponse;
import com.example.jira.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createTicket(
            @RequestBody TicketRequest ticketRequest
    ){
        return ticketService.createAndSaveTicket(ticketRequest);
    }

    @GetMapping("/fetch/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TicketResponse fetchTicket(
            @PathVariable String id
    ){
        return ticketService.fetchTicketById(id);
    }
}
