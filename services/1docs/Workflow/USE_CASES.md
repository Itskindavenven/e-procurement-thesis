# Workflow Service - Use Cases

## Overview
The Workflow Service manages business process lifecycles (BPM), such as Procurement Approvals, Vendor Onboarding, and Contract Sign-offs. It manages state, assignment, and escalation.

## Actors
*   **System**: Starts a process (e.g., "New PR Created").
*   **Approver (Supervisor/Finance)**: Reviews and acts on tasks.

## Use Case List

| ID | Title | Actor | Complexity |
| :--- | :--- | :--- | :--- |
| UC-WF-001 | Start Workflow Process | System | High |
| UC-WF-002 | Get Pending Tasks | User | Medium |
| UC-WF-003 | Approve/Reject Task | User | Medium |
| UC-WF-004 | View Process Status | User/System | Low |

---

## Detailed Use Cases

### UC-WF-001: Start Workflow Process
*   **Description**: Instantiate a new process instance.
*   **Trigger**: Procurement Request submitted.
*   **Flow**:
    1.  Procurement Service calls `POST /process/start`.
    2.  Workflow Service loads definition (`PR_APPROVAL_V1`).
    3.  Service creates instance and assigns initial task to "Supervisor".
    4.  Service returns `processInstanceId`.

### UC-WF-003: Approve/Reject Task
*   **Description**: Complete a user task.
*   **Trigger**: Supervisor clicks "Approve".
*   **Flow**:
    1.  User calls `POST /tasks/{id}/complete` with variables (`decision=APPROVED`).
    2.  Workflow Engine evaluates next step (Gateway).
    3.  If Approved -> Move to next task or End Event.
    4.  If Rejected -> Trigger "Notify Rejection" service task.
