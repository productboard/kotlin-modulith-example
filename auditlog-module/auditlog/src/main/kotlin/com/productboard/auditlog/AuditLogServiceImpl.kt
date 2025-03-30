package com.productboard.auditlog

import mu.KLogging
import org.springframework.stereotype.Service

@Service
class AuditLogServiceImpl(private val auditLogRepository: AuditLogRepository) : AuditLogService {

    override fun listLogsByAuditableId(id: String): List<AuditLogEntry> =
        auditLogRepository.findByAuditableId(id).map { it.toAuditLogEntry() }

    fun createAuditLogEntry(createAuditLogEntry: CreateAuditLogEntry): AuditLogEntryId {
        val auditLogId = requireNotNull(auditLogRepository.save(createAuditLogEntry.toDbAuditLog()).id)
        logger.info {
            "action=auditLogCreated auditableId=${createAuditLogEntry.auditableId} auditableType=${createAuditLogEntry.auditableType} auditableAction=${createAuditLogEntry.auditableAction}"
        }
        return AuditLogEntryId(auditLogId)
    }

    companion object : KLogging()
}

private fun CreateAuditLogEntry.toDbAuditLog(): DbAuditLog =
    DbAuditLog(
        auditableId = auditableId,
        auditableType = auditableType,
        auditableAction = auditableAction,
        timestamp = timestamp,
    )

fun DbAuditLog.toAuditLogEntry(): AuditLogEntry =
    AuditLogEntry(
        auditableId = auditableId,
        auditableType = auditableType,
        auditableAction = auditableAction,
        timestamp = timestamp,
    )
