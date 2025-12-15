# Finance Service - Analysis & Implementations

## 1. Overview
The Finance Service is a critical component handling invoice verification, payment execution (mock), and budget management. It acts as the gatekeeper between Vendor submissions and actual fund release.

## 2. Dependencies
- **Upstream**:
    - `Vendor Service`: Source of Invoices.
    - `Procurement Service`: Source of POs/Contracts (for matching).
- **Downstream**:
    - `Internal Payment Channel`: Mock external system.
    - `Notification Service`.
    - `Audit Service`.

## 3. Implementation Gaps

### Service Existence
- [ ] **Check**: Does `payment-service` covers these features? Or do we need a new `finance-service`?
    - *Observation*: `payment-service` likely handles the raw transaction, but "Invoice Verification" and "Budget Management" might be missing or need to be built.
    - If `payment-service` is purely a gateway, we need logic for `Invoice` workflow.

### Key Logic Needs
1. **Automated Verification Engine**:
    - Logic to fetch PO total from Procurement and compare with Invoice total.
    - Logic to check Date/Termin validity.
2. **Anomaly Detection System**:
    - Rules Engine (B/C/D) implementation.
    - Needs historical data access (Avg PO value).
3. **Budget Locking/Ledger**:
    - `Budget` entity needs concurrency control (optimistic locking) to prevent overdrafts.

## 4. Next Steps
1. Verify if `payment-service` can be expanded to `finance-service` or if a new Spring Boot application is required.
2. Implement Entities (`Invoice`, `Payment`, `Budget`).
3. Implement `VerificationStrategy` pattern for different check types.
