# Security Architecture

## JWT & Refresh Tokens
- **Access Token**: Short-lived (15m), stateless JWT. Contains user roles and `jti`.
- **Refresh Token**: Long-lived (14d), opaque (UUID), stored in Redis.
- **Blacklist**: Redis set containing `jti` of revoked access tokens (until their expiry).

## Flows
### Login
1. User sends credentials.
2. Server validates.
3. Server generates Access Token (JWT) and Refresh Token (UUID).
4. Refresh Token stored in Redis: `refresh:{token} -> {userId, metadata}`.
5. Returns both tokens.

### Refresh
1. User sends Refresh Token.
2. Server looks up in Redis.
3. If valid:
    - Invalidate old Refresh Token.
    - Generate new Access Token and new Refresh Token.
    - Return new tokens.
4. If invalid: 401 Unauthorized.

### Logout
1. User sends Access Token and Refresh Token.
2. Server adds Access Token `jti` to Redis Blacklist (TTL = remaining time).
3. Server deletes Refresh Token from Redis.

### Password Reset / Deactivate
1. Admin/System triggers reset/deactivate.
2. Server finds all Refresh Tokens for user (via `user:{id}:refreshTokens` index).
3. Server deletes all Refresh Tokens.
4. (Optional) Server blacklists currently active Access Tokens if known/tracked.
