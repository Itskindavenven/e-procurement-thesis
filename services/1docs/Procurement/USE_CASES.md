# Procurement Service Use Cases

## 1. Operator Workflows
Operators are the initiators of the procurement process.

*   **Create Procurement Request (Draft)**:
    *   **Goal**: Start a new request for goods or services.
    *   **Action**: Fill in items, quantities, and descriptions. Saves as `DRAFT`.
    *   **API**: `POST /api/operator/procurement/requests`

*   **Submit Request**:
    *   **Goal**: Send the request for approval.
    *   **Action**: Validates the draft and changes status to `SUBMITTED`.
    *   **API**: `PUT /api/operator/procurement/requests/{id}/submit`

*   **View Dashboard**:
    *   **Goal**: Track status of requests.
    *   **Action**: View list of PRs filtered by status (Pending, Approved, Rejected).

*   **Revise Request**:
    *   **Goal**: Fix issues flagged by a supervisor.
    *   **Context**: PR status is `RETURNED`.
    *   **Action**: Edit details and Re-submit.

## 2. Supervisor Workflows
Supervisors are responsible for vetting and approving requests.

*   **View Pending Approvals**:
    *   **Goal**: See workload.
    *   **API**: `GET /api/supervisor/approvals/pending`

*   **Approve Request**:
    *   **Goal**: Authorize the purchase.
    *   **Outcome**: PR status becomes `APPROVED`, and a Purchase Order (PO) is automatically generated.
    *   **API**: `PUT /api/supervisor/approvals/pr/{id}/approve`

*   **Reject Request**:
    *   **Goal**: Deny the purchase.
    *   **Outcome**: PR status becomes `REJECTED`. Process ends.

*   **Return for Revision**:
    *   **Goal**: Request changes from the Operator.
    *   **Outcome**: PR status becomes `RETURNED`.

*   **Provide Feedback**:
    *   **Goal**: Communicate without changing status.
    *   **Action**: Add a comment to the PR timeline.

## 3. Receiving Workflows
Handling the physical arrival of goods.

*   **Accept Goods**:
    *   **Goal**: Confirm receipt against a Purchase Order.
    *   **Action**: Validate PO ID and confirm delivery.
    *   **API**: `POST /api/v1/goods-receiving/{poId}/accept`

## 4. System/Admin Workflows
*   **Audit Trail**:
    *   **Goal**: Compliance.
    *   **Action**: System logs every state change and approval action.
