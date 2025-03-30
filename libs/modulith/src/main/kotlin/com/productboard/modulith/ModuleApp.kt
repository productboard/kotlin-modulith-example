package com.productboard.modulith

import org.springframework.boot.WebApplicationType

/**
 * Base class for spring application that provides helper method for starting application.
 *
 * Most straight-forward usage of this class is to extend it by `companion object` like so:
 * ```
 * @SpringBootApplication
 * class MyApp(...) {
 * companion object: SubmoduleApp {
 *      override val configName = "submodule_app_name"
 * }
 * ```
 *
 * and override all necessary methods and properties.
 */
abstract class ModuleApp {
    abstract val configName: String
    abstract val applicationType: WebApplicationType

    val type: Class<*> = if (this::class.isCompanion) this::class.java.declaringClass else this::class.java
}
