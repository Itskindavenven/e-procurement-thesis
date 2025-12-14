# Admin Service Documentation

## Overview
The **Admin Service** is the central administrative component of the e-Procurement System for PT XYZ. It is designed to manage internal operations, master data, and administrative workflows.

## Key Features
-   **Employee Management**: Manage internal users, roles, and assignments.
-   **Vendor Verification**: Workflow for approving or rejecting vendor registrations.
-   **Location Management**: Manage operational locations and assign staff.
-   **Master Data**: Centralized management of items, categories, and units.
-   **Procurement Oversight**: Administrative corrections and audit flagging.
-   **Calendar Operations**: Manage operational days and holidays.
-   **Notifications**: Template management and log viewing.
-   **Audit Logging**: Centralized system activity logging.

## Technology Stack
-   **Language**: Java 21
-   **Framework**: Spring Boot 3.2.0
-   **Database**: PostgreSQL 15
-   **Messaging**: Apache Kafka
-   **Security**: Spring Security (Resource Server with JWT)
-   **Migration**: Flyway
-   **Documentation**: OpenAPI (Swagger)

## Directory Structure
-   `src/main/java`: Source code
-   `src/main/resources`: Configuration and migrations
-   `src/test`: Unit and Integration tests
-   `docs/`: Detailed documentation (You are here)

## Navigation
-   [Setup & Installation](SETUP.md)
-   [Domain Logic Guide](DOMAINS.md)
-   [Database Schema](DATABASE.md)
-   [API Reference](API_REFERENCE.md)
-   [Test Scenarios](TEST_SCENARIOS.md)
