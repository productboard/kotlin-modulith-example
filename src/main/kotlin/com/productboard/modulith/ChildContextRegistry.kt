package com.productboard.modulith

import java.util.concurrent.ConcurrentHashMap
import org.springframework.context.ConfigurableApplicationContext

/** Children contexts holder */
object ChildContextRegistry {
    private val registry = ConcurrentHashMap<String, ConfigurableApplicationContext>()

    fun add(module: ModuleApp, context: ConfigurableApplicationContext) {
        registry[module.configName] = context
    }

    fun getAll(): List<Pair<String, ConfigurableApplicationContext>> =
        registry.entries.map { (id, context) -> id to context }

    operator fun get(module: ModuleApp): ConfigurableApplicationContext = registry.getValue(module.configName)
}
