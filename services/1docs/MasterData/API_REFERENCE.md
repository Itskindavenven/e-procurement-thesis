# Master Data Service - API Reference

Base URL: `/api/v1/master-data`

## Endpoints

### 1. Locations
*   **GET** `/locations`: List all locations.
*   **POST** `/locations`: Add new location (Admin).

### 2. Cost Centers
*   **GET** `/cost-centers`: List all cost centers.
*   **POST** `/cost-centers`: Add new cost center (Admin).

### 3. Catalog
*   **GET** `/catalog/search?q={query}`: Search items.
*   **GET** `/catalog/categories`: Get item categories.

### 4. General Config
*   **GET** `/config/tax-rates`: Get global tax settings (e.g., PPN 11%).
