# Finance Service - Requirements

## Functional Requirements

### FR-01: Invoice Verification
- System must allow Finance to view invoices with status `submitted`.
- System must automatically check:
    - Total value matches PO/Term.
    - POD existence (from Procurement Service).
- System must calculate SLA (1x24 hours) and alert if approaching due.
- System must detect anomalies (Frequency > 3/day, Value > 2x Avg, Mismatches).

### FR-02: Payment Management
- System must allow scheduling payments for verified invoices.
- System must integrate with `InternalPaymentChannel` (Mock) to execute payments.
- System must handle payment statuses: Scheduled, Processed, Failed, Delayed, Canceled.
- System must generate `Payment` records upon successful transaction.

### FR-03: Budget & Cashflow
- System must track budgets per Operator/Location/Project.
- System must record every transaction in `CashflowRecord`.
- System must allow "Top-Up Requests" from Supervisors and Approval by Finance.
- System must prevent payments if budget is insufficient (unless overridden/topped-up).

### FR-04: Reporting
- System must generate PDF/Excel reports for:
    - Transaction History
    - Budget Realization
    - Outstanding Payments
    - Audit Logs
- System must support scheduled automatic reporting.

### FR-05: Audit & Security
- All sensitive actions (Verify, Pay, Reject, TopUp) must be logged in `AuditLogFinance`.
- Only authenticated "Finance" role users can access these modules.

## Non-Functional Requirements
- **Consistency**: Financial data must be strongly consistent.
- **Auditability**: 100% of state changes must be traceable.
- **SLA**: Use cases (verification) have strict time limits monitored by the system.
