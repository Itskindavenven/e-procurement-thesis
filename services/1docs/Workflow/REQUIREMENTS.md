# Workflow Service - Requirements

## Functional Requirements
1.  **Standard Compliance**: Must support BPMN 2.0 standard.
2.  **Versioning**: Must support multiple active versions of a process (e.g., Old PRs follow V1 logic, new PRs follow V2).
3.  **Timers**: Must support boundary timer events (e.g., "Escalate if not approved in 24h").

## Non-Functional Requirements
1.  **State Consistency**: ACID transactions are critical.
2.  **History**: Must keep historical audit trail of process execution for compliance.
3.  **Isolation**: Process variables must be isolated between instances.

## Configuration
```properties
camunda.bpm.database.schema-update=true
camunda.bpm.job-execution.enabled=true
```
