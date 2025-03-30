package com.productboard.auditlog

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.jdbc.JdbcTestUtils

@SpringBootTest
abstract class AuditLogTest {
    @Autowired private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun clearDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "audit_log")
    }
}
