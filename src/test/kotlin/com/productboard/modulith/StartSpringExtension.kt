package com.productboard.modulith

import com.productboard.test.TestModuleApplication
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource
import org.springframework.context.ConfigurableApplicationContext

/** We need an extension so all tests share the same Spring context */
internal object StartSpringExtension : BeforeAllCallback, CloseableResource {
    internal lateinit var applicationContext: ConfigurableApplicationContext
    private var started = false

    override fun beforeAll(context: ExtensionContext) {
        if (!started) {
            started = true
            // Your "before all tests" startup logic goes here
            // The following line registers a callback hook when the root test context is shut down
            context.root.getStore(GLOBAL).put(this.javaClass.canonicalName, this)

            System.setProperty(
                "auditlog.datasource.url",
                "jdbc:tc:postgresql:15.2:///modulithdb?TC_TMPFS=/testtmpfs:rw",
            )
            System.setProperty(
                "entities.datasource.url",
                "jdbc:tc:postgresql:15.2:///modulithdb?TC_TMPFS=/testtmpfs:rw",
            )

            applicationContext =
                ModulithApplication().startApp(ModulithApplication.MODULES + TestModuleApplication, emptyArray())
        }
    }

    override fun close() {
        applicationContext.stop()
    }
}
