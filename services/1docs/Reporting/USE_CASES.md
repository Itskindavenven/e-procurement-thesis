# Reporting Service - Use Cases

## Overview
The Reporting Service provides analytics, dashboards, and downloadable reports. It aggregates data from all other services via events to provide near-real-time insights without querying operational databases.

## Actors
*   **Manager/Supervisor**: Views high-level KPIs.
*   **Finance Staff**: Analyzes spend.
*   **Vendor**: Views performance stats.

## Use Case List

| ID | Title | Actor | Complexity |
| :--- | :--- | :--- | :--- |
| UC-RPT-001 | View Executive Dashboard | Manager | High |
| UC-RPT-002 | Export Spend Analysis | Finance | Medium |
| UC-RPT-003 | View Vendor Performance | Vendor/Admin | Medium |
| UC-RPT-004 | Generate Audit Report | Admin | Low |

---

## Detailed Use Cases

### UC-RPT-001: View Executive Dashboard
*   **Description**: Charts showing Total Spend, Pending Approvals, Top Vendors.
*   **Flow**:
    1.  User loads Dashboard.
    2.  Service queries pre-aggregated `DashboardMetrics` collection.
    3.  Service returns JSON for charts.

### UC-RPT-002: Export Spend Analysis
*   **Description**: Download CSV/PDF of spending by Category/Department.
*   **Trigger**: End of Month closing.
*   **Flow**:
    1.  Finance sets filters (Date Range, Category).
    2.  Service runs aggregation query on `TransactionHistory`.
    3.  Service generates PDF.
