# Use Cases

| Use Case | Endpoint | Description |
|---|---|---|
| Login | POST /api/v1/auth/login | Authenticate user, return JWT + Refresh Token |
| Refresh | POST /api/v1/auth/refresh | Rotate refresh token, get new access token |
| Logout | POST /api/v1/auth/logout | Revoke access token (blacklist) and delete refresh token |
| Reset Request | POST /api/v1/auth/reset/request | Send email with reset token |
| Reset Confirm | POST /api/v1/auth/reset/confirm | Update password using token |
| Vendor Register | POST /api/v1/vendor/register | Register new vendor (pending verification) |
| Admin List Users | GET /api/v1/admin/users | List users with filters |
| Admin Create User | POST /api/v1/admin/users | Create internal user |
| Admin Update User | PUT /api/v1/admin/users/{id} | Update user details |
| Admin Deactivate | POST /api/v1/admin/users/{id}/deactivate | Deactivate user (revoke tokens) |
| Admin Reactivate | POST /api/v1/admin/users/{id}/reactivate | Reactivate user |
