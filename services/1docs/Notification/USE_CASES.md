# Notification Service - Use Cases

## Overview
The Notification Service is responsible for delivering messages to users via various channels (Email, In-App, SMS - future). It decouples business logic from delivery mechanisms.

## Actors
*   **System Services**: Trigger notifications (e.g., Procurement Service sends "Approval Needed").
*   **User**: Receives notifications and manages preferences.

## Use Case List

| ID | Title | Actor | Complexity |
| :--- | :--- | :--- | :--- |
| UC-NOTIF-001 | Send Notification (Internal) | System | Low |
| UC-NOTIF-002 | View My Notifications | User | Low |
| UC-NOTIF-003 | Mark Notification Read | User | Low |
| UC-NOTIF-004 | Manage Notification Preferences | User | Medium |

---

## Detailed Use Cases

### UC-NOTIF-001: Send Notification (Internal)
*   **Description**: A system service requests a notification be sent.
*   **Trigger**: Business Event (e.g., `PO_APPROVED`).
*   **Flow**:
    1.  Upstream service publishes event `notification.send_request` OR calls API.
    2.  Notification Service validates the recipient.
    3.  Service hydrates the template (e.g., `email-po-approved.html`) with data.
    4.  Service dispatches via configured channels (Email + In-App).
    5.  Service logs delivery status.
    
### UC-NOTIF-002: View My Notifications
*   **Description**: User views their in-app notification inbox.
*   **Trigger**: User logs into the dashboard.
*   **Flow**:
    1.  User requests `GET /notifications`.
    2.  System returns list of unread/recent notifications.

### UC-NOTIF-003: Mark Notification Read
*   **Description**: User acknowledges a notification.
*   **Flow**:
    1.  User clicks "Mark as Read" or opens the notification.
    2.  System updates status in DB.

### UC-NOTIF-004: Manage Preferences
*   **Description**: User decides which notifications to receive.
*   **Flow**:
    1.  User updates settings (e.g., "Email: OFF for Login alerts", "Email: ON for PO Approvals").
    2.  System typically defaults to "ALL ON" if no preferences exist.
