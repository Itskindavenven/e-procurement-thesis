# Master Data Service - Use Cases

## Overview
The Master Data Service manages the auxiliary data required by other services, ensuring consistency across the platform. It handles Locations, Cost Centers, Item Categories, and the Global Catalog.

## Actors
*   **Admin**: Configures master data.
*   **System**: Syncs catalog data from Vendor Service.
*   **User (Procurement)**: Searches for items/locations.

## Use Case List

| ID | Title | Actor | Complexity |
| :--- | :--- | :--- | :--- |
| UC-MD-001 | Manage Locations | Admin | Low |
| UC-MD-002 | Manage Cost Centers | Admin | Low |
| UC-MD-003 | Sync Vendor Catalog | System | Medium |
| UC-MD-004 | Search Catalog | User | Medium |

---

## Detailed Use Cases

### UC-MD-001: Manage Locations
*   **Description**: Add/Update warehouse or office locations.
*   **Flow**:
    1.  Admin adds "Warehouse A - Jakarta".
    2.  System saves to DB.
    3.  (Optional) Publish `master.location.updated` if other services cache this.

### UC-MD-003: Sync Vendor Catalog
*   **Trigger**: Vendor Service publishes `vendor.catalog.updated`.
*   **Flow**:
    1.  Master Data Service consumes event.
    2.  Service updates existing Item or creates new one in `GlobalCatalog`.
    3.  Service re-indexes data for search (Elasticsearch/DB).

### UC-MD-004: Search Catalog
*   **Description**: Find items to procure.
*   **Flow**:
    1.  User types "Laptop".
    2.  System queries `GlobalCatalog` (via fuzzy search).
    3.  System returns items + preferred vendor info.
