# Procurement Service - Use Case Documentation

## Overview
This document provides detailed use case specifications for all implemented features in the Procurement Service (Phases 1-2).

---

## Table of Contents
1. [Phase 1: Core PR Workflow](#phase-1-core-pr-workflow)
   - UC-01: Create Procurement Request
   - UC-02: Update Draft PR
   - UC-03: Submit PR for Approval
   - UC-04: List Procurement Requests
   - UC-05: View PR Details
   - UC-06: Delete Draft PR
   - UC-07: Review Pending Approvals
   - UC-08: Approve Procurement Request
   - UC-09: Reject Procurement Request
   - UC-10: Return PR for Revision
   - UC-11: Add Feedback to PR
2. [Phase 2: Service Procurement](#phase-2-service-procurement)
   - UC-12: List Pending Termins
   - UC-13: View Termin Details
   - UC-14: Accept Termin
   - UC-15: Request Termin Clarification
   - UC-16: Request Termin Revision
3. [System Use Cases](#system-use-cases)
   - UC-17: Auto-Reminder (24h)
   - UC-18: Auto-Escalation (48h)

---

## Phase 1: Core PR Workflow

### UC-01: Create Procurement Request

**Actor**: Operator

**Description**: Operator creates a new procurement request for goods or services

**Preconditions**:
- Operator is authenticated
- Operator has OPERATOR role

**API Endpoint**:
```
POST /api/operator/procurement/requests
Headers: X-User-Id: {operatorId}
Body: CreateProcurementRequestDTO
```

**Main Flow**:
1. Operator provides procurement details:
   - Vendor ID
   - Procurement type (GOODS/SERVICE)
   - Description
   - Priority (NORMAL/IMPORTANT/URGENT)
   - Deadline
   - Items with quantities and prices
   - Delivery details
   - Additional documents (optional)
2. System validates vendor exists and is active
3. System creates PR with status DRAFT
4. System sets operatorId, createdBy, createdAt
5. System saves PR and all related entities
6. System returns PR details

**Postconditions**:
- PR is created with status DRAFT
- PR is visible in operator's PR list
- Audit trail is recorded

**Business Rules**:
- Vendor must be active
- At least one item is required
- Total amount is calculated from items (quantity × unitPrice + tax)

**Request Example**:
```json
{
  "vendorId": "uuid",
  "type": "GOODS",
  "description": "Office supplies procurement",
  "priority": "NORMAL",
  "deadline": "2024-12-31T23:59:59",
  "items": [
    {
      "catalogItemId": "uuid",
      "itemName": "Printer Paper A4",
      "quantity": 100,
      "unitPrice": 25000,
      "taxPpn": 2500000
    }
  ],
  "deliveryDetail": {
    "deliveryAddress": "Office Building A, Floor 3",
    "locationId": "uuid",
    "plannedDeliveryDate": "2024-12-25",
    "notes": "Deliver to reception"
  }
}
```

**Response**: `ProcurementRequestResponseDTO`

**Events Published**: None (DRAFT status)

---

### UC-02: Update Draft PR

**Actor**: Operator

**Description**: Operator updates a draft procurement request

**Preconditions**:
- PR exists and status is DRAFT or RETURNED
- Operator is the owner (created the PR)

**API Endpoint**:
```
PUT /api/operator/procurement/requests/{id}
Headers: X-User-Id: {operatorId}
Body: UpdateProcurementRequestDTO
```

**Main Flow**:
1. Operator provides updated PR details
2. System validates ownership (operatorId matches)
3. System validates status is DRAFT or RETURNED
4. System updates allowed fields (null values ignored)
5. System updates updatedBy and updatedAt
6. System returns updated PR details

**Alternate Flow**:
- **2a**: Operator is not owner → Return 403 Forbidden
- **3a**: PR status is not DRAFT/RETURNED → Return 400 Bad Request

**Postconditions**:
- PR is updated
- updatedBy and updatedAt are set
- Audit trail is recorded

**Business Rules**:
- Only DRAFT or RETURNED PRs can be updated
- Only the creator can update
- Status cannot be changed via this endpoint

---

### UC-03: Submit PR for Approval

**Actor**: Operator

**Description**: Operator submits PR to supervisor for approval

**Preconditions**:
- PR exists and status is DRAFT or RETURNED
- Operator is the owner
- PR has at least one item

**API Endpoint**:
```
PUT /api/operator/procurement/requests/{id}/submit
Headers: X-User-Id: {operatorId}
```

**Main Flow**:
1. Operator submits PR for approval
2. System validates ownership
3. System validates status is DRAFT or RETURNED
4. System validates PR completeness (has items)
5. System updates status to SUBMITTED
6. System sets updatedBy and updatedAt
7. System publishes `procurement.pr.submitted` event
8. System returns updated PR details

**Alternate Flow**:
- **3a**: PR already submitted → Return 400 Bad Request
- **4a**: PR has no items → Return 400 Bad Request "Cannot submit PR without items"

**Postconditions**:
- PR status is SUBMITTED
- PR appears in supervisor's pending approval queue
- Kafka event published for notifications
- Auto-reminder timer starts (24h)

**Events Published**:
```json
{
  "eventType": "procurement.pr.submitted",
  "payload": {
    "procurementRequestId": "uuid",
    "operatorId": "uuid",
    "vendorId": "uuid",
    "type": "GOODS",
    "priority": "NORMAL",
    "totalAmount": 2525000,
    "submittedAt": "2024-12-09T10:30:00"
  }
}
```

**Event Consumers**:
- Notification Service → Send email to supervisor
- Audit Service → Record submission event

---

### UC-04: List Procurement Requests

**Actor**: Operator

**Description**: Operator views their procurement requests

**Preconditions**:
- Operator is authenticated

**API Endpoint**:
```
GET /api/operator/procurement/requests?status={status}&page={page}&size={size}
Headers: X-User-Id: {operatorId}
```

**Main Flow**:
1. Operator requests PR list (optionally filtered by status)
2. System retrieves PRs created by operator
3. System applies status filter if provided
4. System applies pagination
5. System returns PR summaries

**Query Parameters**:
- `status` (optional): Filter by ProcurementStatus
- `page` (optional, default=0): Page number
- `size` (optional, default=20): Page size
- `sort` (optional, default=createdAt,desc): Sort field and direction

**Response**: `Page<ProcurementRequestSummaryDTO>`

**Response Example**:
```json
{
  "content": [
    {
      "id": "uuid",
      "description": "Office supplies",
      "vendorName": "ABC Supplier",
      "status": "SUBMITTED",
      "totalAmount": 2525000,
      "itemCount": 1,
      "createdAt": "2024-12-09T10:00:00",
      "deadline": "2024-12-31T23:59:59"
    }
  ],
  "totalElements": 15,
  "totalPages": 1,
  "number": 0,
  "size": 20
}
```

---

### UC-05: View PR Details

**Actor**: Operator

**Description**: Operator views full details of a procurement request

**Preconditions**:
- PR exists
- Operator has access (owner or supervisor/finance)

**API Endpoint**:
```
GET /api/operator/procurement/requests/{id}
Headers: X-User-Id: {operatorId}
```

**Main Flow**:
1. Operator requests PR details
2. System retrieves PR with all relationships
3. System enriches with vendor name and location name
4. System calculates total amount from items
5. System returns complete PR details

**Response**: `ProcurementRequestResponseDTO`

**Response Includes**:
- Basic PR information
- All items with prices
- Delivery details
- Additional documents
- Approval history
- Vendor information (from Vendor Service)
- Location information (from Master Data Service)

---

### UC-06: Delete Draft PR

**Actor**: Operator

**Description**: Operator soft-deletes a draft procurement request

**Preconditions**:
- PR exists and status is DRAFT
- Operator is the owner

**API Endpoint**:
```
DELETE /api/operator/procurement/requests/{id}
Headers: X-User-Id: {operatorId}
```

**Main Flow**:
1. Operator requests PR deletion
2. System validates ownership
3. System validates status is DRAFT
4. System sets isDeleted=true and deletedAt=now
5. System sets updatedBy
6. System confirms deletion

**Alternate Flow**:
- **3a**: PR is not DRAFT → Return 400 "Only draft PRs can be deleted"

**Postconditions**:
- PR is soft-deleted (isDeleted=true)
- PR no longer appears in operator's list
- PR data is preserved in database
- Audit trail is recorded

**Business Rules**:
- Only DRAFT PRs can be deleted
- Soft delete pattern is used (data retained)
- Only owner can delete

---

### UC-07: Review Pending Approvals

**Actor**: Supervisor

**Description**: Supervisor views list of PRs awaiting approval

**Preconditions**:
- Supervisor is authenticated
- Supervisor has SUPERVISOR role

**API Endpoint**:
```
GET /api/supervisor/approvals/pending?page={page}&size={size}
Headers: X-User-Id: {supervisorId}
```

**Main Flow**:
1. Supervisor requests pending approvals
2. System retrieves PRs with status SUBMITTED or ESCALATED
3. System calculates days waiting for each PR
4. System applies pagination
5. System returns PR summaries

**Response**: `Page<PendingApprovalDTO>`

**Response Example**:
```json
{
  "content": [
    {
      "procurementRequestId": "uuid",
      "description": "Office supplies",
      "type": "GOODS",
      "priority": "NORMAL",
      "submittedAt": "2024-12-09T10:30:00",
      "deadline": "2024-12-31T23:59:59",
      "daysWaiting": 2
    }
  ],
  "totalElements": 5,
  "totalPages": 1
}
```

**Business Rules**:
- Only SUBMITTED and ESCALATED PRs appear
- Sorted by submission date (oldest first)
- Days waiting calculated from submittedAt

---

### UC-08: Approve Procurement Request

**Actor**: Supervisor

**Description**: Supervisor approves a submitted procurement request

**Preconditions**:
- PR exists and status is SUBMITTED or ESCALATED
- Supervisor is authenticated

**API Endpoint**:
```
PUT /api/supervisor/approvals/pr/{prId}/approve
Headers: X-User-Id: {supervisorId}
Body: ApprovalRequestDTO
```

**Main Flow**:
1. Supervisor provides approval decision with optional notes
2. System validates PR status is SUBMITTED or ESCALATED
3. System validates decision is APPROVE
4. System creates ApprovalHistory record
5. System updates PR status to APPROVED
6. System sets updatedBy and updatedAt
7. System publishes `procurement.pr.approved` event
8. System returns approval confirmation

**Request Example**:
```json
{
  "decision": "APPROVE",
  "note": "Approved. Good pricing."
}
```

**Alternate Flow**:
- **2a**: PR not in reviewable status → Return 400 "Only submitted/escalated PRs can be reviewed"

**Postconditions**:
- PR status is APPROVED
- ApprovalHistory record created
- Kafka event published
- PO creation triggered (future phase)

**Events Published**:
```json
{
  "eventType": "procurement.pr.approved",
  "payload": {
    "procurementRequestId": "uuid",
    "supervisorId": "uuid",
    "approvedAt": "2024-12-09T14:30:00",
    "note": "Approved. Good pricing."
  }
}
```

**Event Consumers**:
- PO Service → Create Purchase Order
- Notification Service → Notify operator
- Audit Service → Record approval

**Response**: `ApprovalResponseDTO`

---

### UC-09: Reject Procurement Request

**Actor**: Supervisor

**Description**: Supervisor rejects a procurement request

**Preconditions**:
- PR exists and status is SUBMITTED or ESCALATED
- Rejection reason is provided

**API Endpoint**:
```
PUT /api/supervisor/approvals/pr/{prId}/reject
Headers: X-User-Id: {supervisorId}
Body: ApprovalRequestDTO
```

**Main Flow**:
1. Supervisor provides rejection with reason
2. System validates rejection reason is provided
3. System validates PR status
4. System creates ApprovalHistory record
5. System updates PR status to REJECTED
6. System publishes `procurement.pr.rejected` event
7. System returns rejection confirmation

**Request Example**:
```json
{
  "decision": "REJECT",
  "note": "Pricing too high, please renegotiate with vendor"
}
```

**Alternate Flow**:
- **2a**: No rejection reason → Return 400 "Rejection reason is required"

**Postconditions**:
- PR status is REJECTED
- ApprovalHistory record created
- Operator is notified
- PR workflow ends

**Events Published**:
```json
{
  "eventType": "procurement.pr.rejected",
  "payload": {
    "procurementRequestId": "uuid",
    "supervisorId": "uuid",
    "rejectedAt": "2024-12-09T14:30:00",
    "reason": "Pricing too high..."
  }
}
```

---

### UC-10: Return PR for Revision

**Actor**: Supervisor

**Description**: Supervisor returns PR to operator for revision

**Preconditions**:
- PR exists and status is SUBMITTED or ESCALATED
- Return reason is provided

**API Endpoint**:
```
PUT /api/supervisor/approvals/pr/{prId}/return
Headers: X-User-Id: {supervisorId}
Body: ApprovalRequestDTO
```

**Main Flow**:
1. Supervisor provides return reason
2. System validates return reason is provided
3. System validates PR status
4. System creates ApprovalHistory record
5. System updates PR status to RETURNED
6. System publishes `procurement.pr.returned` event
7. System returns confirmation

**Request Example**:
```json
{
  "decision": "RETURN",
  "note": "Please add detailed specifications for item #2"
}
```

**Postconditions**:
- PR status is RETURNED
- Operator can update and resubmit PR
- ApprovalHistory record created

**Events Published**:
```json
{
  "eventType": "procurement.pr.returned",
  "payload": {
    "procurementRequestId": "uuid",
    "supervisorId": "uuid",
    "returnedAt": "2024-12-09T14:30:00",
    "reason": "Please add detailed specifications..."
  }
}
```

**Business Rules**:
- Operator can update the PR (UC-02)
- Operator must resubmit for approval (UC-03)
- Approval history is preserved

---

### UC-11: Add Feedback to PR

**Actor**: Supervisor

**Description**: Supervisor adds feedback/comments without changing PR status

**Preconditions**:
- PR exists
- PR status is not REJECTED or CANCELLED

**API Endpoint**:
```
PUT /api/supervisor/approvals/pr/{prId}/feedback
Headers: X-User-Id: {supervisorId}
Body: ApprovalRequestDTO
```

**Main Flow**:
1. Supervisor provides feedback notes
2. System validates PR status allows feedback
3. System creates ApprovalHistory record with FEEDBACK_ONLY decision
4. System does NOT change PR status
5. System returns confirmation

**Request Example**:
```json
{
  "decision": "FEEDBACK_ONLY",
  "note": "Please consider eco-friendly alternatives"
}
```

**Postconditions**:
- ApprovalHistory record created
- PR status unchanged
- Feedback visible to operator

**Business Rules**:
- Status does not change
- Multiple feedback entries allowed
- Cannot add feedback to rejected/cancelled PRs

---

## Phase 2: Service Procurement

### UC-12: List Pending Termins

**Actor**: Operator

**Description**: Operator views list of service termins awaiting review

**Preconditions**:
- Operator is authenticated
- Service PRs have been approved
- Vendor has submitted termins

**API Endpoint**:
```
GET /api/operator/service/termins?page={page}&size={size}
Headers: X-User-Id: {operatorId}
```

**Main Flow**:
1. Operator requests pending termins
2. System retrieves termins with status SUBMITTED_BY_VENDOR
3. System calculates days waiting for each
4. System applies pagination
5. System returns termin summaries

**Response**: `Page<TerminSummaryDTO>`

**Response Example**:
```json
{
  "content": [
    {
      "id": "uuid",
      "procurementRequestId": "uuid",
      "prDescription": "Website Development",
      "terminNumber": 1,
      "phaseName": "Initial Design",
      "value": 50000000,
      "status": "SUBMITTED_BY_VENDOR",
      "vendorSubmittedAt": "2024-12-08T10:00:00",
      "daysWaiting": 1
    }
  ],
  "totalElements": 3,
  "totalPages": 1
}
```

---

### UC-13: View Termin Details

**Actor**: Operator

**Description**: Operator views full details of a service termin

**Preconditions**:
- Termin exists
- Operator has access rights

**API Endpoint**:
```
GET /api/operator/service/termins/{id}
Headers: X-User-Id: {operatorId}
```

**Main Flow**:
1. Operator requests termin details
2. System retrieves termin with related PR
3. System returns complete termin information

**Response**: `TerminDetailsResponseDTO`

**Response Includes**:
- Termin number and phase name
- Payment value
- Deliverables description
- Current status
- All timestamps (vendor submit, operator review, supervisor decide)
- All notes (clarification, revision, operator, supervisor)
- Related PR information

---

### UC-14: Accept Termin

**Actor**: Operator

**Description**: Operator accepts a termin and forwards to supervisor for payment approval

**Preconditions**:
- Termin exists and status is SUBMITTED_BY_VENDOR
- Operator has reviewed deliverables

**API Endpoint**:
```
PUT /api/operator/service/termins/{id}/accept
Headers: X-User-Id: {operatorId}
Body: AcceptTerminDTO
```

**Main Flow**:
1. Operator provides acceptance with review notes
2. System validates termin status is SUBMITTED_BY_VENDOR
3. System updates status to REVIEWED_BY_OPERATOR
4. System sets operatorReviewNotes, operatorReviewedAt, reviewedBy
5. System publishes termin reviewed event
6. System returns confirmation

**Request Example**:
```json
{
  "reviewNotes": "Deliverables match requirements. Design approved."
}
```

**Postconditions**:
- Termin status is REVIEWED_BY_OPERATOR
- Termin forwarded to supervisor for payment approval
- Operator review recorded
- Event published for notification

**Events Published**: `procurement.termin.reviewed`

**Response**: `TerminActionResponseDTO`

---

### UC-15: Request Termin Clarification

**Actor**: Operator

**Description**: Operator requests clarification from vendor on termin details

**Preconditions**:
- Termin exists and status is SUBMITTED_BY_VENDOR
- Clarification is needed

**API Endpoint**:
```
PUT /api/operator/service/termins/{id}/clarify
Headers: X-User-Id: {operatorId}
Body: RequestClarificationDTO
```

**Main Flow**:
1. Operator provides clarification request
2. System validates termin status
3. System updates status to CLARIFICATION_REQUESTED
4. System sets clarificationNotes, operatorReviewedAt, reviewedBy
5. System publishes clarification requested event
6. System returns confirmation

**Request Example**:
```json
{
  "clarificationNotes": "Please provide detailed breakdown of Phase 1 deliverables"
}
```

**Postconditions**:
- Termin status is CLARIFICATION_REQUESTED
- Vendor is notified to provide clarification
- Termin remains in operator's review queue

**Events Published**: `procurement.termin.clarification.requested`

**Business Rules**:
- Vendor must respond with clarification
- Termin returns to SUBMITTED_BY_VENDOR after vendor responds
- Operator can review again

---

### UC-16: Request Termin Revision

**Actor**: Operator

**Description**: Operator requests revision of termin from vendor

**Preconditions**:
- Termin exists and status is SUBMITTED_BY_VENDOR
- Revisions are needed

**API Endpoint**:
```
PUT /api/operator/service/termins/{id}/revise
Headers: X-User-Id: {operatorId}
Body: RequestRevisionDTO
```

**Main Flow**:
1. Operator provides revision request details
2. System validates termin status
3. System updates status to REVISION_REQUESTED
4. System sets revisionNotes, operatorReviewedAt, reviewedBy
5. System publishes revision requested event
6. System returns confirmation

**Request Example**:
```json
{
  "revisionNotes": "Deliverables do not match Phase 1 scope. Please revise to include wireframes."
}
```

**Postconditions**:
- Termin status is REVISION_REQUESTED
- Vendor is notified to revise and resubmit
- Original termin data preserved

**Events Published**: `procurement.termin.revision.requested`

**Business Rules**:
- Vendor must revise and resubmit
- Termin returns to SUBMITTED_BY_VENDOR after resubmission
- Revision history is tracked

---

## System Use Cases

### UC-17: Auto-Reminder (24h)

**Actor**: System (Scheduled Task)

**Description**: System sends reminder if PR not reviewed within 24 hours

**Trigger**: Cron schedule (hourly)

**Preconditions**:
- PR exists with status SUBMITTED
- 24 hours have passed since submission
- Less than 48 hours have passed

**Main Flow**:
1. System checks for SUBMITTED PRs older than 24h and younger than 48h
2. For each PR found:
   - System creates reminder event
   - System publishes `procurement.pr.reminder` event
3. System logs reminder count

**Business Rules**:
- Only runs for PRs between 24-48 hours old
- Runs hourly (cron: `0 0 * * * *`)
- Does NOT change PR status

**Events Published**:
```json
{
  "eventType": "procurement.pr.reminder",
  "payload": {
    "procurementRequestId": "uuid",
    "supervisorId": "uuid",
    "submittedAt": "2024-12-08T10:30:00",
    "hoursWaiting": 26
  }
}
```

**Event Consumers**:
- Notification Service → Send reminder email to supervisor

---

### UC-18: Auto-Escalation (48h)

**Actor**: System (Scheduled Task)

**Description**: System escalates PR if not reviewed within 48 hours

**Trigger**: Cron schedule (hourly)

**Preconditions**:
- PR exists with status SUBMITTED
- 48 hours have passed since submission

**Main Flow**:
1. System checks for SUBMITTED PRs older than 48h
2. For each PR found:
   - System updates status to ESCALATED
   - System sets updatedAt
   - System publishes `procurement.pr.escalated` event
3. System logs escalation count

**Business Rules**:
- Only runs for PRs older than 48 hours
- Changes status from SUBMITTED to ESCALATED
- Runs hourly (cron: `0 0 * * * *`)
- Escalated PRs still appear in supervisor queue

**Events Published**:
```json
{
  "eventType": "procurement.pr.escalated",
  "payload": {
    "procurementRequestId": "uuid",
    "supervisorId": "uuid",
    "submittedAt": "2024-12-07T10:30:00",
    "escalatedAt": "2024-12-09T11:00:00"
  }
}
```

**Event Consumers**:
- Notification Service → Send escalation email to manager
- Audit Service → Record escalation

---

## Event Flow Summary

### PR Submission Flow
```
Operator submits PR
  → procurement.pr.submitted event
    → Notification Service sends email to supervisor
    → Audit Service records submission
  
24h later (if not reviewed)
  → procurement.pr.reminder event
    → Notification Service sends reminder to supervisor

48h later (if not reviewed)
  → Status changed to ESCALATED
  → procurement.pr.escalated event
    → Notification Service sends escalation to manager
```

### PR Approval Flow
```
Supervisor approves PR
  → procurement.pr.approved event
    → PO Service creates Purchase Order
    → Notification Service notifies operator
    → Audit Service records approval
```

### Termin Review Flow
```
Vendor submits termin
  → Status: SUBMITTED_BY_VENDOR

Operator accepts
  → Status: REVIEWED_BY_OPERATOR
  → procurement.termin.reviewed event
    → Notification Service notifies supervisor for payment approval

OR

Operator requests clarification
  → Status: CLARIFICATION_REQUESTED
  → procurement.termin.clarification.requested event
    → Notification Service notifies vendor

OR

Operator requests revision
  → Status: REVISION_REQUESTED
  → procurement.termin.revision.requested event
    → Notification Service notifies vendor
```

---

## API Summary

### Operator Endpoints (11)
| Method | Endpoint | Use Case |
|--------|----------|----------|
| POST | `/api/operator/procurement/requests` | UC-01: Create PR |
| PUT | `/api/operator/procurement/requests/{id}` | UC-02: Update PR |
| PUT | `/api/operator/procurement/requests/{id}/submit` | UC-03: Submit PR |
| GET | `/api/operator/procurement/requests` | UC-04: List PRs |
| GET | `/api/operator/procurement/requests/{id}` | UC-05: View PR |
| DELETE | `/api/operator/procurement/requests/{id}` | UC-06: Delete PR |
| GET | `/api/operator/service/termins` | UC-12: List Termins |
| GET | `/api/operator/service/termins/{id}` | UC-13: View Termin |
| PUT | `/api/operator/service/termins/{id}/accept` | UC-14: Accept Termin |
| PUT | `/api/operator/service/termins/{id}/clarify` | UC-15: Request Clarification |
| PUT | `/api/operator/service/termins/{id}/revise` | UC-16: Request Revision |

### Supervisor Endpoints (6)
| Method | Endpoint | Use Case |
|--------|----------|----------|
| GET | `/api/supervisor/approvals/pending` | UC-07: List Pending |
| GET | `/api/supervisor/approvals/pr/{prId}` | UC-05: View PR |
| PUT | `/api/supervisor/approvals/pr/{prId}/approve` | UC-08: Approve |
| PUT | `/api/supervisor/approvals/pr/{prId}/reject` | UC-09: Reject |
| PUT | `/api/supervisor/approvals/pr/{prId}/return` | UC-10: Return |
| PUT | `/api/supervisor/approvals/pr/{prId}/feedback` | UC-11: Feedback |

### System (Scheduled)
- Auto-Reminder: UC-17
- Auto-Escalation: UC-18

---

## Status Transitions

### ProcurementRequest Status Flow
```
DRAFT
  ↓ (UC-03: Submit)
SUBMITTED
  ↓ (UC-17: 24h elapsed)
SUBMITTED (reminder sent)
  ↓ (UC-18: 48h elapsed)
ESCALATED
  ↓ (UC-08: Approve / UC-09: Reject / UC-10: Return)
APPROVED / REJECTED / RETURNED
  
RETURNED → UC-02: Update → UC-03: Submit → SUBMITTED
```

### TerminDetails Status Flow
```
SUBMITTED_BY_VENDOR
  ↓ (UC-14: Accept)
REVIEWED_BY_OPERATOR
  
OR

SUBMITTED_BY_VENDOR
  ↓ (UC-15: Clarify)
CLARIFICATION_REQUESTED → Vendor responds → SUBMITTED_BY_VENDOR
  
OR

SUBMITTED_BY_VENDOR
  ↓ (UC-16: Revise)
REVISION_REQUESTED → Vendor resubmits → SUBMITTED_BY_VENDOR
```

---

## Business Rules Summary

1. **PR Creation**: Vendor must be active, at least one item required
2. **PR Update**: Only DRAFT or RETURNED status, owner only
3. **PR Submit**: Must have items, only DRAFT or RETURNED
4. **PR Delete**: Only DRAFT status, soft delete
5. **Approval**: Only SUBMITTED or ESCALATED PRs
6. **Rejection**: Reason required
7. **Return**: Reason required, allows resubmission
8. **Feedback**: Status unchanged, multiple allowed
9. **Auto-Reminder**: 24h after submission, hourly check
10. **Auto-Escalation**: 48h after submission, status changes
11. **Termin Accept**: Forwards to supervisor for payment
12. **Termin Clarify/Revise**: Returns to vendor, preserves history

---

## Integration Points

### External Services
1. **Vendor Service** (port 8081)
   - Validate vendor active status
   - Retrieve vendor details

2. **Notification Service** (port 8084)
   - Send emails for all events
   - Support event types: PR submitted/approved/rejected/returned/reminder/escalated
   - Support termin review events

3. **Audit Service** (port 8085)
   - Record all state changes
   - Track approval decisions

4. **Master Data Service**
   - Retrieve location information
   - Retrieve catalog item details

### Kafka Topics
- `procurement.pr.submitted`
- `procurement.pr.approved`
- `procurement.pr.rejected`
- `procurement.pr.returned`
- `procurement.pr.reminder`
- `procurement.pr.escalated`
- `procurement.termin.reviewed`
- `procurement.termin.clarification.requested`
- `procurement.termin.revision.requested`

---

## Error Handling

All endpoints follow standardized error response format:

```json
{
  "timestamp": "2024-12-09T14:30:00",
  "status": 400,
  "errorCode": "VALIDATION_ERROR",
  "message": "Cannot submit PR without items"
}
```

### Common Error Codes
- `RESOURCE_NOT_FOUND` (404): Entity not found
- `VALIDATION_ERROR` (400): Business rule violation
- `UNAUTHORIZED` (403): Access denied
- `CONFLICT` (409): Concurrent modification

---

## Testing Scenarios

### Happy Path
1. Create PR (DRAFT) → Update → Submit → Approve → PO Created
2. Create PR → Submit → Return → Update → Resubmit → Approve

### Alternate Paths
1. Create PR → Submit → Reject (End)
2. Create PR → Delete (End)
3. Create PR → Submit → 48h passes → Escalate → Approve

### Service Procurement
1. Approve Service PR → Vendor Submit Termin → Accept → Payment
2. Vendor Submit Termin → Request Clarification → Vendor Responds → Accept
3. Vendor Submit Termin → Request Revision → Vendor Resubmits → Accept

---

*Document Version: 1.0*
*Last Updated: 2024-12-10*
*Status: Production (Phases 1-2 Implemented)*
