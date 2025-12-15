# Document Service - Use Cases

## Overview
The Document Service handles the storage, retrieval, and management of files (attachments) for the e-Procurement system. It abstracts the underlying storage mechanism (Local, S3, MinIO).

## Actors
*   **User/Vendor**: Uploads documents (e.g., invoices, delivery notes).
*   **System**: Retrieves documents for processing or display.
*   **Admin**: Manages file policies (optional).

## Use Case List

| ID | Title | Actor | Complexity |
| :--- | :--- | :--- | :--- |
| UC-DOC-001 | Upload Document | User | Low |
| UC-DOC-002 | Download Document | User/System | Low |
| UC-DOC-003 | Delete Document | User/System | Low |
| UC-DOC-004 | Get Document Metadata | User/System | Low |

---

## Detailed Use Cases

### UC-DOC-001: Upload Document
*   **Description**: Upload a file to the system.
*   **Trigger**: User attaches a file in a form (e.g., "Upload Invoice").
*   **Flow**:
    1.  User selects a file.
    2.  System validates file size and type (MIME).
    3.  System saves the file to the storage backend.
    4.  System generates a unique, secure filename/ID.
    5.  System stores metadata (original name, size, type, uploader ID) in the database.
    6.  System returns the `fileId` and `downloadUrl`.

### UC-DOC-002: Download Document
*   **Description**: Retrieve a file.
*   **Trigger**: User clicks a link to view a document.
*   **Flow**:
    1.  User requests file by `fileId`.
    2.  System checks permissions (via Auth Service or token).
    3.  System streams the file content to the response.

### UC-DOC-003: Delete Document
*   **Description**: Remove a file.
*   **Trigger**: User removes an attachment or entity is deleted.
*   **Flow**:
    1.  User requests deletion of `fileId`.
    2.  System marks metadata as "deleted" (Soft Delete) or permanently removes file from storage (Hard Delete, requires config).

### UC-DOC-004: Get Document Metadata
*   **Description**: Retrieve file info without downloading.
*   **Flow**:
    1.  User requests metadata for `fileId`.
    2.  System returns JSON (Size, Type, CreatedAt).
