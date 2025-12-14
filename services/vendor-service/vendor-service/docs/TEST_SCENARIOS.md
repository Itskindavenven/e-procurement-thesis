# Vendor Service Test Scenarios

This document details the test scenarios to verify the functionality of the Vendor Service. These can be executed using Postman, curl, or the Swagger UI.

## Scenario 1: Vendor Lifecycle
**Objective**: Verify that a vendor can be registered, updated, and retrieved.

| Step | Action | Endpoint | Payload / Params | Expected Result |
|------|--------|----------|------------------|-----------------|
| 1 | **Register Vendor** | `POST /api/vendor/account` | `{"vendorId": "v001", "namaPerusahaan": "Tech Corp", "emailKontak": "admin@tech.com", "status": "ACTIVE"}` | **200 OK**. Returns JSON with `vendorId: "v001"`. |
| 2 | **Get Vendor** | `GET /api/vendor/account/v001` | - | **200 OK**. Returns vendor details matching Step 1. |
| 3 | **Update Vendor** | `PUT /api/vendor/account/v001` | `{"namaPerusahaan": "Tech Corp Global", "emailKontak": "admin@tech.com", "status": "ACTIVE"}` | **200 OK**. Name updated to "Tech Corp Global". |
| 4 | **Verify Update** | `GET /api/vendor/account/v001` | - | **200 OK**. Returns updated name. |

## Scenario 2: Catalog Management
**Objective**: Verify that a vendor can manage their product catalog.

| Step | Action | Endpoint | Payload / Params | Expected Result |
|------|--------|----------|------------------|-----------------|
| 1 | **Add Item** | `POST /api/vendor/catalog` | `{"vendorId": "v001", "sku": "LPT-001", "namaItem": "Laptop", "hargaList": 1000}` | **200 OK**. Returns item with generated `catalogItemId`. |
| 2 | **List Items** | `GET /api/vendor/catalog` | `?vendorId=v001` | **200 OK**. List contains "Laptop". |
| 3 | **Update Price** | `PUT /api/vendor/catalog/{itemId}` | `{"vendorId": "v001", "sku": "LPT-001", "namaItem": "Laptop", "hargaList": 950}` | **200 OK**. Price updated to 950. |
| 4 | **Delete Item** | `DELETE /api/vendor/catalog/{itemId}` | - | **204 No Content**. |
| 5 | **Verify Delete** | `GET /api/vendor/catalog/{itemId}` | - | **404 Not Found** (or marked deleted if soft delete logic is checked). |

## Scenario 3: RFQ and Quotation Process
**Objective**: Verify the flow of responding to a Request for Quotation (RFQ).

**Pre-requisite**: An RFQ must exist in the database. You may need to insert one manually or use a seed script if no RFQ creation endpoint exists for Vendors (usually created by Procurement Service).
*   *Manual Insert (SQL)*: `INSERT INTO rfqs (rfq_id, status_rfq, tipe) VALUES ('rfq-001', 'OPEN', 'OPEN');`

| Step | Action | Endpoint | Payload / Params | Expected Result |
|------|--------|----------|------------------|-----------------|
| 1 | **Check Open RFQs** | `GET /api/vendor/rfq/open` | - | **200 OK**. List includes `rfq-001`. |
| 2 | **Submit Quote** | `POST /api/vendor/quotation/rfq-001` | `{"vendorId": "v001", "totalHarga": 5000, "leadTime": 5}` | **200 OK**. Quote created with status `SUBMITTED`. |
| 3 | **Verify Quote** | `GET /api/vendor/quotation/rfq-001` | `?vendorId=v001` | **200 OK**. Returns the quote details from Step 2. |

## Scenario 4: Error Handling
**Objective**: Verify that the system handles invalid inputs gracefully.

| Step | Action | Endpoint | Payload / Params | Expected Result |
|------|--------|----------|------------------|-----------------|
| 1 | **Get Invalid Vendor** | `GET /api/vendor/account/invalid-id` | - | **404 Not Found**. Error message: "Vendor not found...". |
| 2 | **Create Invalid Item** | `POST /api/vendor/catalog` | `{}` (Empty body) | **400 Bad Request**. Validation errors. |
