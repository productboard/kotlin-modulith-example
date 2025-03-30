package com.productboard.entities

import com.productboard.auditlog.AuditLogEvent
import com.productboard.auditlog.AuditableAction
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class EntitiesAuditLogHandler(private val applicationEventPublisher: ApplicationEventPublisher) {

    @EventListener
    fun onEntityCreatedEvent(event: EntityCreatedEvent) {
        applicationEventPublisher.publishEvent(
            AuditLogEvent(
                auditableId = event.entity.id.toString(),
                auditableType = AUDITABLE_TYPE,
                auditableAction = AuditableAction.CREATE,
                source = EntitiesApp.Companion,
            )
        )
    }

    @EventListener
    fun onEntityDeletedEvent(event: EntityDeletedEvent) {
        applicationEventPublisher.publishEvent(
            AuditLogEvent(
                auditableId = event.id.toString(),
                auditableType = AUDITABLE_TYPE,
                auditableAction = AuditableAction.DELETE,
                source = EntitiesApp.Companion,
            )
        )
    }

    companion object {
        private const val AUDITABLE_TYPE = "entity"
    }
}
