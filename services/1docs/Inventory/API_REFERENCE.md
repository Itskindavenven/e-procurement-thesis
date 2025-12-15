# Inventory Service - API Reference

Base URL: `/api/v1/inventory`

## Endpoints

### 1. Stock Operations
*   **GET** `/stocks`: Get all stock. Query params: `sku`, `locationId`.
*   **GET** `/stocks/{sku}`: Get specific item availability.

### 2. Transactions
*   **POST** `/inbound`: Record goods receipt.
    ```json
    {
      "poId": "uuid",
      "items": [{"sku": "A1", "qty": 10}]
    }
    ```
*   **POST** `/outbound`: Record goods issuance.

### 3. Adjustments
*   **POST** `/adjust`: Manual correction (Stock Taking).
