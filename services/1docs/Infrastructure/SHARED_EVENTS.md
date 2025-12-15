# Shared Event Catalog

This document defines the key Domain Events used for asynchronous communication between microservices.

## Topic Structure
Pattern: `domain.entity.action` (e.g., `procurement.po.created`)

## Event Registry

### 1. Procurement Domain (`procurement-service`)
| Event Type | Topic | Trigger | Consumers |
| :--- | :--- | :--- | :--- |
| `po.approved` | `procurement.events` | PO is fully approved | **Finance** (Create Invoice), **Vendor** (Notify), **Notification** (Email) |
| `pr.created` | `procurement.events` | PR submitted | **Workflow** (Start Approval), **Audit** |

### 2. Vendor Domain (`vendor-service`)
| Event Type | Topic | Trigger | Consumers |
| :--- | :--- | :--- | :--- |
| `invoice.submitted` | `vendor.events` | Vendor submits invoice | **Finance** (Validate), **Notification** |
| `quotation.submitted` | `vendor.events` | Vendor responds to RFQ | **Procurement** (Update Status), **Notification** |
| `vendor.registered` | `vendor.events` | New vendor signup | **Auth** (Create User), **Workflow** (Onboarding) |

### 3. Finance Domain (`finance-service`)
| Event Type | Topic | Trigger | Consumers |
| :--- | :--- | :--- | :--- |
| `payment.processed` | `finance.events` | Payment successful | **Procurement** (Close PO), **Vendor** (Notify) |

### 4. Workflow Domain (`workflow-service`)
| Event Type | Topic | Trigger | Consumers |
| :--- | :--- | :--- | :--- |
| `workflow.task.assigned` | `workflow.events` | User task created | **Notification** (Send Email to Assignee) |
| `workflow.process.completed` | `workflow.events` | Process ends | **source-service** (e.g., Procurement) |

### 5. Document Domain (`document-service`)
| Event Type | Topic | Trigger | Consumers |
| :--- | :--- | :--- | :--- |
| `document.uploaded` | `document.events` | File upload complete | **Audit**, **VirusScan** |
