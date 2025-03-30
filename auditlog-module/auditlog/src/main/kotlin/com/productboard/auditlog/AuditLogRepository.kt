package com.productboard.auditlog

import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AuditLogRepository : CrudRepository<DbAuditLog, UUID> {
    fun findByAuditableId(auditableId: String): List<DbAuditLog>
}
