# Notification Service - API Reference

Base URL: `/api/v1/notifications`

## Endpoints

### 1. Send Notification (Internal)
Trigger a notification manually via REST.

*   **URL**: `/send`
*   **Method**: `POST`
*   **Permissions**: `Service-to-Service` (Internal Only)
*   **Body**:
    ```json
    {
      "recipientId": "user-uuid",
      "templateCode": "WELCOME_EMAIL",
      "variables": {
        "name": "John Doe"
      },
      "channels": ["EMAIL", "IN_APP"]
    }
    ```

### 2. Get My Notifications
Retrieve in-app notifications for the logged-in user.

*   **URL**: `/me`
*   **Method**: `GET`
*   **Query Params**: `unreadOnly=true` (optional)
*   **Response**:
    ```json
    [
      {
        "id": "123",
        "title": "Welcome",
        "message": "Welcome to e-Procurement",
        "read": false,
        "createdAt": "..."
      }
    ]
    ```

### 3. Mark as Read
Mark a specific notification as read.

*   **URL**: `/{id}/read`
*   **Method**: `PUT`

### 4. Mark All as Read
Mark all pending notifications as read.

*   **URL**: `/me/read-all`
*   **Method**: `PUT`
