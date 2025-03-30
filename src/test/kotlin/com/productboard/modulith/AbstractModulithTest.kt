package com.productboard.modulith

import kotlin.reflect.KClass
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(StartSpringExtension::class)
abstract class AbstractModulithTest {

    protected fun <T : Any> getBeanFromParentContext(clazz: KClass<T>): T {
        return StartSpringExtension.applicationContext.getBean(clazz.java)
    }

    protected fun <T : Any> getBeanFromModuleContext(module: ModuleApp, clazz: KClass<T>): T {
        return ChildContextRegistry[module].getBean(clazz.java)
    }
}
