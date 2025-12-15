# Procurement Service API Reference

**Base URL**: `/api`
**Version**: `v1`

---

## 1. Operator APIs
**Role Required**: `ROLE_OPERATOR`

### 1.1 Procurement Requests
Manage the lifecycle of procurement requests (Goods & Services).

#### List Procurement Requests
**GET** `/operator/requests`
*Query Params*: `page` (default 0), `size` (default 20), `status` (optional), `search` (optional)

**Response**:
```json
{
  "content": [
    {
      "id": "uuid",
      "vendorName": "PT. Vendor Jaya",
      "type": "GOODS",
      "description": "Office Supplies Q3",
      "priority": "NORMAL",
      "status": "DRAFT",
      "deadline": "2023-12-31T23:59:00",
      "totalAmount": 15000000,
      "createdAt": "2023-10-01T10:00:00",
      "itemCount": 5
    }
  ],
  "totalPages": 5,
  "totalElements": 98
}
```

#### Create Draft Request
**POST** `/operator/requests`

**Request Body**:
```json
{
  "vendorId": "uuid-vendor-1",
  "type": "GOODS",
  "description": "Urgent Laptop Replacement",
  "priority": "URGENT",
  "deadline": "2023-11-15T17:00:00",
  "locationId": "uuid-loc-1",
  "items": [
    {
      "vendorCatalogId": "uuid-catalog-item-1",
      "itemName": "MacBook Pro M2",
      "quantity": 2,
      "unitPrice": 25000000
    }
  ],
  "deliveryDetail": {
    "deliveryAddress": "Main Office, Floor 3",
    "plannedDeliveryDate": "2023-11-10",
    "deliveryNotes": "Call upon arrival"
  }
}
```

#### Get Request Details
**GET** `/operator/requests/{id}`

**Response**:
```json
{
  "id": "uuid-pr-1",
  "vendorName": "PT. Tech Solutions",
  "status": "SUBMITTED",
  "approvalHistory": [
    {
      "approverName": "Supervisor Budi",
      "role": "SUPERVISOR",
      "decision": "APPROVED",
      "note": "Approved as per budget",
      "timestamp": "2023-10-02T09:00:00"
    }
  ],
  "items": [...]
}
```

#### Submit Request
**PUT** `/operator/requests/{id}/submit`

**Request Body**:
```json
{
  "submitNotes": "Please approve quickly due to project deadline."
}
```

### 1.2 Goods Receiving
Process incoming deliveries for Purchase Orders (POs).

#### List POs Awaiting Receiving
**GET** `/operator/receiving/pos`

#### Accept Full Delivery
**POST** `/operator/receiving/pos/{id}/accept`

**Request Body**:
```json
{
  "receivedDate": "2023-10-25T14:30:00",
  "notes": "All items received in good condition. Checked against DO-123.",
  "deliveryOrderNumber": "DO-123456",
  "imageUrls": [
    "http://storage/do-scan.jpg",
    "http://storage/goods-photo.jpg"
  ]
}
```

#### Accept Partial Delivery
**POST** `/operator/receiving/pos/{id}/partial`

**Request Body**:
```json
{
  "receivedDate": "2023-10-25",
  "deliveryOrderNumber": "DO-PARTIAL-1",
  "items": [
    {
      "poItemId": "uuid-item-1",
      "quantityReceived": 5,
      "condition": "GOOD"
    }
  ]
}
```

#### Reject Delivery
**POST** `/operator/receiving/pos/{id}/reject`

**Request Body**:
```json
{
  "rejectionReason": "Wrong items delivered",
  "rejectedDate": "2023-10-25"
}
```

### 1.3 Service Completion
Confirm completion of service jobs.

#### Confirm Service
**POST** `/operator/service-completion/confirm`

**Request Body**:
```json
{
  "procurementRequestId": "uuid-pr-service",
  "completionDate": "2023-10-20",
  "rating": 5,
  "notes": "Technician was professional and job done well."
}
```

### 1.4 Vendor Rating
Rate vendor performance after completion.

#### Submit Rating
**POST** `/operator/vendor-rating`

**Request Body**:
```json
{
  "poId": "uuid-po-1",
  "rating": 4,
  "comment": "Good quality but slight delay in delivery."
}
```

### 1.5 Operator Dashboard
**GET** `/operator/dashboard/stats`
**GET** `/operator/dashboard/recent-activities`

---

## 2. Supervisor APIs
**Role Required**: `ROLE_SUPERVISOR`

### 2.1 Approvals
Manage approval workflow for Operator requests.

#### List Pending Approvals
**GET** `/supervisor/approvals/pending`

**Response**:
```json
[
  {
    "procurementRequestId": "uuid-1",
    "operatorName": "Operator Andi",
    "vendorName": "PT. Vendor A",
    "totalAmount": 5000000,
    "daysWaiting": 2
  }
]
```

#### Approve Request
**PUT** `/supervisor/approvals/pr/{id}/approve`

**Request Body**:
```json
{
  "decision": "APPROVED",
  "note": "Budget sufficient. Proceed."
}
```

#### Reject Request
**PUT** `/supervisor/approvals/pr/{id}/reject`

**Request Body**:
```json
{
  "decision": "REJECTED",
  "note": "Over budget for Q3."
}
```

#### Return for Revision
**PUT** `/supervisor/approvals/pr/{id}/return`

**Request Body**:
```json
{
  "decision": "RETURNED",
  "note": "Please attach 3 comparison quotes."
}
```

### 2.2 Service Termin Review
Review progress payment terms (Termin) submitted by Vendors.

#### Review Termin
**POST** `/termin/{id}/review`

**Request Body**:
```json
{
  "action": "APPROVE", // or CLARIFY, REVISE
  "notes": "Termin 1 deliverables verified."
}
```

### 2.3 Supervisor Dashboard
**GET** `/supervisor/dashboard/summary`
**GET** `/supervisor/dashboard/budget-utilization`

---

## 3. General APIs
**No specific role required (Authenticated)**

### 3.1 Reference Data
**GET** `/public/procurement-types`
**GET** `/public/priorities`
