# Vendor Service API Reference

## Catalog Management
### Get Catalog Items
- **Method**: `GET`
- **URL**: `/api/vendor/catalog`
- **Params**: `vendorId` (String)
- **Response**: `List<VendorCatalogItemDTO>`

### Get Item by ID
- **Method**: `GET`
- **URL**: `/api/vendor/catalog/{id}`
- **Response**: `VendorCatalogItemDTO`

### Create Item
- **Method**: `POST`
- **URL**: `/api/vendor/catalog`
- **Body**: `VendorCatalogItemDTO`
- **Response**: `VendorCatalogItemDTO`

### Update Item
- **Method**: `PUT`
- **URL**: `/api/vendor/catalog/{id}`
- **Body**: `VendorCatalogItemDTO`
- **Response**: `VendorCatalogItemDTO`

### Delete Item
- **Method**: `DELETE`
- **URL**: `/api/vendor/catalog/{id}`
- **Response**: `204 No Content`

### Mass Update
- **Method**: `POST`
- **URL**: `/api/vendor/catalog/mass-update`
- **Body**: `List<VendorCatalogItemDTO>`
- **Response**: `200 OK`

---

## Delivery Management
### Create Delivery
- **Method**: `POST`
- **URL**: `/api/vendor/delivery`
- **Body**: `DeliveryDTO`
- **Response**: `DeliveryDTO`

### Upload Packing List
- **Method**: `POST`
- **URL**: `/api/vendor/delivery/{id}/packing-list`
- **Content-Type**: `multipart/form-data`
- **Params**: `file` (MultipartFile)
- **Response**: `200 OK`

### Upload POD
- **Method**: `POST`
- **URL**: `/api/vendor/delivery/{id}/pod`
- **Content-Type**: `multipart/form-data`
- **Params**: `file` (MultipartFile)
- **Response**: `200 OK`

---

## Invoice Management
### Generate Draft Invoice
- **Method**: `POST`
- **URL**: `/api/vendor/invoice/generate-draft`
- **Body**: `InvoiceGenerationRequest`
- **Response**: `InvoiceDTO`

### Submit Invoice
- **Method**: `POST`
- **URL**: `/api/vendor/invoice/{id}/submit`
- **Response**: `InvoiceDTO`

### Upload Invoice Document
- **Method**: `POST`
- **URL**: `/api/vendor/invoice/{id}/document`
- **Content-Type**: `multipart/form-data`
- **Params**: `file` (MultipartFile)
- **Response**: `200 OK`

---

## Quotation Management
### Get Quotation
- **Method**: `GET`
- **URL**: `/api/vendor/quotation/{rfqId}`
- **Params**: `vendorId` (String)
- **Response**: `QuotationDTO`

### Submit Quotation
- **Method**: `POST`
- **URL**: `/api/vendor/quotation/{rfqId}`
- **Body**: `QuotationDTO`
- **Response**: `QuotationDTO`

---

## RFQ Management
### Get Open RFQs
- **Method**: `GET`
- **URL**: `/api/vendor/rfq/open`
- **Params**: `vendorId` (optional)
- **Response**: `List<RFQDTO>`

### Get RFQ Detail
- **Method**: `GET`
- **URL**: `/api/vendor/rfq/{id}`
- **Response**: `RFQDTO`

### Decline RFQ
- **Method**: `POST`
- **URL**: `/api/vendor/rfq/{id}/decline`
- **Body**: `DeclineRequest`
- **Params**: `vendorId`
- **Response**: `200 OK`

### Request Clarification
- **Method**: `POST`
- **URL**: `/api/vendor/rfq/{id}/clarification`
- **Body**: `ClarificationRequest`
- **Params**: `vendorId`
- **Response**: `200 OK`

---

## Account & Reports
### Vendor Account Operations
- **Base URL**: `/api/vendor/account`
- **Operations**: `GET /`, `GET /{id}`, `POST /`, `PUT /{id}`

### Reporting
- **Get Dashboard**: `GET /api/vendor/report/dashboard?vendorId={id}`
- **Get Performance**: `GET /api/vendor/report/performance?vendorId={id}&period={period}`
- **Export Report**: `GET /api/vendor/report/export?vendorId={id}&format={PDF/EXCEL}`
