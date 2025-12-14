# Implementation Plan - Auth Service Unit Tests

## Goal
Implement comprehensive unit tests for the Auth Service to cover the scenarios defined in `TEST_SCENARIOS.md`. This includes expanding existing tests and creating new test classes for uncovered services.

## User Review Required
> [!NOTE]
> This plan focuses on **Unit Tests** (Service Layer) using Mockito. Integration tests (Controller Layer) will be handled in a subsequent task.

## Proposed Changes

### Service Layer Tests

#### [MODIFY] [AuthServiceTest.java](file:///c:/Users/Asus/Desktop/tugas-akhir/backend/services/auth-service/auth-service/src/test/java/com/tugas_akhir/auth_service/service/AuthServiceTest.java)
- **Add Tests for Login**:
    - Login with inactive account (SC-005)
- **Add Tests for Refresh Token**:
    - Successful refresh
    - Invalid/Expired refresh token
- **Add Tests for Logout**:
    - Successful logout (SC-011)
- **Add Tests for Password Reset**:
    - Request reset (Success & User Not Found) (SC-006, SC-007)
    - Confirm reset (Success, Mismatch, Invalid Token) (SC-008, SC-009, SC-010)

#### [NEW] [AdminServiceTest.java](file:///c:/Users/Asus/Desktop/tugas-akhir/backend/services/auth-service/auth-service/src/test/java/com/tugas_akhir/auth_service/service/AdminServiceTest.java)
- **Create User**: Success, Username/Email Exists (SC-014)
- **Update User**: Success, User Not Found (SC-015)
- **Deactivate/Reactivate User**: Success, User Not Found (SC-016)
- **Verify**: Audit and MasterData events are published.

#### [NEW] [VendorServiceTest.java](file:///c:/Users/Asus/Desktop/tugas-akhir/backend/services/auth-service/auth-service/src/test/java/com/tugas_akhir/auth_service/service/VendorServiceTest.java)
- **Register Vendor**: Success, Email Exists (SC-012, SC-013)
- **Verify**: User creation, Profile creation, Event publishing.

#### [NEW] [TokenServiceTest.java](file:///c:/Users/Asus/Desktop/tugas-akhir/backend/services/auth-service/auth-service/src/test/java/com/tugas_akhir/auth_service/service/TokenServiceTest.java)
- **Create Refresh Token**: Verify Redis storage.
- **Validate Refresh Token**: Success, Not Found.
- **Revoke Token**: Verify deletion.
- **Blacklist Access Token**: Verify Redis entry with TTL.

## Verification Plan
### Automated Tests
Run all tests using Maven:
```bash
.\mvnw.cmd test
```
**Success Criteria**: All tests pass (Exit Code 0) and coverage increases significantly.
