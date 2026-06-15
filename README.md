# Spring Boot Skills Showcase — Ticket Service

This project is a focused demonstration of **production-style Spring Boot backend development** using layered architecture, clean API design, persistence, exception handling, messaging integration, and test coverage.

## What This Demonstrates in Spring Boot

### 1. Layered Application Architecture

- `controller` layer for HTTP contracts (`TicketController`)
- `service` layer for business logic (`TicketService`)
- `repository` layer with Spring Data JPA (`TicketsRepository`)
- clear separation of DTOs, entities, and domain enums

### 2. REST API Design with Spring Web

- annotation-driven endpoints with `@RestController`, `@RequestMapping`, `@PostMapping`, `@GetMapping`
- explicit status codes via `@ResponseStatus`
- dedicated request/response DTOs to keep API contract decoupled from persistence model

### 3. Persistence with Spring Data JPA

- JPA entity mapping using `@Entity`, `@Table`, `@Id`, `@Enumerated`
- repository abstraction with `JpaRepository` for CRUD without boilerplate
- enum-backed ticket lifecycle (`TicketStatus`) stored as readable strings

### 4. Robust Error Handling

- custom domain exception (`TicketNotFoundException`)
- centralized exception mapping using `@RestControllerAdvice`
- standards-based error payloads with Spring `ProblemDetail`

### 5. Event-Driven Integration Readiness

- Kafka consumer enabled via `@EnableKafka`
- listener implementation using `@KafkaListener`
- infrastructure-ready foundation for asynchronous workflows

### 6. Spring-Centric Testing Approach

- isolated unit tests with Mockito for service logic
- integration-style API tests with `@SpringBootTest` and `WebTestClient`
- positive and negative path validation for endpoint behavior

## Spring Components Implemented

```text
src/main/java/com/example/jira
├── TicketApplication.java              # Spring Boot bootstrap
├── controller/TicketController.java    # REST endpoints
├── service/TicketService.java          # Business logic
├── repository/TicketsRepository.java   # Spring Data JPA repository
├── model/Ticket.java                   # JPA entity
├── model/TicketStatus.java             # Domain enum
├── exceptions/GlobalExceptionHandler.java
├── exceptions/TicketNotFoundException.java
├── config/KafkaConfig.java             # Kafka consumer configuration
└── consumer/UserDeletedEventListener.java
```

## Core Spring Boot Competencies Highlighted

- designing maintainable backend services with Spring conventions
- translating business use-cases into clean service-layer logic
- handling persistence concerns through JPA abstractions
- delivering resilient API behavior with centralized exception strategy
- preparing services for event-driven architecture with Kafka
- building confidence through both unit and integration testing styles
