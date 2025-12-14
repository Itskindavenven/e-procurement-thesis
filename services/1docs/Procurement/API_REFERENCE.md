# Procurement Service API Reference

**Base URL**: `/api`

---

## 1. Operator API (`/operator/procurement`)
**Role**: `OPERATOR`

### Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/requests` | List PRs (Optional: `?status=APPROVED`) |
| **POST** | `/requests` | Create new Draft PR |
| **PUT** | `/requests/{id}` | Update Draft PR |
| **GET** | `/requests/{id}` | Get PR Details |
| **DELETE** | `/requests/{id}` | Delete Draft PR |
| **PUT** | `/requests/{id}/submit` | Submit Draft for Approval |

### Payloads

**Create/Update PR (`CreateProcurementRequestDTO`)**
```json
{
  "description": "Office Supplies Q4",
  "items": [
    {
      "itemName": "A4 Paper",
      "quantity": 100,
      "estimatedPrice": 50000,
      "category": "Office",
      "vendorId": "uuid"
    }
  ]
}
```

---

## 2. Supervisor API (`/supervisor/approvals`)
**Role**: `SUPERVISOR`

### Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/pending` | List pending approvals |
| **GET** | `/pr/{id}` | Get PR details for review |
| **PUT** | `/pr/{id}/approve` | Approve PR (Triggers PO) |
| **PUT** | `/pr/{id}/reject` | Reject PR |
| **PUT** | `/pr/{id}/return` | Return to Operator |
| **PUT** | `/pr/{id}/feedback` | Add comment |

### Payloads

**Approval Action (`ApprovalRequestDTO`)**
```json
{
  "notes": "Budget approved for Project X."
}
```

---

## 3. Goods Receiving (`/v1/goods-receiving`)
**Role**: `OPERATOR` / `WAREHOUSE`

### Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **POST** | `/{poId}/accept` | Confirm receipt of goods |

---

## 4. General/Public (`/v1/procurement-requests`)
Generic access (likely used by other services or shared views).

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/{id}` | Get Request by ID |
| **POST** | `/draft` | Create Draft (Alt) |
| **POST** | `/{id}/submit` | Submit (Alt) |
