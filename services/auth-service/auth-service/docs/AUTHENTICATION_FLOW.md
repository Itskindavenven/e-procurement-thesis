# Procurement Service Authentication Flow Documentation

## Table of Contents
1. [Overview](#overview)
2. [Complete Authentication Flow](#complete-authentication-flow)
3. [JWT Token Structure](#jwt-token-structure)
4. [Request Processing Flow](#request-processing-flow)
5. [Role-Based Authorization](#role-based-authorization)
6. [Error Handling](#error-handling)
7. [Security Components](#security-components)

---

## Overview

The procurement-service uses **OAuth2 Resource Server** pattern with **JWT (JSON Web Token)** authentication. All protected endpoints require a valid JWT token issued by the auth-service.

**Key Principles:**
- **Stateless Authentication**: No session storage, all auth info in JWT
- **Shared Secret Validation**: JWT signed with same secret as auth-service
- **Role-Based Access Control**: Endpoints protected by user roles
- **Token Expiration**: Tokens expire after 15 minutes (configurable)

---

## Complete Authentication Flow

### End-to-End User Journey

```mermaid
sequenceDiagram
    participant User as User/Client
    participant Auth as Auth Service<br/>(Port 8081)
    participant Redis as Redis<br/>(Token Store)
    participant Proc as Procurement Service<br/>(Port 8083)
    participant DB as PostgreSQL<br/>(Database)
    
    Note over User,DB: 1. LOGIN PHASE
    User->>Auth: POST /api/v1/auth/login<br/>{username, password}
    Auth->>DB: Validate credentials
    DB-->>Auth: User + Roles
    Auth->>Auth: Generate JWT Token<br/>(userId, username, roles)
    Auth->>Redis: Store refresh token
    Auth-->>User: {accessToken, refreshToken}
    
    Note over User,DB: 2. API REQUEST PHASE
    User->>Proc: POST /api/operator/procurement/requests<br/>Authorization: Bearer <JWT>
    
    Note over Proc: JWT Validation Filter
    Proc->>Proc: Extract JWT from header
    Proc->>Proc: Validate signature (HS256)
    Proc->>Proc: Check expiration
    Proc->>Proc: Extract roles from claims
    
    alt JWT Valid
        Proc->>Proc: Set SecurityContext<br/>(userId, roles)
        Proc->>Proc: Check role authorization<br/>(ROLE_OPERATOR required)
        
        alt Has Required Role
            Proc->>Proc: Extract userId from JWT
            Proc->>DB: Execute business logic
            DB-->>Proc: Result
            Proc-->>User: 201 Created
        else Missing Required Role
            Proc-->>User: 403 Forbidden
        end
    else JWT Invalid/Expired
        Proc-->>User: 401 Unauthorized
    end
```

---

## JWT Token Structure

### Token Claims

When auth-service generates a JWT, it includes the following claims:

```json
{
  "sub": "550e8400-e29b-41d4-a716-446655440000",  // User ID (UUID)
  "username": "john.operator",                    // Username
  "roles": ["ROLE_OPERATOR"],                     // User roles
  "jti": "7c9e6679-7425-40de-944b-e07fc1f90ae7", // JWT ID (for blacklisting)
  "iat": 1702300000,                              // Issued at (timestamp)
  "exp": 1702300900                               // Expires at (timestamp)
}
```

### Token Validation in Procurement Service

```mermaid
graph TB
    A[Incoming Request] --> B{Has Authorization<br/>Header?}
    B -->|No| C[401 Unauthorized]
    B -->|Yes| D[Extract JWT Token]
    D --> E{Valid Signature?}
    E -->|No| C
    E -->|Yes| F{Token Expired?}
    F -->|Yes| C
    F -->|No| G[Extract Claims]
    G --> H[Extract Roles]
    H --> I[Create Authentication]
    I --> J[Set SecurityContext]
    J --> K[Continue to Controller]
    
    style C fill:#ffcccc
    style K fill:#ccffcc
```

---

## Request Processing Flow

### Detailed Step-by-Step Process

#### Step 1: Client Sends Request

```http
POST /api/operator/procurement/requests HTTP/1.1
Host: localhost:8083
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "vendorId": "uuid",
  "type": "GOODS",
  "description": "Office supplies",
  "items": [...]
}
```

#### Step 2: Spring Security Filter Chain

```mermaid
sequenceDiagram
    participant Request
    participant SecurityFilter as SecurityFilterChain
    participant JwtDecoder as JwtDecoder
    participant AuthConverter as JwtAuthenticationConverter
    participant Controller
    
    Request->>SecurityFilter: Incoming HTTP Request
    SecurityFilter->>SecurityFilter: Check if endpoint is public
    
    alt Public Endpoint (/swagger-ui, /actuator)
        SecurityFilter->>Controller: Allow without auth
    else Protected Endpoint
        SecurityFilter->>JwtDecoder: Decode JWT Token
        JwtDecoder->>JwtDecoder: Validate signature<br/>using jwt.secret
        JwtDecoder->>JwtDecoder: Check expiration
        
        alt Valid Token
            JwtDecoder-->>SecurityFilter: Jwt object
            SecurityFilter->>AuthConverter: Convert to Authentication
            AuthConverter->>AuthConverter: Extract roles claim
            AuthConverter->>AuthConverter: Add ROLE_ prefix
            AuthConverter-->>SecurityFilter: Authentication object
            SecurityFilter->>SecurityFilter: Set SecurityContext
            SecurityFilter->>SecurityFilter: Check role requirements
            
            alt Has Required Role
                SecurityFilter->>Controller: Forward request
            else Missing Role
                SecurityFilter-->>Request: 403 Forbidden
            end
        else Invalid Token
            SecurityFilter-->>Request: 401 Unauthorized
        end
    end
```

#### Step 3: Controller Processes Request

```java
@PostMapping
public ResponseEntity<ProcurementRequestResponseDTO> createProcurementRequest(
        @Valid @RequestBody CreateProcurementRequestDTO request,
        @AuthenticationPrincipal Jwt jwt,  // ← JWT injected by Spring Security
        @RequestHeader(value = "X-User-Id", required = false) UUID headerUserId) {
    
    // Extract user ID from validated JWT
    UUID operatorId = SecurityUtils.getUserIdFromJwt(jwt);
    
    // Backward compatibility: validate header if provided
    if (headerUserId != null) {
        SecurityUtils.validateUserIdMatch(headerUserId, jwt);
    }
    
    // Business logic with authenticated user ID
    ProcurementRequestResponseDTO response = 
        procurementRequestService.createProcurementRequest(request, operatorId);
    
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}
```

---

## Role-Based Authorization

### Endpoint Protection Matrix

| Endpoint Pattern | Required Role | Example |
|-----------------|---------------|---------|
| `/api/operator/**` | `ROLE_OPERATOR` | Create/update PRs |
| `/api/supervisor/**` | `ROLE_SUPERVISOR` or `ROLE_ADMIN` | Approve/reject PRs |
| `/api/finance/**` | `ROLE_FINANCE` | Financial operations |
| `/actuator/**` | Public | Health checks |
| `/swagger-ui/**` | Public | API documentation |

### Authorization Flow

```mermaid
graph LR
    A[Request] --> B{Endpoint Pattern}
    B -->|/api/operator/**| C{Has ROLE_OPERATOR?}
    B -->|/api/supervisor/**| D{Has ROLE_SUPERVISOR<br/>or ROLE_ADMIN?}
    B -->|/api/finance/**| E{Has ROLE_FINANCE?}
    B -->|/actuator/**| F[Allow]
    
    C -->|Yes| G[Allow]
    C -->|No| H[403 Forbidden]
    D -->|Yes| G
    D -->|No| H
    E -->|Yes| G
    E -->|No| H
    
    style G fill:#90EE90
    style H fill:#FFB6C1
    style F fill:#87CEEB
```

### SecurityConfig Implementation

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    http
        .authorizeHttpRequests(auth -> auth
            // Public endpoints
            .requestMatchers("/actuator/**", "/swagger-ui/**").permitAll()
            
            // Role-based protection
            .requestMatchers("/api/operator/**").hasRole("OPERATOR")
            .requestMatchers("/api/supervisor/**").hasAnyRole("SUPERVISOR", "ADMIN")
            .requestMatchers("/api/finance/**").hasRole("FINANCE")
            
            // All others require authentication
            .anyRequest().authenticated()
        )
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.decoder(jwtDecoder()))
        );
}
```

---

## Error Handling

### Authentication Error Scenarios

#### Scenario 1: Missing JWT Token

```mermaid
sequenceDiagram
    participant Client
    participant Proc as Procurement Service
    
    Client->>Proc: POST /api/operator/procurement/requests<br/>(No Authorization header)
    Proc->>Proc: SecurityFilterChain checks auth
    Proc-->>Client: 401 Unauthorized<br/>{error: "Unauthorized"}
```

**HTTP Response:**
```http
HTTP/1.1 401 Unauthorized
Content-Type: application/json

{
  "timestamp": "2024-12-11T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/operator/procurement/requests"
}
```

---

#### Scenario 2: Expired JWT Token

```mermaid
sequenceDiagram
    participant Client
    participant Proc as Procurement Service
    participant JwtDecoder
    
    Client->>Proc: POST /api/operator/procurement/requests<br/>Authorization: Bearer <EXPIRED_JWT>
    Proc->>JwtDecoder: Decode and validate JWT
    JwtDecoder->>JwtDecoder: Check expiration
    Note over JwtDecoder: exp: 1702300900<br/>now: 1702301000<br/>(expired!)
    JwtDecoder-->>Proc: JwtException: Token expired
    Proc-->>Client: 401 Unauthorized<br/>{error: "Token expired"}
```

**HTTP Response:**
```http
HTTP/1.1 401 Unauthorized
Content-Type: application/json

{
  "timestamp": "2024-12-11T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "JWT token has expired",
  "path": "/api/operator/procurement/requests"
}
```

---

#### Scenario 3: Invalid Signature

```mermaid
sequenceDiagram
    participant Client
    participant Proc as Procurement Service
    participant JwtDecoder
    
    Client->>Proc: POST /api/operator/procurement/requests<br/>Authorization: Bearer <TAMPERED_JWT>
    Proc->>JwtDecoder: Decode and validate JWT
    JwtDecoder->>JwtDecoder: Verify signature with jwt.secret
    Note over JwtDecoder: Signature mismatch!<br/>Token may be tampered
    JwtDecoder-->>Proc: JwtException: Invalid signature
    Proc-->>Client: 401 Unauthorized<br/>{error: "Invalid token"}
```

---

#### Scenario 4: Insufficient Permissions (Role Mismatch)

```mermaid
sequenceDiagram
    participant Client
    participant Proc as Procurement Service
    participant SecurityFilter
    
    Client->>Proc: GET /api/supervisor/approvals/pending<br/>Authorization: Bearer <OPERATOR_JWT>
    Proc->>SecurityFilter: Validate JWT (valid)
    SecurityFilter->>SecurityFilter: Extract roles: [ROLE_OPERATOR]
    SecurityFilter->>SecurityFilter: Check endpoint requires:<br/>ROLE_SUPERVISOR or ROLE_ADMIN
    Note over SecurityFilter: User has ROLE_OPERATOR<br/>but needs ROLE_SUPERVISOR
    SecurityFilter-->>Client: 403 Forbidden<br/>{error: "Access Denied"}
```

**HTTP Response:**
```http
HTTP/1.1 403 Forbidden
Content-Type: application/json

{
  "timestamp": "2024-12-11T10:30:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access is denied",
  "path": "/api/supervisor/approvals/pending"
}
```

---

#### Scenario 5: Header Mismatch (Backward Compatibility Check)

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant SecurityUtils
    
    Client->>Controller: POST /api/operator/procurement/requests<br/>Authorization: Bearer <JWT><br/>X-User-Id: different-user-id
    Controller->>SecurityUtils: getUserIdFromJwt(jwt)
    SecurityUtils-->>Controller: userId: abc-123
    Controller->>SecurityUtils: validateUserIdMatch(different-user-id, jwt)
    SecurityUtils->>SecurityUtils: Compare:<br/>JWT userId: abc-123<br/>Header userId: different-user-id
    Note over SecurityUtils: Mismatch detected!
    SecurityUtils-->>Controller: SecurityException
    Controller-->>Client: 403 Forbidden<br/>{error: "User ID mismatch"}
```

---

## Security Components

### Component Architecture

```mermaid
graph TB
    subgraph "Spring Security Layer"
        A[SecurityFilterChain]
        B[JwtDecoder]
        C[JwtAuthenticationConverter]
        D[CustomJwtGrantedAuthoritiesConverter]
    end
    
    subgraph "Application Layer"
        E[Controllers]
        F[SecurityUtils]
        G[Services]
    end
    
    subgraph "Configuration"
        H[SecurityConfig]
        I[application.properties<br/>jwt.secret]
    end
    
    A --> B
    B --> C
    C --> D
    A --> E
    E --> F
    E --> G
    H --> A
    H --> B
    I --> B
    
    style A fill:#FFE4B5
    style H fill:#98FB98
    style E fill:#87CEEB
```

### Component Responsibilities

#### 1. SecurityConfig
**File**: `SecurityConfig.java`

**Responsibilities**:
- Define security filter chain
- Configure JWT decoder with shared secret
- Set up role-based endpoint protection
- Configure stateless session management

**Key Configuration**:
```java
@Bean
public JwtDecoder jwtDecoder() {
    byte[] secretBytes = Base64.getDecoder().decode(jwtSecret);
    SecretKey secretKey = new SecretKeySpec(secretBytes, "HmacSHA256");
    return NimbusJwtDecoder.withSecretKey(secretKey).build();
}
```

---

#### 2. CustomJwtGrantedAuthoritiesConverter
**File**: `CustomJwtGrantedAuthoritiesConverter.java`

**Responsibilities**:
- Extract `roles` claim from JWT
- Convert roles to Spring Security `GrantedAuthority`
- Add `ROLE_` prefix if not present

**Flow**:
```
JWT Token → Extract "roles" claim → ["OPERATOR"] 
→ Add prefix → ["ROLE_OPERATOR"] 
→ Convert to GrantedAuthority → [SimpleGrantedAuthority("ROLE_OPERATOR")]
```

---

#### 3. SecurityUtils
**File**: `SecurityUtils.java`

**Responsibilities**:
- Extract user information from JWT
- Provide helper methods for controllers
- Validate backward compatibility (X-User-Id header)

**Common Methods**:
```java
// Get current user ID
UUID userId = SecurityUtils.getCurrentUserId();

// Extract from specific JWT
UUID userId = SecurityUtils.getUserIdFromJwt(jwt);

// Check role
boolean isOperator = SecurityUtils.currentUserHasRole("OPERATOR");

// Validate header matches JWT
SecurityUtils.validateUserIdMatch(headerUserId, jwt);
```

---

## Token Lifecycle

### Complete Token Journey

```mermaid
stateDiagram-v2
    [*] --> Created: User logs in
    Created --> Active: Token issued<br/>(exp: now + 15min)
    Active --> Validated: Each API request
    Validated --> Active: Token still valid
    Active --> Expired: Time > exp
    Active --> Blacklisted: User logs out
    Expired --> [*]: Rejected (401)
    Blacklisted --> [*]: Rejected (401)
    
    note right of Active
        Token is valid and can be used
        for API requests
    end note
    
    note right of Expired
        Token expired after 15 minutes
        User must refresh or re-login
    end note
    
    note right of Blacklisted
        Token revoked in Redis
        Cannot be used even if not expired
    end note
```

### Token Refresh Flow

```mermaid
sequenceDiagram
    participant Client
    participant Auth as Auth Service
    participant Redis
    participant Proc as Procurement Service
    
    Note over Client: Access token expired
    Client->>Auth: POST /api/v1/auth/refresh<br/>{refreshToken}
    Auth->>Redis: Validate refresh token
    Redis-->>Auth: Valid
    Auth->>Auth: Generate new access token
    Auth->>Redis: Update refresh token
    Auth-->>Client: {newAccessToken, newRefreshToken}
    
    Note over Client: Use new access token
    Client->>Proc: POST /api/operator/procurement/requests<br/>Authorization: Bearer <NEW_JWT>
    Proc->>Proc: Validate new JWT
    Proc-->>Client: 201 Created
```

---

## Configuration Reference

### Required Environment Variables

```bash
# JWT Secret (MUST match auth-service)
export JWT_SECRET="verysecretkeythatshouldbechangedinproduction1234567890"

# Database
export DB_HOST=postgres
export DB_PORT=5432
export DB_NAME=procurement_db

# Redis
export REDIS_HOST=redis
export REDIS_PORT=6379

# Kafka
export KAFKA_BOOTSTRAP_SERVERS=kafka:29092
```

### application.properties

```properties
# JWT Configuration
jwt.secret=${JWT_SECRET:verysecretkeythatshouldbechangedinproduction1234567890}
jwt.issuer=auth-service

# Security
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://auth-service:8081
```

---

## Testing Authentication

### Manual Test Commands

#### 1. Get JWT Token
```bash
curl -X POST http://localhost:8081/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "operator1",
    "password": "password123"
  }'
```

#### 2. Use JWT Token
```bash
# Store token
export JWT="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

# Make authenticated request
curl -X POST http://localhost:8083/api/operator/procurement/requests \
  -H "Authorization: Bearer $JWT" \
  -H "Content-Type: application/json" \
  -d '{
    "vendorId": "uuid",
    "type": "GOODS",
    "description": "Test PR"
  }'
```

#### 3. Test Without JWT (Should Fail)
```bash
curl -X POST http://localhost:8083/api/operator/procurement/requests \
  -H "Content-Type: application/json" \
  -d '{...}'

# Expected: 401 Unauthorized
```

#### 4. Test Role Mismatch (Should Fail)
```bash
# Operator trying to access supervisor endpoint
curl -X GET http://localhost:8083/api/supervisor/approvals/pending \
  -H "Authorization: Bearer $OPERATOR_JWT"

# Expected: 403 Forbidden
```

---

## Summary

### Authentication Flow Summary

1. **User Login** → Auth-service validates credentials → Issues JWT token
2. **API Request** → Client sends JWT in Authorization header
3. **JWT Validation** → Procurement-service validates signature and expiration
4. **Role Check** → Spring Security checks if user has required role
5. **Business Logic** → Controller extracts user ID from JWT and processes request

### Security Guarantees

✅ **No unauthorized access** - All endpoints require valid JWT  
✅ **No user impersonation** - JWT signature prevents tampering  
✅ **Role-based access** - Endpoints protected by user roles  
✅ **Token expiration** - Tokens expire after 15 minutes  
✅ **Stateless** - No server-side session storage  
✅ **Backward compatible** - Supports X-User-Id header during migration  

### Key Files

- **SecurityConfig.java** - Main security configuration
- **CustomJwtGrantedAuthoritiesConverter.java** - Role extraction
- **SecurityUtils.java** - Helper utilities
- **application.properties** - JWT secret configuration
- **All Controllers** - Use `@AuthenticationPrincipal Jwt jwt` to access token
