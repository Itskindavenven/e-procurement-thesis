# Architecture & Folder Structure

## Directory Structure

```
vendor-service/
├── src/main/java/com/tugasakhir/vendor_service/
│   ├── config/             # Configuration classes (OpenAPI, etc.)
│   ├── controller/         # REST Controllers (grouped by domain)
│   │   ├── catalog/
│   │   ├── rfq/
│   │   ├── ...
│   ├── service/            # Business Logic (grouped by domain)
│   ├── repository/         # JPA Repositories
│   ├── model/              # JPA Entities
│   │   ├── BaseEntity.java # Common fields (createdAt, updatedAt, isDeleted)
│   ├── dto/                # Data Transfer Objects & Mappers
│   ├── exception/          # Global Exception Handling
│   └── utils/              # Utility classes
├── src/main/resources/
│   └── application.properties
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

## Design Patterns

### 1. Domain-Driven Packaging
Classes are grouped by layer (`controller`, `service`, etc.) and then by domain (`catalog`, `rfq`). This makes it easier to navigate the codebase as it grows.

### 2. Base Entity
All entities extend `BaseEntity`, which provides:
- `@CreatedDate`: Automatically records creation timestamp.
- `@LastModifiedDate`: Automatically records update timestamp.
- `isDeleted`: Supports soft-delete implementation.

### 3. Global Exception Handling
`GlobalExceptionHandler` uses `@RestControllerAdvice` to catch exceptions globally and return a standardized `ErrorResponse` JSON structure.

### 4. DTO Pattern & MapStruct
We strictly separate Internal Entities from Public APIs using DTOs. `MapStruct` is used for high-performance, type-safe mapping between Entities and DTOs.

### 5. Soft Delete
Data is rarely permanently deleted. The `delete` operations in services typically set the `isDeleted` flag to `true` instead of removing the row from the database.
