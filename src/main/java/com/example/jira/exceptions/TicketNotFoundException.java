package com.example.jira.exceptions;

public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException(String id, String customMessage) {
        super(customMessage + " with id: " + id);
    }
}
