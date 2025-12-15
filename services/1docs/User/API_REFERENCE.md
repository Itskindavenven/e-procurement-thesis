# User Service - API Reference

Base URL: `/api/v1/users`

## Endpoints

### 1. Get Users
*   **URL**: `/`
*   **Method**: `GET`
*   **Params**: `page`, `role`, `department`
*   **Response**: Paginated List of Users.

### 2. Create User (Internal)
*   **URL**: `/`
*   **Method**: `POST`
*   **Body**:
    ```json
    {
      "email": "janedoe@company.com",
      "fullName": "Jane Doe",
      "roles": ["ROLE_OPERATOR"],
      "departmentId": "dept-123"
    }
    ```

### 3. Get User Profile
*   **URL**: `/{id}`
*   **Method**: `GET`
*   **Response**: Full profile details + Role info.

### 4. Update Status
*   **URL**: `/{id}/status`
*   **Method**: `PUT`
*   **Body**: `{"status": "INACTIVE"}`
