# Audit Service - API Reference

Base URL: `/api/v1/audit`

## Endpoints

### 1. Get Audit Logs
Retrieve a paginated list of audit logs based on filters.

*   **URL**: `/logs`
*   **Method**: `GET`
*   **Permissions**: `ROLE_ADMIN`, `ROLE_AUDITOR`
*   **Query Parameters**:
    *   `page` (int, default: 0)
    *   `size` (int, default: 20)
    *   `from` (date-time, optional)
    *   `to` (date-time, optional)
    *   `actorId` (string, optional)
    *   `module` (string, optional)
    *   `action` (string, optional)
    *   `resourceId` (string, optional)

*   **Response (200 OK)**:
    ```json
    {
      "content": [
        {
          "id": "550e8400-e29b-41d4-a716-446655440000",
          "timestamp": "2025-12-15T10:00:00Z",
          "actorName": "John Doe",
          "action": "APPROVE_PO",
          "module": "PROCUREMENT",
          "resourceId": "PO-2025-001",
          "summary": "Approved Purchase Order PO-2025-001"
        }
      ],
      "totalPages": 5,
      "totalElements": 100
    }
    ```

### 2. Get Audit Log Details
Retrieve full details of a specific audit entry, including the "before" and "after" state changes.

*   **URL**: `/logs/{id}`
*   **Method**: `GET`
*   **Permissions**: `ROLE_ADMIN`, `ROLE_AUDITOR`

*   **Response (200 OK)**:
    ```json
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "timestamp": "2025-12-15T10:00:00Z",
      "actorId": "user-123",
      "actorName": "John Doe",
      "action": "UPDATE_PROFILE",
      "resourceType": "VENDOR",
      "changes": [
        {
          "field": "address",
          "old": "123 Old St",
          "new": "456 New Ave"
        }
      ]
    }
    ```

### 3. Export Logs
Download a report of audit logs.

*   **URL**: `/export`
*   **Method**: `GET`
*   **Permissions**: `ROLE_ADMIN`
*   **Query Parameters**: Same as "Get Audit Logs"
*   **Format**: Returns a generic byte stream (PDF/CSV/Excel).
*   **Headers**: `Content-Disposition: attachment; filename="audit_report.csv"`
