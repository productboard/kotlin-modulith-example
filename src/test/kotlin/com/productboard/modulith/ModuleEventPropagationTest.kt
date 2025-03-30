package com.productboard.modulith

import com.productboard.test.TestModuleApplication
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationListener

class ModuleEventPropagationTest : AbstractModulithTest() {

    // context itself is ApplicationEventPublisher
    private val childContext = ChildContextRegistry[TestModuleApplication.Companion]

    private var callsCount = 0
    private var payload: Any? = null

    @BeforeEach
    fun setupListener() {
        callsCount = 0
        payload = null
        // create a listener in the same context as publisher
        val listener =
            ApplicationListener.forPayload<TestEvent> {
                callsCount++
                payload = it
            }
        childContext.addApplicationListener(listener)
    }

    @Test
    fun `Should propagate module events`() {
        val event = TestEvent(42, TestModuleApplication.Companion)

        childContext.publishEvent(event)
        assertEquals(event, payload)
        assertEquals(1, callsCount)
    }

    @Test
    fun `Should not propagate internal event`() {
        val event = TestInternalEvent(43)

        childContext.publishEvent(event)

        assertNull(payload)
        assertEquals(0, callsCount)
    }
}

data class TestEvent(val something: Int, val moduleApp: ModuleApp) : ModulithEvent(moduleApp)

data class TestInternalEvent(val something: Int)
