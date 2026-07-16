# Ticket Service

A **Spring Boot microservice** for managing Jira-style tickets in a distributed backend system. It provides REST endpoints to create and fetch tickets, persists data in its own PostgreSQL database, and consumes **Kafka events** from the user service to support cross-service workflows.

Designed as a companion service to **user-service**, demonstrating database-per-service, event-driven architecture, and test-driven backend development.

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Runtime | Java 21 |
| Framework | Spring Boot 4.1.0 |
| Web | Spring Web |
| Persistence | Spring Data JPA, Hibernate |
| Database | PostgreSQL 15 |
| Messaging | Apache Kafka (Spring Kafka consumer) |
| Testing | JUnit 5, Mockito, WebTestClient |
| Build | Maven |
| Utilities | Lombok |

---

## Architecture

```text
┌─────────────┐     REST           ┌───────────────────┐
│   Client    │ ────────────────► │ TicketController  │
└─────────────┘                   └─────────┬─────────┘
                                            │
                                   ┌────────▼─────────┐
                                   │  TicketService   │
                                   └────────┬─────────┘
                                            │
                                   ┌────────▼─────────┐
                                   │TicketsRepository │
                                   └────────┬─────────┘
                                            │
                                   ┌────────▼─────────┐
                                   │   PostgreSQL     │
                                   │  (tickets DB)    │
                                   └──────────────────┘

┌──────────────────┐   user-deleted-event   ┌─────────────────────────┐
│  User Service    │ ────────────────────► │ UserDeletedEventListener│
│  (Kafka producer)│                         └─────────────────────────┘
└──────────────────┘
```

### Package Structure

```text
src/main/java/com/example/jira/
├── TicketApplication.java
├── controller/TicketController.java
├── service/TicketService.java
├── repository/TicketsRepository.java
├── model/Ticket.java, TicketStatus.java
├── dto/TicketRequest, TicketResponse, ApiResponse
├── config/KafkaConfig.java
├── consumer/UserDeletedEventListener.java
└── exceptions/GlobalExceptionHandler.java, TicketNotFoundException.java
```

---

## Features

- **Create tickets** with description, status, and story points
- **Fetch tickets by ID** with structured response DTOs
- **Domain-driven status enum** — `OPEN`, `IN_PROGRESS`, `CLOSED`, `REJECTED`
- **Kafka consumer** listening on `user-deleted-event` (cross-service integration)
- **RFC 7807 error responses** via Spring `ProblemDetail`
- **Multi-layer test strategy** — unit tests (Mockito) + integration tests (WebTestClient)

---

## API Reference

**Base URL:** `http://localhost:8083`  
**Prefix:** `/api/ticket`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/create` | Create a new ticket |
| `GET` | `/fetch/{id}` | Retrieve a ticket by ID |

### Create Ticket

```http
POST /api/ticket/create
Content-Type: application/json

{
  "description": "Fix login bug on mobile",
  "status": "OPEN",
  "storyPoints": 5
}
```

**Response (201 Created):**

```json
{
  "message": "Ticket created successfully!",
  "id": "uuid-here"
}
```

> If `status` is omitted, it defaults to `OPEN`.

### Fetch Ticket

```http
GET /api/ticket/fetch/{id}
```

**Response (200 OK):**

```json
{
  "id": "uuid-here",
  "description": "Fix login bug on mobile",
  "status": "OPEN",
  "storyPoints": 5
}
```

**Response (404 Not Found):**

```json
{
  "title": "Ticket not found with id: {id}",
  "timeStamp": 1720000000000
}
```

---

## Domain Model

### Ticket Entity

| Field | Type | Description |
|-------|------|-------------|
| `id` | String (UUID) | Primary key |
| `description` | String | Ticket summary/body |
| `status` | TicketStatus enum | Workflow state |
| `storyPoints` | Integer | Estimation |
| `userMetadata` | String | Extensibility field for user linkage |

### TicketStatus

`OPEN` · `IN_PROGRESS` · `CLOSED` · `REJECTED`

---

## Event-Driven Integration

The service subscribes to Kafka topic **`user-deleted-event`** (published by user-service):

```java
@KafkaListener(topics = "user-deleted-event", groupId = "ticket-service-group")
public void consumeUserDeletedEvent(String userId) { ... }
```

**Consumer group:** `ticket-service-group`  
**Offset strategy:** `earliest`

This enables eventual-consistency patterns such as closing or reassigning tickets when a user is removed from the system.

---

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+
- Docker & Docker Compose
- Running Kafka broker (started via user-service `docker-compose.yml` on port `9092`)

### 1. Start Database

From the project root:

```bash
docker compose up -d
```

| Service | Container | Port |
|---------|-----------|------|
| PostgreSQL (tickets) | `jira-ticket-db` | `5432` |

### 2. Start Kafka (if not already running)

From the **user-service** repo:

```bash
docker compose up -d kafka
```

### 3. Run the Application

```bash
./mvnw spring-boot:run
```

The service listens on **port 8083**.

### 4. Configuration

```properties
server.port=8083
spring.datasource.url=jdbc:postgresql://localhost:5432/jira_tickets_db
spring.datasource.username=jira_user
spring.datasource.password=secret_pass
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=ticket-service-group
spring.kafka.consumer.auto-offset-reset=earliest
```

---

## Testing

```bash
./mvnw test
```

| Test | Type | What It Validates |
|------|------|-------------------|
| `CreateTicketTest` | Unit | Ticket creation service logic |
| `FetchTicketTest` | Unit | Ticket retrieval and not-found paths |
| `SaveAndFetchTicketIntegrationTest` | Integration | Full create → fetch happy path |
| `FetchTicketNegativeIntegrationTest` | Integration | 404 + ProblemDetail payload |

Integration tests boot the app on a random port and use **WebTestClient** for HTTP assertions.

---

## Skills Demonstrated

- Clean layered Spring Boot architecture
- REST API design with request/response DTO separation
- JPA entity mapping and Spring Data repositories
- Kafka consumer configuration with `@EnableKafka` and `@KafkaListener`
- Cross-microservice event consumption
- Centralized exception handling with `ProblemDetail`
- Unit + integration testing with Mockito and WebTestClient
- Dockerized PostgreSQL for local development

---

## Roadmap

- [ ] Implement ticket cleanup/reassignment logic in `UserDeletedEventListener`
- [ ] Link tickets to user IDs and validate against user-service
- [ ] Add JWT validation or service-to-service auth
- [ ] Add update/status-transition endpoints
- [ ] Introduce typed Kafka event schemas (Avro/JSON Schema)
