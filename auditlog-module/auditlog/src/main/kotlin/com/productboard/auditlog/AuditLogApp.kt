package com.productboard.auditlog

import com.productboard.modulith.ModuleApp
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.productboard.auditlog"])
@EnableJpaRepositories
class AuditLogApp {
    companion object : ModuleApp() {
        override val configName = "auditlog"
        override val applicationType = WebApplicationType.NONE
    }
}
