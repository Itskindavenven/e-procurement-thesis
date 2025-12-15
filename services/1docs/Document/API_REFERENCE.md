# Document Service - API Reference

Base URL: `/api/v1/documents`

## Endpoints

### 1. Upload File
Upload a single file.

*   **URL**: `/upload`
*   **Method**: `POST`
*   **Content-Type**: `multipart/form-data`
*   **Body**:
    *   `file`: (Binary)
    *   `description` (String, optional)
*   **Response (201 Created)**:
    ```json
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "filename": "invoice.pdf",
      "downloadUrl": "/api/v1/documents/550e8400...",
      "size": 2048,
      "contentType": "application/pdf"
    }
    ```

### 2. Download File
Retrieve the binary file content.

*   **URL**: `/{id}`
*   **Method**: `GET`
*   **Headers**:
    *   Accept: `application/octet-stream` (for download)
*   **Response (200 OK)**: Binary Stream

### 3. Get Metadata
Retrieve file details.

*   **URL**: `/{id}/meta`
*   **Method**: `GET`
*   **Response (200 OK)**:
    ```json
    {
      "id": "...",
      "originalFilename": "invoice.pdf",
      "createdAt": "2025-12-15T10:00:00Z",
      "uploaderId": "user-123"
    }
    ```

### 4. Delete File
Remove a file.

*   **URL**: `/{id}`
*   **Method**: `DELETE`
*   **Response (204 No Content)**
