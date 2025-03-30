package com.productboard.modulith

import com.productboard.test.X
import com.productboard.test.X1
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

class RootBeanDefinitionsTest : AbstractModulithTest() {

    @Test
    fun `Should autowire primary bean`() {
        assertInstanceOf(X1::class.java, getBeanFromParentContext(X::class))
    }
}
