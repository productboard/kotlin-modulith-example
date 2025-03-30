package com.productboard.auditlog

import com.productboard.modulith.ModuleApp
import java.time.Instant
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.WebApplicationType
import org.springframework.context.ApplicationEventPublisher

class AuditLogEventsListenerTest(
    @Autowired private val applicationEventPublisher: ApplicationEventPublisher,
    @Autowired private val auditLogRepository: AuditLogRepository,
) : AuditLogTest() {

    private val testSourceModule =
        object : ModuleApp() {
            override val configName: String
                get() = "test"

            override val applicationType: WebApplicationType
                get() = WebApplicationType.NONE
        }

    @Test
    fun `should consume event and store AuditLogEntry`() {
        val auditableId = UUID.randomUUID().toString()
        // Given
        val event =
            AuditLogEvent(
                auditableId = auditableId,
                auditableType = "testType",
                auditableAction = AuditableAction.CREATE,
                timestamp = Instant.now(),
                source = testSourceModule,
            )

        // When
        applicationEventPublisher.publishEvent(event)

        // Then
        val savedEntries = auditLogRepository.findByAuditableId(auditableId)
        assertEquals(1, savedEntries.size)
        val savedEntry = savedEntries.first()
        assertNotNull(savedEntry.id)
        assertEquals(event.auditableId, savedEntry.auditableId)
        assertEquals(event.auditableType, savedEntry.auditableType)
        assertEquals(event.auditableAction, savedEntry.auditableAction)
        assertEquals(event.timestamp, savedEntry.timestamp)
    }
}
