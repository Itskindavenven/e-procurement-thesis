# Finance Service - API Reference

## Invoices / Verification

### GET /api/finance/invoices
List invoices pending verification.
- **Query Params**: `status` (submitted, verified), `sla_due_before`
- **Response**: `List<InvoiceDTO>`

### GET /api/finance/invoices/{id}
Get detail verification data (PO linkage, Anomaly Flags).

### POST /api/finance/invoices/{id}/verify
Verify an invoice.
- **Body**: `{ "decision": "VERIFY" }`

### POST /api/finance/invoices/{id}/reject
Reject an invoice.
- **Body**: `{ "decision": "REJECT", "reason": "..." }`

---

## Payments

### POST /api/finance/payments/schedule
Schedule a payment.
- **Body**: `{ "invoice_id": "...", "date": "YYYY-MM-DD", "method": "INTERNAL" }`

### POST /api/finance/payments/execute
(Optional) Manual trigger for "Pay Now".

### PUT /api/finance/payments/{scheduleId}/status
Update status (Delay/Cancel).
- **Body**: `{ "status": "DELAYED", "reason": "..." }`

---

## Budget & Top-Up

### GET /api/finance/budgets
Get budget overview per operator/location.

### GET /api/finance/topups
List top-up requests.
- **Query Params**: `status=submitted`

### POST /api/finance/topups/{id}/approve
Approve top-up.
- **Body**: `{ "approved_amount": 1000000 }` (Optional adjustment)

### POST /api/finance/topups/{id}/reject
Reject top-up.

---

## Reports & Audit

### GET /api/finance/reports
Generate report.
- **Query Params**: `type` (TRANSACTION, BUDGET), `format` (PDF, EXCEL), `start_date`, `end_date`.

### GET /api/finance/audit-logs
Search audit logs.
