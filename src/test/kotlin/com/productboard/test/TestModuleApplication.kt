package com.productboard.test

import com.productboard.modulith.ModuleApp
import org.springframework.boot.WebApplicationType
import org.springframework.context.annotation.ComponentScan

/** An extra module used in Modulith integration tests to test cross-module concepts. */
@ComponentScan(basePackages = ["com.productboard.test"])
@Suppress("UtilityClassWithPublicConstructor")
class TestModuleApplication {

    companion object : ModuleApp() {
        override val configName = "test-module"
        override val applicationType = WebApplicationType.NONE
    }
}
