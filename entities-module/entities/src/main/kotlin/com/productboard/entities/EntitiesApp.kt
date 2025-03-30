package com.productboard.entities

import com.productboard.modulith.ModuleApp
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = ["com.productboard.entities"])
class EntitiesApp {
    companion object : ModuleApp() {
        override val configName = "entities"
        override val applicationType = WebApplicationType.NONE
    }
}
