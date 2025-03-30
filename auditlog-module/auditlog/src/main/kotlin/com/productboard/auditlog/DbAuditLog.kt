package com.productboard.auditlog

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "audit_log")
data class DbAuditLog(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: UUID? = null,
    val auditableId: String,
    val auditableType: String,
    val auditableAction: AuditableAction,
    val timestamp: Instant,
)
