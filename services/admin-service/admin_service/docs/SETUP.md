# Setup & Installation Guide

## Prerequisites
Before running the Admin Service, ensure you have the following installed:
-   **Java Development Kit (JDK) 21**
-   **Maven** (or use the provided `mvnw` wrapper)
-   **Docker Desktop** (for containerization and local infrastructure)
-   **PostgreSQL Client** (optional, for manual DB inspection)

## Configuration

### Environment Variables
The application uses `application.properties` for configuration. Key settings include:

| Property | Description | Default |
| :--- | :--- | :--- |
| `server.port` | HTTP Port | `8082` |
| `spring.datasource.url` | Database URL | `jdbc:postgresql://localhost:5433/admin_service` |
| `spring.datasource.username` | Database User | `admin` |
| `spring.datasource.password` | Database Password | `admin` |
| `spring.kafka.bootstrap-servers` | Kafka Broker | `localhost:9092` |

### Docker Compose
A `docker-compose.yml` file is provided to spin up the necessary infrastructure (PostgreSQL, Kafka, Zookeeper) and the service itself.

## Building the Project

To build the application and run tests:

```bash
./mvnw clean install
```

To skip tests during build:

```bash
./mvnw clean install -DskipTests
```

## Running Locally

### Option 1: Using Docker Compose (Recommended)
This will start the database, Kafka, and the Admin Service in containers.

```bash
docker-compose up -d
```

### Option 2: Running JAR Locally
Ensure PostgreSQL and Kafka are running (you can use `docker-compose up postgres kafka -d`).

```bash
./mvnw spring-boot:run
```

## Verifying Installation

1.  **Health Check**: The application should start on port 8082.
2.  **Swagger UI**: Visit `http://localhost:8082/swagger-ui.html` to explore the API.
3.  **Logs**: Check console logs for "Started AdminServiceApplication".
