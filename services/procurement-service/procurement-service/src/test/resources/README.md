# Procurement Service Tests

This project uses JUnit 5 for unit tests and Testcontainers for integration tests.

## Prerequisites
*   Docker (must be running for Integration Tests)
*   Java 21
*   Maven

## Running Tests

### Unit Tests Only
Fast validation of business logic without spinning up containers.
```bash
mvn test -DskipITs
```

### Integration Tests
Runs tests that connect to real (containerized) Database and Kafka.
```bash
mvn verify
```

## Structure
*   `com.tugas_akhir.procurementservice.domain.*`: Unit tests per domain.
*   `com.tugas_akhir.procurementservice.integration.*`: End-to-end integration tests.
*   `TestDataFactory`: Helper for creating test objects.
