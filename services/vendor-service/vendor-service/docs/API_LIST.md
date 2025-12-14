# Vendor Service API List

This document provides a detailed reference for the implemented REST APIs in the Vendor Service.

## 1. Vendor Account Management
**Base URL**: `/api/vendor/account`

### Get All Vendors
- **Method**: `GET`
- **URL**: `/`
- **Description**: Retrieves a list of all registered vendors.
- **Response**: `200 OK` - Array of `VendorAccountDTO`

### Get Vendor by ID
- **Method**: `GET`
- **URL**: `/{id}`
- **Description**: Retrieves details of a specific vendor.
- **Parameters**:
    - `id` (Path): The Vendor ID.
- **Response**: `200 OK` - `VendorAccountDTO`

### Create Vendor
- **Method**: `POST`
- **URL**: `/`
- **Description**: Registers a new vendor.
- **Body**: `VendorAccountDTO`
    ```json
    {
      "vendorId": "v123",
      "namaPerusahaan": "PT. Vendor Maju",
      "emailKontak": "contact@vendor.com",
      "status": "ACTIVE",
      "ratingPerforma": 0.0
    }
    ```
- **Response**: `200 OK` - Created `VendorAccountDTO`

### Update Vendor
- **Method**: `PUT`
- **URL**: `/{id}`
- **Description**: Updates an existing vendor's information.
- **Body**: `VendorAccountDTO` (fields to update)
- **Response**: `200 OK` - Updated `VendorAccountDTO`

---

## 2. Catalog Management
**Base URL**: `/api/vendor/catalog`

### Get Catalog by Vendor
- **Method**: `GET`
- **URL**: `/`
- **Query Params**:
    - `vendorId`: The ID of the vendor.
- **Description**: Retrieves all catalog items belonging to a vendor.
- **Response**: `200 OK` - Array of `VendorCatalogItemDTO`

### Get Item by ID
- **Method**: `GET`
- **URL**: `/{id}`
- **Description**: Retrieves details of a specific catalog item.
- **Response**: `200 OK` - `VendorCatalogItemDTO`

### Create Catalog Item
- **Method**: `POST`
- **URL**: `/`
- **Description**: Adds a new item to the vendor's catalog.
- **Body**: `VendorCatalogItemDTO`
    ```json
    {
      "vendorId": "v123",
      "sku": "ITEM-001",
      "namaItem": "Laptop High Spec",
      "spesifikasi": "RAM 16GB, SSD 512GB",
      "hargaList": 15000000,
      "ppn": 1650000,
      "statusItem": "ACTIVE"
    }
    ```
- **Response**: `200 OK` - Created `VendorCatalogItemDTO`

### Update Catalog Item
- **Method**: `PUT`
- **URL**: `/{id}`
- **Description**: Updates details of a catalog item.
- **Body**: `VendorCatalogItemDTO`
- **Response**: `200 OK` - Updated `VendorCatalogItemDTO`

### Delete Catalog Item
- **Method**: `DELETE`
- **URL**: `/{id}`
- **Description**: Soft deletes a catalog item (sets `isDeleted = true`).
- **Response**: `204 No Content`

---

## 3. RFQ (Request for Quotation)
**Base URL**: `/api/vendor/rfq`

### Get Open RFQs
- **Method**: `GET`
- **URL**: `/open`
- **Query Params**:
    - `vendorId` (Optional): Filter RFQs relevant to this vendor.
- **Description**: Retrieves a list of RFQs with status `OPEN`.
- **Response**: `200 OK` - Array of `RFQDTO`

### Get RFQ Details
- **Method**: `GET`
- **URL**: `/{id}`
- **Description**: Retrieves full details of an RFQ.
- **Response**: `200 OK` - `RFQDTO`

---

## 4. Quotation Management
**Base URL**: `/api/vendor/quotation`

### Submit Quotation
- **Method**: `POST`
- **URL**: `/{rfqId}`
- **Description**: Submits a price quotation for a specific RFQ.
- **Body**: `QuotationDTO`
    ```json
    {
      "vendorId": "v123",
      "totalHarga": 14500000,
      "rincianHarga": "Discount applied",
      "leadTime": 7,
      "catatanVendor": "Ready stock"
    }
    ```
- **Response**: `200 OK` - Created `QuotationDTO` (Status: `SUBMITTED`)

### Get Quotation
- **Method**: `GET`
- **URL**: `/{rfqId}`
- **Query Params**:
    - `vendorId`: The vendor who submitted the quote.
- **Description**: Retrieves the quotation submitted by a vendor for a specific RFQ.
- **Response**: `200 OK` - `QuotationDTO`
