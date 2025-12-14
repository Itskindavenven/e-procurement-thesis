# Database Schema Documentation

## Overview
The Procurement Service uses PostgreSQL as its primary database with Flyway for version-controlled database migrations. The schema supports both goods and service procurement workflows with full audit trails.

---

## Flyway Migrations

### Migration Files Location
```
src/main/resources/db/migration/
```

### Migration Execution
Flyway automatically runs migrations on application startup. Migrations are executed in version order (V1, V2, V3, etc.).

```bash
# Automatic on startup
mvn spring-boot:run

# Manual execution
mvn flyway:migrate

# Check migration status
mvn flyway:info

# Validate migrations
mvn flyway:validate
```

---

## Schema Overview

### Database Tables (8 Total)

| Table | Purpose | Phase | Rows (Est.) |
|-------|---------|-------|-------------|
| procurement_requests | Core PR entity | 1 | High |
| procurement_items | PR line items | 1 | Very High |
| delivery_details | Delivery information | 1 | High |
| additional_documents | Supporting docs | 1 | Medium |
| approval_histories | Audit trail | 1 | High |
| termin_details | Service termins | 2 | Medium |
| po_headers | Purchase orders | 3 | High |
| po_items | PO line items | 3 | Very High |
| receiving_records | Goods receiving | 3 | High |

---

## Migration Details

### V1: Procurement Requests Table

**File**: `V1__create_procurement_requests_table.sql`

**Purpose**: Core procurement request entity

**Columns**:
- `id` (UUID, PK): Unique identifier
- `operator_id` (UUID, NOT NULL): Creator/owner
- `vendor_id` (UUID, NOT NULL): Selected vendor
- `type` (VARCHAR(20), NOT NULL): GOODS or SERVICE
- `priority` (VARCHAR(20), NOT NULL): NORMAL, IMPORTANT, URGENT
- `status` (VARCHAR(20), NOT NULL): Workflow status
- `description` (TEXT): PR description
- `deadline` (TIMESTAMP): Required completion date
- `created_at`, `updated_at`: Timestamps
- `created_by`, `updated_by`: Audit fields
- `is_deleted` (BOOLEAN): Soft delete flag
- `deleted_at` (TIMESTAMP): Soft delete timestamp

**Indexes** (5):
1. `idx_pr_operator_id`: Query by operator
2. `idx_pr_status`: Filter by status
3. `idx_pr_vendor_id`: Group by vendor
4. `idx_pr_created_at`: Sort by date
5. `idx_pr_operator_status`: Composite for operator's PR list

**Constraints**:
- `type` CHECK: Only GOODS or SERVICE
- `priority` CHECK: NORMAL, IMPORTANT, or URGENT
- `status` CHECK: Valid workflow statuses

---

### V2: Procurement Items Table

**File**: `V2__create_procurement_items_table.sql`

**Purpose**: Line items for procurement requests

**Columns**:
- `id` (UUID, PK)
- `procurement_request_id` (UUID, FK, NOT NULL)
- `catalog_item_id` (UUID): Reference to catalog
- `item_name` (VARCHAR(255), NOT NULL)
- `quantity` (INTEGER, NOT NULL, CHECK > 0)
- `unit_price` (DECIMAL(15,2), NOT NULL, CHECK >= 0)
- `tax_ppn` (DECIMAL(15,2)): VAT/PPN tax
- `subtotal` (DECIMAL(15,2)): Calculated amount

**Relationships**:
- FK to `procurement_requests` with CASCADE DELETE

**Indexes** (2):
1. `idx_item_pr_id`: Fast PR item lookup
2. `idx_item_catalog_id`: Catalog integration

---

### V3: Delivery Details Table

**File**: `V3__create_delivery_details_table.sql`

**Purpose**: Delivery and location information

**Columns**:
- `id` (UUID, PK)
- `procurement_request_id` (UUID, FK, NOT NULL)
- `delivery_address` (TEXT, NOT NULL)
- `location_id` (UUID): Reference to location master data
- `planned_delivery_date` (DATE)
- `notes` (TEXT)

**Relationships**:
- FK to `procurement_requests` with CASCADE DELETE

**Indexes** (2):
1. `idx_delivery_pr_id`: PR lookup
2. `idx_delivery_location`: Location grouping

---

### V4: Additional Documents Table

**File**: `V4__create_additional_documents_table.sql`

**Purpose**: Supporting document attachments

**Columns**:
- `id` (UUID, PK)
- `procurement_request_id` (UUID, FK, NOT NULL)
- `file_name` (VARCHAR(255), NOT NULL)
- `file_type` (VARCHAR(100)): MIME type
- `file_size` (BIGINT): Size in bytes
- `file_url` (TEXT, NOT NULL): Storage path/URL
- `uploaded_at` (TIMESTAMP, NOT NULL)

**Relationships**:
- FK to `procurement_requests` with CASCADE DELETE

**Indexes** (2):
1. `idx_document_pr_id`: PR documents
2. `idx_document_uploaded`: Sort by upload date

---

### V5: Approval Histories Table

**File**: `V5__create_approval_histories_table.sql`

**Purpose**: Complete audit trail of all approvals

**Columns**:
- `id` (UUID, PK)
- `procurement_request_id` (UUID, FK, NOT NULL)
- `approver_id` (UUID, NOT NULL): Who made decision
- `role` (VARCHAR(20), NOT NULL): SUPERVISOR, FINANCE, MANAGER
- `decision` (VARCHAR(20), NOT NULL): APPROVE, REJECT, RETURN, FEEDBACK_ONLY
- `note` (TEXT): Decision notes
- `timestamp` (TIMESTAMP, NOT NULL): When decision made

**Relationships**:
- FK to `procurement_requests` with CASCADE DELETE

**Indexes** (4):
1. `idx_approval_pr_id`: PR history
2. `idx_approval_approver`: User actions
3. `idx_approval_timestamp`: Timeline
4. `idx_approval_decision`: Filter by decision

**Constraints**:
- `role` CHECK: Valid approver roles
- `decision` CHECK: Valid decisions

---

### V6: Termin Details Table

**File**: `V6__create_termin_details_table.sql`

**Purpose**: Service procurement payment milestones (Phase 2)

**Columns**:
- `id` (UUID, PK)
- `procurement_request_id` (UUID, FK, NOT NULL)
- `termin_number` (INTEGER, NOT NULL): 1, 2, 3...
- `phase_name` (VARCHAR(200), NOT NULL): Phase description
- `value` (DECIMAL(15,2), NOT NULL): Payment amount
- `deliverables` (TEXT): Expected outputs
- `status` (VARCHAR(50), NOT NULL): Review status
- `vendor_submitted_at` (TIMESTAMP): When vendor submitted
- `operator_reviewed_at` (TIMESTAMP): When operator reviewed
- `supervisor_decided_at` (TIMESTAMP): When supervisor decided
- `clarification_notes` (TEXT): Clarification requests
- `revision_notes` (TEXT): Revision requests
- `operator_review_notes` (TEXT): Operator comments
- `supervisor_notes` (TEXT): Supervisor comments
- `reviewed_by` (UUID): Operator who reviewed
- `approved_by` (UUID): Supervisor who approved
- Audit trail fields + soft delete

**Relationships**:
- FK to `procurement_requests` with CASCADE DELETE

**Indexes** (4):
1. `idx_termin_pr_id`: PR termins
2. `idx_termin_status`: Status filtering
3. `idx_termin_vendor_submitted`: Pending queue
4. `idx_termin_number`: Sequential ordering

**Constraints**:
- `value` CHECK: >= 0
- `status` CHECK: Valid termin statuses

---

### V7: Purchase Order Tables

**File**: `V7__create_po_tables.sql`

**Purpose**: Purchase orders from approved PRs (Phase 3)

#### Table: po_headers

**Columns**:
- `id` (UUID, PK)
- `procurement_request_id` (UUID, FK, UNIQUE, NOT NULL)
- `po_number` (VARCHAR(50), UNIQUE, NOT NULL): PO identifier
- `vendor_id` (UUID, NOT NULL)
- `status` (VARCHAR(20), NOT NULL): PO fulfillment status
- `total_amount` (DECIMAL(15,2), NOT NULL)
- Audit trail fields + soft delete

**Relationships**:
- FK to `procurement_requests` (ONE-TO-ONE)

**Indexes** (5):
1. `idx_po_pr_id`: PR to PO lookup
2. `idx_po_number`: PO number search
3. `idx_po_vendor`: Vendor POs
4. `idx_po_status`: Status filtering
5. `idx_po_created`: Date sorting

**Constraints**:
- `status` CHECK: CREATED, IN_DELIVERY, DELIVERED, COMPLETED, CANCELLED
- `po_number` UNIQUE
- `procurement_request_id` UNIQUE (one PO per PR)

#### Table: po_items

**Columns**:
- `id` (UUID, PK)
- `po_id` (UUID, FK, NOT NULL)
- `item_id` (UUID): Reference to procurement item
- `item_name` (VARCHAR(255), NOT NULL)
- `quantity` (INTEGER, NOT NULL, CHECK > 0)
- `unit_price` (DECIMAL(15,2), NOT NULL, CHECK >= 0)
- `subtotal` (DECIMAL(15,2), NOT NULL)

**Relationships**:
- FK to `po_headers` with CASCADE DELETE

**Indexes** (2):
1. `idx_po_item_po_id`: PO items
2. `idx_po_item_item_id`: Item reference

---

### V8: Receiving Records Table

**File**: `V8__create_receiving_records_table.sql`

**Purpose**: Goods/service receiving tracking (Phase 3)

**Columns**:
- `id` (UUID, PK)
- `po_id` (UUID, FK, NOT NULL)
- `received_quantity` (INTEGER): Actual quantity
- `condition` (VARCHAR(50)): GOOD, DAMAGED, INCOMPLETE
- `status` (VARCHAR(30), NOT NULL): ACCEPTED, REJECTED, RETURNED, PARTIALLY_ACCEPTED
- `received_at` (TIMESTAMP, NOT NULL): When received
- `received_by` (UUID, NOT NULL): Operator who received
- `notes` (TEXT): General notes
- `rejection_reason` (TEXT): Why rejected/returned
- Audit trail fields + soft delete

**Relationships**:
- FK to `po_headers`

**Indexes** (4):
1. `idx_receiving_po_id`: PO receiving records
2. `idx_receiving_status`: Status filtering
3. `idx_receiving_date`: Timeline
4. `idx_receiving_by`: Operator actions

**Constraints**:
- `status` CHECK: Valid receiving statuses

---

## Entity Relationships

### ER Diagram (Text Format)

```
procurement_requests (1) ----< (M) procurement_items
procurement_requests (1) ----< (M) delivery_details
procurement_requests (1) ----< (M) additional_documents
procurement_requests (1) ----< (M) approval_histories
procurement_requests (1) ----< (M) termin_details

procurement_requests (1) ---- (1) po_headers
po_headers (1) ----< (M) po_items
po_headers (1) ----< (M) receiving_records
```

### Cascade Behavior

**ON DELETE CASCADE**:
- Deleting a PR deletes all related items, documents, approvals, termins
- Deleting a PO deletes all PO items

**Soft Delete Pattern**:
- `procurement_requests`: Uses soft delete (is_deleted flag)
- `termin_details`: Uses soft delete
- `po_headers`: Uses soft delete
- `receiving_records`: Uses soft delete

---

## Data Types

### Common Patterns

**UUIDs**: All primary keys and foreign keys
- Format: `a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11`
- Generated by application (Java UUID.randomUUID())

**Decimals**: All monetary values
- `DECIMAL(15, 2)`: Up to 999,999,999,999.99 (13 digits + 2 decimals)
- Prevents floating-point errors

**Timestamps**: All datetime fields
- `TIMESTAMP`: With timezone support
- Default: `CURRENT_TIMESTAMP`

**Status Enums**: VARCHAR with CHECK constraints
- Ensures data integrity at DB level
- Readable in queries

---

## Indexes Strategy

### Performance Optimizations

1. **Foreign Key Indexes**: All FK columns indexed
2. **Status Filtering**: All status columns indexed
3. **Timestamp Sorting**: created_at with DESC
4. **Composite Indexes**: operator_id + status for common queries
5. **Partial Indexes**: `WHERE is_deleted = FALSE` on soft-deleted tables

### Index Naming Convention
- `idx_{table}_{column(s)}`: Standard index
- Lowercase with underscores

---

## Database Sizing Estimates

### Expected Growth (1 Year)

| Table | Rows/Year | Size/Row | Total Size |
|-------|-----------|----------|------------|
| procurement_requests | 50,000 | 2 KB | 100 MB |
| procurement_items | 200,000 | 500 B | 100 MB |
| approval_histories | 150,000 | 500 B | 75 MB |
| termin_details | 10,000 | 1 KB | 10 MB |
| po_headers | 40,000 | 1 KB | 40 MB |
| po_items | 160,000 | 500 B | 80 MB |
| **Total** | | | **~500 MB** |

Add 50% for indexes: **750 MB total**

---

## Maintenance

### Recommended Tasks

**Daily**:
- Monitor slow queries
- Check connection pool usage

**Weekly**:
- Review table growth
- Analyze query performance

**Monthly**:
- `VACUUM ANALYZE` on large tables
- Check index usage with `pg_stat_user_indexes`
- Archive old soft-deleted records (> 1 year)

**Quarterly**:
- Review and optimize slow queries
- Consider partitioning for very large tables

---

## Backup Strategy

### Recommended Approach

**Full Backup**: Daily at 2 AM
```bash
pg_dump procurement_db > backup_$(date +%Y%m%d).sql
```

**Incremental**: WAL archiving for point-in-time recovery

**Retention**:
- Daily backups: 30 days
- Weekly backups: 6 months
- Monthly backups: 2 years

---

## Security

### Access Control

**Application User** (procurement_app):
- SELECT, INSERT, UPDATE on all tables
- No DELETE permissions (soft delete only)
- No DDL permissions

**Migration User** (flyway):
- Full DDL permissions
- Used only during migrations

**Read-Only User** (procurement_readonly):
- SELECT only on all tables
- For reporting and analytics

### Sensitive Data

No PII stored in this database. Vendor and operator information referenced by UUID only (stored in other services).

---

## Configuration

### Application Properties

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/procurement_db
spring.datasource.username=procurement_app
spring.datasource.password=${DB_PASSWORD}

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.validate-on-migrate=true
```

### Environment Variables

```bash
# Required
DB_PASSWORD=your_secure_password

# Optional (defaults shown)
DB_HOST=localhost
DB_PORT=5432
DB_NAME=procurement_db
DB_USER=procurement_app
```

---

## Troubleshooting

### Common Issues

**Issue**: Flyway migration fails
```
Solution: Check flyway_schema_history table
SELECT * FROM flyway_schema_history ORDER BY installed_rank DESC;
```

**Issue**: Duplicate key violation
```
Solution: Check UUID generation in application
Ensure UUID.randomUUID() is being called
```

**Issue**: Soft delete filter not working
```
Solution: Verify @Where annotation in entity
@Where(clause = "is_deleted = false")
```

**Issue**: Slow queries on large tables
```
Solution: Check if indexes are being used
EXPLAIN ANALYZE SELECT ...;
```

---

## Future Enhancements

### Planned Schema Changes

**Phase 4**: Reporting tables
- Materialized views for dashboards
- Summary tables for analytics

**Phase 5**: Notifications
- notification_templates
- notification_logs

**Phase 6**: Finance workflow
- payment_records
- invoice_tracking

---

## Appendix: SQL Quick Reference

### Useful Queries

**Check table sizes**:
```sql
SELECT 
    schemaname,
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
```

**Find unused indexes**:
```sql
SELECT 
    schemaname,
    tablename,
    indexname,
    idx_scan
FROM pg_stat_user_indexes
WHERE idx_scan = 0 AND indexrelname NOT LIKE 'pg_toast%'
ORDER BY pg_relation_size(indexrelid) DESC;
```

**Active soft-deleted records**:
```sql
SELECT 
    'procurement_requests' as table_name,
    COUNT(*) as deleted_count
FROM procurement_requests
WHERE is_deleted = true;
```

---

*Document Version: 1.0*  
*Last Updated: 2024-12-10*  
*Database Version: PostgreSQL 14+*
