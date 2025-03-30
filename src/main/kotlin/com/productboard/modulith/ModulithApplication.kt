package com.productboard.modulith

import ch.qos.logback.classic.LoggerContext
import com.productboard.auditlog.AuditLogApp
import com.productboard.entities.EntitiesApp
import com.productboard.modulith.ModulithApplication.Companion.MODULES
import com.productboard.modulith.ModulithApplication.Companion.logger
import com.productboard.restapi.RestApiApp
import jakarta.annotation.PreDestroy
import java.util.concurrent.TimeoutException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import mu.KLogging
import mu.KotlinLogging
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.WebApplicationType
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean

/** The main application class that is used to run the whole service. */
@SpringBootConfiguration
class ModulithApplication {
    @PreDestroy fun destroy() = logLifeCycle("APPLICATION STOPPED")

    fun startApp(modules: List<ModuleApp>, args: Array<String>): ConfigurableApplicationContext {
        logLifeCycle("STARTING APPLICATION")

        setupLogging()
        Thread.setDefaultUncaughtExceptionHandler { thread, e -> logger.error(e) { "Uncaught exception: $thread" } }

        val parentContextBuilder =
            SpringApplicationBuilder(ModulithApplication::class.java)
                .web(WebApplicationType.NONE)
                .registerShutdownHook(false)
        val parentContext =
            parentContextBuilder.run(*args).apply { setId(ModulithApplication::class.simpleName.orEmpty()) }
        val initializer = PublicInterfacesInitializer(parentContext)

        val childContexts =
            modules.associateWith {
                logger.info { "Starting module ${it.configName}" }
                parentContextBuilder
                    .child(it.type)
                    .properties("spring.config.name=${it.configName}")
                    .initializers(initializer)
                    .web(it.applicationType)
                    .registerShutdownHook(false)
                    .run(*args)
                    .apply {
                        setId(it.configName)
                        ChildContextRegistry.add(it, this)
                    }
            }

        Runtime.getRuntime()
            .addShutdownHook(
                Thread(
                    {
                        logLifeCycle("STOPPING APPLICATION")
                        // shutdown child contexts in reverse order
                        val (webContexts, nonWebContexts) =
                            modules.reversed().partition { it.applicationType != WebApplicationType.NONE }
                        // shutdown web contexts first
                        ContextCloser.closeAndWait(webContexts.map { childContexts.getValue(it) })
                        // then shutdown non-web contexts
                        ContextCloser.closeAndWait(nonWebContexts.map { childContexts.getValue(it) })
                        // close the parent context
                        ContextCloser.closeAndWait(listOf(parentContext))
                    },
                    "ShutdownHook",
                )
            )

        logLifeCycle("APPLICATION STARTED")
        return parentContext
    }

    @Bean fun forwardToChildrenListener() = ForwardToChildrenListener()

    companion object : KLogging() {
        val MODULES: List<ModuleApp> = listOf(AuditLogApp, EntitiesApp, RestApiApp)
    }
}

/** Production main method */
fun main(args: Array<String>) {
    ModulithApplication().startApp(MODULES, args)
}

fun logLifeCycle(state: String) {
    logger.info { "===================== $state =====================" }
}

private fun setupLogging() {
    // we don't want these framework packages to show up in (and pollute) our stack traces
    val additionalFrameworkPackages = listOf("io.netty", "reactor")
    (LoggerFactory.getILoggerFactory() as LoggerContext).frameworkPackages.addAll(additionalFrameworkPackages)
}

private object ContextCloser {

    private val SLEEP = 50.milliseconds
    // corresponds to default grace period in both Kubernetes and Spring
    private val TIMEOUT = 30.seconds

    private val logger = KotlinLogging.logger {}

    fun closeAndWait(
        contexts: Collection<ConfigurableApplicationContext>,
        timeout: Duration = TIMEOUT,
        sleep: Duration = SLEEP,
    ) {
        if (contexts.none { it.isActive }) {
            return
        }
        contexts.forEach {
            logLifeCycle("CLOSING APPLICATION CONTEXT ${it.id}")
            it.close()
        }
        val sleepMillis = sleep.inWholeMilliseconds
        try {
            var waited = 0L
            while (contexts.any { it.isActive }) {
                if (waited > timeout.inWholeMilliseconds) {
                    throw TimeoutException()
                }
                Thread.sleep(sleepMillis)
                waited += sleepMillis
            }
        } catch (ex: InterruptedException) {
            Thread.currentThread().interrupt()
            logger.warn { "Interrupted waiting for application contexts $contexts to become inactive" }
        } catch (ex: TimeoutException) {
            val stillActive = contexts.filter { it.isActive }
            logger.warn(ex) { "Timed out waiting for application contexts $stillActive to become inactive" }
        }
    }
}
