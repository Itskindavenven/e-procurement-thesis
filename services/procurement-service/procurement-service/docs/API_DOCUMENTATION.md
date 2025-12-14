# Procurement Service - API Documentation

## Overview
Complete REST API documentation for the Procurement Service with 29 endpoints across 5 functional phases.

**Base URL**: `http://localhost:8083`
**Swagger UI**: `http://localhost:8083/swagger-ui.html`

---

## Table of Contents
1. [Operator - Procurement Requests (6 endpoints)](#operator-procurement-requests)
2. [Operator - Service Termins (5 endpoints)](#operator-service-termins)
3. [Operator - Receiving (6 endpoints)](#operator-receiving)
4. [Operator - Dashboard (3 endpoints)](#operator-dashboard)
5. [Supervisor - Approvals (6 endpoints)](#supervisor-approvals)
6. [Supervisor - Dashboard (1 endpoint)](#supervisor-dashboard)
7. [Audit & Activity (2 endpoints)](#audit-activity)

---

## Operator - Procurement Requests

### 1. Create PR
```http
POST /api/operator/procurement/requests
Headers: X-User-Id: {uuid}
```

**Request Body**:
```json
{
  "vendorId": "uuid",
  "type": "GOODS",
  "description": "Office supplies",
  "priority": "NORMAL",
  "deadline": "2024-12-31T23:59:59",
  "items": [{
    "catalogItemId": "uuid",
    "itemName": "Printer Paper",
    "quantity": 100,
    "unitPrice": 25000,
    "taxPpn": 250000
  }],
  "deliveryDetail": {
    "deliveryAddress": "Office A",
    "locationId": "uuid",
    "plannedDeliveryDate": "2024-12-25"
  }
}
```

### 2. Update PR
```http
PUT /api/operator/procurement/requests/{id}
Headers: X-User-Id: {uuid}
```

### 3. Submit PR
```http
PUT /api/operator/procurement/requests/{id}/submit
Headers: X-User-Id: {uuid}
```

### 4. List PRs
```http
GET /api/operator/procurement/requests?status=DRAFT&page=0&size=20
Headers: X-User-Id: {uuid}
```

### 5. Get PR Details
```http
GET /api/operator/procurement/requests/{id}
Headers: X-User-Id: {uuid}
```

### 6. Delete PR
```http
DELETE /api/operator/procurement/requests/{id}
Headers: X-User-Id: {uuid}
```

---

## Operator - Service Termins

### 1. List Pending Termins
```http
GET /api/operator/service/termins?page=0&size=20
Headers: X-User-Id: {uuid}
```

### 2. Get Termin Details
```http
GET /api/operator/service/termins/{id}
Headers: X-User-Id: {uuid}
```

### 3. Accept Termin
```http
PUT /api/operator/service/termins/{id}/accept
Headers: X-User-Id: {uuid}
Body: { "reviewNotes": "Approved" }
```

### 4. Request Clarification
```http
PUT /api/operator/service/termins/{id}/clarify
Headers: X-User-Id: {uuid}
Body: { "clarificationNotes": "Please provide details" }
```

### 5. Request Revision
```http
PUT /api/operator/service/termins/{id}/revise
Headers: X-User-Id: {uuid}
Body: { "revisionNotes": "Please update deliverables" }
```

---

## Operator - Receiving

### 1. List POs Awaiting Receiving
```http
GET /api/operator/receiving/pos?page=0&size=20
Headers: X-User-Id: {uuid}
```

### 2. Get PO Details
```http
GET /api/operator/receiving/pos/{id}
Headers: X-User-Id: {uuid}
```

### 3. Accept Full Delivery
```http
POST /api/operator/receiving/pos/{id}/accept
Headers: X-User-Id: {uuid}
Body: { "condition": "GOOD", "notes": "All items received" }
```

### 4. Accept Partial Delivery
```http
POST /api/operator/receiving/pos/{id}/partial
Headers: X-User-Id: {uuid}
Body: { "receivedQuantity": 50, "condition": "GOOD", "notes": "Partial" }
```

### 5. Reject Delivery
```http
POST /api/operator/receiving/pos/{id}/reject
Headers: X-User-Id: {uuid}
Body: { "rejectionReason": "Damaged items", "notes": "..." }
```

### 6. Return Goods
```http
POST /api/operator/receiving/pos/{id}/return
Headers: X-User-Id: {uuid}
Body: { "returnQuantity": 10, "returnReason": "Defective", "condition": "DAMAGED" }
```

---

## Operator - Dashboard

### 1. Get Dashboard
```http
GET /api/operator/dashboard
Headers: X-User-Id: {uuid}
```

**Response**:
```json
{
  "totalPRs": 45,
  "draftPRs": 5,
  "submittedPRs": 3,
  "approvedPRs": 30,
  "rejectedPRs": 2,
  "escalatedPRs": 1,
  "returnedPRs": 4,
  "prsThisMonth": 12,
  "prsThisWeek": 3,
  "generatedAt": "2024-12-11T12:00:00"
}
```

### 2. Get PR Status Breakdown
```http
GET /api/operator/dashboard/pr-status
Headers: X-User-Id: {uuid}
```

### 3. Get Monthly Stats
```http
GET /api/operator/dashboard/monthly-stats?year=2024&month=12
Headers: X-User-Id: {uuid}
```

---

## Supervisor - Approvals

### 1. List Pending Approvals
```http
GET /api/supervisor/approvals/pending?page=0&size=20
Headers: X-User-Id: {uuid}
```

### 2. Get PR for Review
```http
GET /api/supervisor/approvals/pr/{prId}
Headers: X-User-Id: {uuid}
```

### 3. Approve PR
```http
PUT /api/supervisor/approvals/pr/{prId}/approve
Headers: X-User-Id: {uuid}
Body: { "decision": "APPROVE", "note": "Approved" }
```

### 4. Reject PR
```http
PUT /api/supervisor/approvals/pr/{prId}/reject
Headers: X-User-Id: {uuid}
Body: { "decision": "REJECT", "note": "Budget exceeded" }
```

### 5. Return PR
```http
PUT /api/supervisor/approvals/pr/{prId}/return
Headers: X-User-Id: {uuid}
Body: { "decision": "RETURN", "note": "Please add specifications" }
```

### 6. Add Feedback
```http
PUT /api/supervisor/approvals/pr/{prId}/feedback
Headers: X-User-Id: {uuid}
Body: { "decision": "FEEDBACK_ONLY", "note": "Consider alternatives" }
```

---

## Supervisor - Dashboard

### 1. Get Dashboard
```http
GET /api/supervisor/dashboard
Headers: X-User-Id: {uuid}
```

**Response**:
```json
{
  "pendingApprovals": 5,
  "escalatedApprovals": 2,
  "urgentApprovals": 1,
  "approvedToday": 3,
  "approvedThisWeek": 15,
  "approvedThisMonth": 45,
  "rejectedThisMonth": 3,
  "averageApprovalTimeHours": 24.5,
  "oldestPendingDays": 2,
  "generatedAt": "2024-12-11T12:00:00"
}
```

---

## Audit & Activity

### 1. Get Activity Feed
```http
GET /api/audit/activity-feed?page=0&size=20
Headers: X-User-Id: {uuid}
```

### 2. Get PR History
```http
GET /api/audit/pr/{prId}/history
Headers: X-User-Id: {uuid}
```

---

## Status Codes

- `200 OK`: Success
- `201 Created`: Resource created
- `400 Bad Request`: Validation error
- `403 Forbidden`: Access denied
- `404 Not Found`: Resource not found
- `409 Conflict`: Concurrent modification
- `500 Internal Server Error`: Server error

---

## Error Response Format

```json
{
  "timestamp": "2024-12-11T12:00:00",
  "status": 400,
  "errorCode": "VALIDATION_ERROR",
  "message": "Cannot submit PR without items"
}
```

---

## Authentication

All endpoints require `X-User-Id` header with user UUID.

---

*Version: 1.0*
*Last Updated: 2024-12-11*
*Total Endpoints: 29*
