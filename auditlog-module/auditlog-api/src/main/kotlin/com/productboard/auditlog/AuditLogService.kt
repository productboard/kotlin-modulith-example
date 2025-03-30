package com.productboard.auditlog

import com.productboard.modulith.PublicModuleInterface

interface AuditLogService : PublicModuleInterface {
    fun listLogsByAuditableId(id: String): List<AuditLogEntry>
}
