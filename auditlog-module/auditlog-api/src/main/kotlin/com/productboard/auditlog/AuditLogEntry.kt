package com.productboard.auditlog

import java.time.Instant

data class AuditLogEntry(
    val auditableId: String,
    val auditableType: String,
    val auditableAction: AuditableAction,
    val timestamp: Instant,
)
