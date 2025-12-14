# API Reference

This document provides a high-level overview of the REST API endpoints. For detailed request/response schemas, please use the Swagger UI (`/swagger-ui.html`).

## Authentication
All endpoints (except public ones) require a valid JWT token in the `Authorization` header:
`Authorization: Bearer <token>`

## Endpoints

### Employee Management
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/admin/employees` | List all employees |
| `POST` | `/api/admin/employees` | Create a new employee |
| `PUT` | `/api/admin/employees/{id}/role` | Assign a role to an employee |
| `PUT` | `/api/admin/employees/{id}/location` | Assign a location to an employee |

### Vendor Management
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/admin/vendors` | List vendor registrations |
| `POST` | `/api/admin/vendors/{id}/verify` | Verify (Approve/Reject) a vendor |

### Location Management
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/admin/locations` | List all locations |
| `POST` | `/api/admin/locations` | Create a new location |
| `POST` | `/api/admin/locations/assign-operator` | Assign an operator |
| `POST` | `/api/admin/locations/assign-supervisor` | Assign a supervisor |

### Master Data
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/admin/master-data` | List master data items |
| `POST` | `/api/admin/master-data` | Create a master data item |
| `PUT` | `/api/admin/master-data/{id}` | Update master data item |
| `DELETE` | `/api/admin/master-data/{id}` | Soft delete master data item |
| `POST` | `/api/admin/master-data/relations` | Create a parent-child relation |
| `POST` | `/api/admin/master-data/sync` | Trigger data sync |

### Procurement
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/admin/procurement/correction` | Submit administrative correction |
| `POST` | `/api/admin/procurement/flag` | Flag item for investigation |

### Calendar
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/admin/calendar` | Get calendar entries |
| `POST` | `/api/admin/calendar/update` | Update day status (working/cutoff) |
| `POST` | `/api/admin/calendar/cutoff` | Set monthly cutoff date |
| `POST` | `/api/admin/calendar/non-active` | Add non-active day range |

### Notifications
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/admin/notifications/templates` | List templates |
| `POST` | `/api/admin/notifications/templates` | Create a template |
| `GET` | `/api/admin/notifications/logs` | View notification logs |

### Audit
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/admin/audit` | View all audit logs |
| `GET` | `/api/admin/audit/service/{name}` | View logs by service name |
