package com.productboard.restapi

import com.productboard.modulith.ModuleApp
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration

@SpringBootApplication(scanBasePackages = ["com.productboard.restapi"], exclude = [DataSourceAutoConfiguration::class])
class RestApiApp {
    companion object : ModuleApp() {
        override val configName = "restapi"
        override val applicationType = WebApplicationType.SERVLET
    }
}
