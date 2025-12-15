# Gateway Service - Configuration & Routes

## Route Configuration
Routes are defined in `application.yml` or Java Configuration.

| Path Pattern | Target Service | Predicates | Filters |
| :--- | :--- | :--- | :--- |
| `/api/auth/**` | `auth-service` | Method: POST/GET | StripPrefix=1 |
| `/api/v1/users/**` | `user-service` | | StripPrefix=1 |
| `/api/v1/vendors/**` | `vendor-service` | | StripPrefix=1 |
| `/api/v1/procurements/**` | `procurement-service` | | StripPrefix=1 |
| `/api/v1/audit/**` | `audit-service` | Roles: ADMIN | StripPrefix=1 |
| `/api/v1/documents/**` | `document-service` | | StripPrefix=1 |

## Configuration Example (YAML)

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth_route
          uri: lb://auth-service
          predicates:
            - Path=/api/auth/**
          filters:
            - StripPrefix=1
        
        - id: procurement_route
          uri: lb://procurement-service
          predicates:
            - Path=/api/v1/procurements/**
          filters:
            - StripPrefix=1
            - name: CircuitBreaker
              args:
                name: procurementCB
                fallbackUri: forward:/fallback

      globalcors:
        cors-configurations:
          '[/**]':
            allowedOrigins: "*"
            allowedMethods:
              - GET
              - POST
              - PUT
              - DELETE
            allowedHeaders: "*"
```

## Security Headers
The Gateway injects standard security headers:
*   `X-Content-Type-Options: nosniff`
*   `X-Frame-Options: DENY`
*   `X-XSS-Protection: 1; mode=block`
