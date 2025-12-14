# Auth Service API Reference

**Base URL**: `/api/v1`

---

## 1. Authentication (`/auth`)
Public endpoints for session management.

### Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **POST** | `/auth/login` | Authenticate user |
| **POST** | `/auth/refresh` | Get new access token |
| **POST** | `/auth/logout` | End session |
| **POST** | `/auth/reset/request` | Initial password reset (email) |
| **POST** | `/auth/reset/confirm` | Complete password reset |

### Payloads

**Login (`LoginRequest`)**
```json
{
  "username": "user",
  "password": "password"
}
```

**Response (`TokenResponse`)**
```json
{
  "accessToken": "jwt-string",
  "refreshToken": "uuid-string"
}
```

**Refresh (`RefreshRequest`)**
```json
{
  "refreshToken": "uuid-string"
}
```

---

## 2. Admin User Management (`/admin/users`)
**Auth Required**: Bearer Token
**Role**: `ADMIN`

### Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/admin/users` | List users (Paginated) |
| **POST** | `/admin/users` | Create new user |
| **PUT** | `/admin/users/{id}` | Update user details |
| **POST** | `/admin/users/{id}/deactivate` | Ban user |
| **POST** | `/admin/users/{id}/reactivate` | Unban user |

### Payloads

**Create User**
```json
{
  "username": "jdoe",
  "email": "jdoe@company.com",
  "password": "secretPassword",
  "roles": ["ROLE_OPERATOR"]
}
```

**Update User**
```json
{
  "email": "newemail@company.com",
  "roles": ["ROLE_SUPERVISOR"]
}
```

---

## 3. Vendor Registration (`/vendor`)
Public endpoint for new suppliers.

### Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **POST** | `/vendor/register` | Submit registration application |

### Content-Type: `multipart/form-data`
*   `companyName`: string
*   `npwp`: string
*   `address`: string
*   `email`: string
*   `phone`: string
*   `documents`: File[] (Optional)
