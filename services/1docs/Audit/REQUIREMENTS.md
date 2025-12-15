# Audit Service - Requirements

## Functional Requirements
1.  **Event Ingestion**: Must consume events from Kafka seamlessly.
2.  **Filtering**: Must generally ignore "read" events (loading a page) to save space, focusing on "write" events (Create, Update, Delete) and security events (Login, Failed Login).
3.  **Searchability**: Logs must be searchable by Actor, Date, and Resource.

## Non-Functional Requirements
1.  **Immutability**: Once written, audit logs CANNOT be modified or deleted via the API.
2.  **Retention**: Logs must be kept for a minimum of 5 years (configurable).
3.  **Performance**:
    *   Write (Ingestion) must be asynchronous (Event-Driven) so it does not block the user action in the main service.
    *   Read (Dashboard) should load < 2 seconds for standard queries.
4.  **Security**:
    *   Access to the Audit Service API is strictly restricted to `ADMIN` and `AUDITOR` roles.
    *   The Audit Service itself should log who accessed the audit service (Meta-Auditing).
5.  **Reliability**: Message queue (Kafka) guarantees at-least-once delivery to ensure no audit trail is lost.
