package com.productboard.auditlog

import com.productboard.modulith.ModuleApp
import com.productboard.modulith.ModulithEvent
import java.time.Instant

data class AuditLogEvent(
    val auditableId: String,
    val auditableType: String,
    val auditableAction: AuditableAction,
    val timestamp: Instant = Instant.now(),
    private val source: ModuleApp,
) : ModulithEvent(source)

enum class AuditableAction {
    CREATE,
    UPDATE,
    DELETE,
}
