package com.productboard.restapi

import com.productboard.auditlog.AuditLogEntry
import com.productboard.auditlog.AuditLogService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auditlog")
class AuditLogController(private val auditLogService: AuditLogService) {
    @GetMapping("/{id}")
    fun getAuditLogsForEntity(@PathVariable id: String): List<AuditLogEntry> = auditLogService.listLogsByAuditableId(id)
}
