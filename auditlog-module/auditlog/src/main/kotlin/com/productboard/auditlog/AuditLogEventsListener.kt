package com.productboard.auditlog

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class AuditLogEventsListener(private val auditLogService: AuditLogServiceImpl) {

    @EventListener
    fun onAuditLogEvent(event: AuditLogEvent) {
        auditLogService.createAuditLogEntry(event.toCreateDto())
    }
}

private fun AuditLogEvent.toCreateDto(): CreateAuditLogEntry =
    CreateAuditLogEntry(
        auditableId = auditableId,
        auditableType = auditableType,
        auditableAction = auditableAction,
        timestamp = timestamp,
    )
