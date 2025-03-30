package com.productboard.modulith

import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader
import org.springframework.core.Ordered

/**
 * An [ApplicationContextInitializer] used to inject an instance of [PublicInterfacesBeanPostProcessor] into the
 * corresponding application context.
 */
class PublicInterfacesInitializer(private val rootContext: ConfigurableApplicationContext) :
    ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        /* Register the post processor as a bean in the context. This is necessary as the Ordered-based ordering only
        works for auto-discovered bean post processors and NOT for those manually registered via a
        BeanFactoryPostProcessor. */
        applicationContext.beanFactory.registerSingleton(
            "publicInterfacesBeanPostProcessor",
            PublicInterfacesBeanPostProcessor(rootContext),
        )
    }
}

/**
 * Custom [BeanPostProcessor] injecting any bean of type [PublicModuleInterface] into the [rootContext] as well to
 * enable cross-module dependencies on such beans.
 *
 * Note that this implementation is [Ordered] and comes with [Ordered.LOWEST_PRECEDENCE] so that it picks up the "final"
 * version of each bean, i.e. after all other post-processors have been applied. This it required e.g. for AOP proxies
 * to work correctly etc.
 */
class PublicInterfacesBeanPostProcessor(private val rootContext: ConfigurableApplicationContext) :
    BeanPostProcessor, Ordered {
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (bean is PublicModuleInterface) {
            ModulithApplication.logger.info { "action=registerPublicInterface name=$beanName" }
            registerBeanDefinition(bean, beanName)
            rootContext.beanFactory.registerSingleton(beanName, bean)
        }
        return bean
    }

    private fun registerBeanDefinition(bean: PublicModuleInterface, beanName: String) {
        val rootRegistry = rootContext.beanFactory as BeanDefinitionRegistry
        val reader = AnnotatedBeanDefinitionReader(rootRegistry)
        reader.registerBean(bean.javaClass, beanName)
    }

    override fun getOrder(): Int = Ordered.LOWEST_PRECEDENCE
}
