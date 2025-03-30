package com.productboard.auditlog

import java.util.UUID

data class AuditLogEntryId(val id: UUID) {
    override fun toString(): String = id.toString()
}
