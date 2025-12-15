# Reporting Service - API Reference

Base URL: `/api/v1/reporting`

## Endpoints

### 1. Dashboards
*   **GET** `/dashboard/executive`: Summary stats.
*   **GET** `/dashboard/procurement`: Pending PRs, Active POs.

### 2. Analytics
*   **GET** `/analytics/spend`: Query spend data.
    *   Params: `from`, `to`, `groupBy={category|vendor}`.

### 3. Vendor Reports
*   **GET** `/vendors/{id}/performance`: Get specific vendor stats.

### 4. Exports
*   **POST** `/exports/generate`: Triggers async report generation.
*   **GET** `/exports/{id}/download`: Download generated file.
