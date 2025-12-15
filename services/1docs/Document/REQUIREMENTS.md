# Document Service - Requirements

## Functional Requirements
1.  **Multiple Backends**: Must support swappable storage backends (Local vs S3) via configuration.
2.  **Validation**:
    *   Must validate file extensions against allowed list.
    *   Must validate "Magic Numbers" (file signature) to prevent MIME spoofing.
3.  **Security**:
    *   Files must not be executable.
    *   Filenames must be sanitized to prevent directory traversal attacks.

## Non-Functional Requirements
1.  **Size Limit**: Maximum file size 10MB (configurable).
2.  **Scalability**: Stateless service; storage handling delegates to robust backend (S3/Filesystem).
3.  **Availability**: 99.9% uptime.

## Configuration Keys
```properties
# application.properties
document.storage.type=LOCAL # or S3
document.storage.local.path=/data/uploads
document.max-file-size=10MB
document.allowed-types=application/pdf,image/png,image/jpeg
```
