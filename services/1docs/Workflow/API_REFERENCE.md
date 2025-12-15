# Workflow Service - API Reference

Base URL: `/api/v1/workflow`

## Endpoints

### 1. Start Process
*   **URL**: `/process-definition/{key}/start`
*   **Method**: `POST`
*   **Body**:
    ```json
    {
      "businessKey": "PR-2025-001",
      "variables": {
        "amount": 50000,
        "initiator": "user-1"
      }
    }
    ```
*   **Response**: `{"id": "process-instance-uuid"}`

### 2. Get My Tasks
*   **URL**: `/tasks`
*   **Method**: `GET`
*   **Query**: `assignee=user-1`

### 3. Complete Task
*   **URL**: `/tasks/{taskId}/complete`
*   **Method**: `POST`
*   **Body**:
    ```json
    {
      "variables": {
        "approved": true,
        "comment": "Good to go"
      }
    }
    ```

### 4. Deploy Definition
*   **URL**: `/deployment/create`
*   **Method**: `POST`
*   **Content-Type**: `multipart/form-data` (BPMN file)
