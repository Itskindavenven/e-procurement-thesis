# Admin Service API Reference

**Base URL**: `/api/admin`
**Authentication**: Required `Authorization: Bearer <JWT>`
**Role Required**: `ADMIN`

---

## 1. Master Data
Manage the catalog of items, categories, and units.

### Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/master-data` | List all master data items |
| **GET** | `/master-data/{id}` | Get item by ID |
| **POST** | `/master-data` | Create new item |
| **PUT** | `/master-data/{id}` | Update existing item |
| **DELETE** | `/master-data/{id}` | Delete item |
| **POST** | `/master-data/relations` | Create parent-child relation (e.g. Category -> Subcategory) |
| **POST** | `/master-data/sync` | Trigger manual sync to other services |

### Payloads

**Create/Update Master Data (`MasterDataDTO`)**
```json
{
  "name": "Laptop Dell XPS",
  "code": "ITM-001",
  "category": "Electronics",
  "unit": "UNIT",
  "status": "ACTIVE"
}
```

---

## 2. Procurement Oversight
Intervene in procurement processes.

### Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **POST** | `/procurement/correct` | Apply administrative correction |
| **POST** | `/procurement/flag` | Flag transaction for investigation |
| **POST** | `/procurement/{id}/status` | Force update status |

### Payloads

**Correction (`ProcurementCorrectionDTO`)**
```json
{
  "procurementId": "uuid-string",
  "correctionType": "COST_CENTER_UPDATE",
  "oldValue": "CC-101",
  "newValue": "CC-102",
  "reason": "Typo in initial request"
}
```

**Flag (`InvestigationFlagDTO`)**
```json
{
  "procurementId": "uuid-string",
  "reason": "Price exceeds market average by 50%",
  "flaggedBy": "admin-user",
  "status": "active"
}
```

---

## 3. Vendor Management
Manage vendor registrations.

### Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/vendors` | List all registrations |
| **GET** | `/vendors/pending` | List pending verification |
| **GET** | `/vendors/{id}` | Get details |
| **PUT** | `/vendors/{id}/verify` | Approve or Reject vendor |
| **DELETE** | `/vendors/{id}` | Deactivate vendor |
| **POST** | `/vendors/{id}/notes` | Add evaluation note |

### Payloads

**Verification Request**
```json
{
  "status": "VERIFIED", // or REJECTED
  "notes": "Documents are valid."
}
```

**Add Note**
```json
{
  "note": "Vendor submitted expired license, pending update."
}
```

---

## 4. Employee Management
Manage users and roles.

### Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/employees` | List all employees |
| **GET** | `/employees/{id}` | Get details |
| **POST** | `/employees` | Create employee |
| **PUT** | `/employees/{id}` | Update employee |
| **DELETE** | `/employees/{id}` | Deactivate employee |

### Payloads

**Employee (`EmployeeDTO`)**
```json
{
  "name": "John Doe",
  "email": "john@company.com",
  "roleId": "ROLE_OPERATOR",
  "status": "ACTIVE"
}
```

---

## 5. Location Management
Manage sites and assignments.

### Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/locations` | List locations |
| **GET** | `/locations/{id}` | Get detail |
| **POST** | `/locations` | Create location |
| **PUT** | `/locations/{id}` | Update location |
| **DELETE** | `/locations/{id}` | Deactivate location |
| **POST** | `/locations/{id}/assign-operator` | Set default Operator |
| **POST** | `/locations/{id}/assign-supervisor` | Set default Supervisor |

### Payloads

**Location (`LokasiDTO`)**
```json
{
  "name": "Warehouse A",
  "address": "123 Industrial Park",
  "type": "WAREHOUSE"
}
```

**Assign Payload**
```json
{
  "operatorId": "uuid-string" // or "supervisorId"
}
```

---

## 6. Calendar Management
Configure operational dates.

### Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/calendar` | List entries |
| **POST** | `/calendar/update` | Update specific day |
| **POST** | `/calendar/cutoff` | Set monthly cutoff date |
| **POST** | `/calendar/non-active` | Add holiday/non-working day |

---

## 7. Audit & Logs
System monitoring.

### Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/audit` | Get all system logs |
| **GET** | `/audit/service/{serviceName}` | Filter logs by service |
| **GET** | `/notifications/logs` | View notification history |


### Response (`LogSystemDTO`)
```json
{
  "logId": "uuid",
  "serviceName": "procurement-service",
  "action": "CREATE_PR",
  "userId": "user-123",
  "details": "Created PR #1001",
  "createdAt": "2023-10-01T10:00:00"
}
```

### Additional Actions
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **POST** | `/audit/export` | Export logs (PDF/CSV) |
| **POST** | `/audit/backup` | Backup logs to separate server |
| **GET** | `/audit/anomalies` | Detected system anomalies |

---

## 8. Notification & Alert Management
Manage templates and recipients.

### Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/notifications/templates` | List all templates |
| **PUT** | `/notifications/templates/{id}` | Update template content |
| **GET** | `/notifications/recipients` | List recipient configs |
| **POST** | `/notifications/recipients` | Add/Update recipients |
| **GET** | `/notifications/logs` | View delivery history |

### Payloads
**Update Template**
```json
{
  "subject": "PR Approved",
  "body": "Your PR {{pr_no}} has been approved."
}
```

**Update Recipients**
```json
{
  "templateId": "uuid-string",
  "recipients": ["user@example.com", "admin@example.com"]
}
```

---

## 9. General Features
Cross-module functionality.

### Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **POST** | `/audit/export` | Export System Logs (CSV) |
