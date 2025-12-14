# Vendor Service Documentation

## Overview
The **Vendor Service** is a core microservice in the e-Procurement system responsible for managing the entire vendor lifecycle. This includes vendor registration, catalog management, RFQ responses, quotation submission, delivery tracking, invoice generation, and performance evaluation.

## Tech Stack
- **Language**: Java 17
- **Framework**: Spring Boot 3.3.0
- **Database**: PostgreSQL 15
- **Messaging**: Kafka (Confluent Platform 7.5.0)
- **API Documentation**: SpringDoc OpenAPI (Swagger UI)
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose

## Architecture
The service follows a layered architecture:
- **Controller Layer**: Handles HTTP requests and defines REST APIs.
- **Service Layer**: Contains business logic and transaction management.
- **Repository Layer**: Manages data access using Spring Data JPA.
- **Model Layer**: Defines JPA entities and database schema.
- **DTO Layer**: Data Transfer Objects for API request/response.

## Key Features
1.  **Vendor Account Management**: Manage vendor profiles and status.
2.  **Catalog Management**: Vendors can manage their products/services.
3.  **RFQ & Quotation**:
    - View open RFQs.
    - Submit, revise, or decline quotations.
4.  **Delivery Management**:
    - Create deliveries based on POs.
    - Upload packing lists and proof of delivery (POD).
5.  **Invoice Management**:
    - Auto-generate invoices from POs or payment terms.
6.  **Contract & Payment Terms**: Manage contracts and agreed payment schedules.

## Running the Service

### Prerequisites
- Docker Desktop installed and running.
- Java 17 SDK (optional, for local dev).

### Using Docker Compose (Recommended)
This will start the Vendor Service along with PostgreSQL and Kafka.

```bash
cd vendor-service
docker-compose up --build
```

The service will be available at `http://localhost:8083`.

### Local Development
1.  Ensure PostgreSQL and Kafka are running locally or update `application.properties` to point to your instances.
2.  Run the application:
    ```bash
    ./mvnw spring-boot:run
    ```

## API Documentation
Once the service is running, you can access the interactive API documentation (Swagger UI) at:

[http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html)

## Configuration
Configuration is managed in `src/main/resources/application.properties`.

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | 8083 | Service port |
| `spring.datasource.url` | `jdbc:postgresql://localhost:5433/vendor_db` | Database URL |
| `spring.kafka.bootstrap-servers` | `localhost:9092` | Kafka brokers |

## Database Schema
The service uses the following main tables:
- `vendor_accounts`
- `vendor_catalog_items`
- `rfqs`
- `quotation_responses`
- `deliveries`
- `invoices`
- `contracts`
