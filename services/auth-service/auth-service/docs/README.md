# Auth Service Documentation

## Generated Artifacts
- **Source Code**: `src/main/java/com/tugasakhir/authservice`
- **Docker**: `Dockerfile`, `docker-compose.yml`
- **Tests**: `src/test/java`

## How to Run
### Prerequisites
- Docker & Docker Compose
- Java 21 (optional if using Docker)
- Maven (optional if using Docker)

### Using Docker Compose (Recommended)
```bash
docker-compose up --build
```
This will start:
- Postgres (5432)
- Redis (6379)
- Kafka (9092)
- Auth Service (8080)

### Local Development
1. Start Infra:
   ```bash
   docker-compose up postgres redis kafka zookeeper -d
   ```
2. Run App:
   ```bash
   ./mvnw spring-boot:run
   ```

## Environment Variables
| Variable | Default | Description |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/authdb` | DB URL |
| `SPRING_REDIS_HOST` | `localhost` | Redis Host |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` | Kafka Brokers |
| `JWT_SECRET` | (dev secret) | HS256 Secret Key |
| `JWT_ACCESS_TOKEN_TTL` | `900000` (15m) | Access Token TTL (ms) |
| `JWT_REFRESH_TOKEN_TTL` | `1209600000` (14d) | Refresh Token TTL (ms) |

## Endpoints
See [USECASES.md](USECASES.md) for a detailed list of implemented use cases and endpoints.
See [OPENAPI.md](OPENAPI.md) for Swagger UI details.
