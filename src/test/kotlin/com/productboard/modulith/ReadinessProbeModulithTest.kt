package com.productboard.modulith

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.context.WebApplicationContext

class ReadinessProbeModulithTest : AbstractModulithTest() {

    @Test
    fun `All modules should be ready`() {
        val webTestClient = WebTestClient.bindToServer().build()
        val webContexts = ChildContextRegistry.getAll().unzip().second.filterIsInstance<WebApplicationContext>()
        assertEquals(1, webContexts.size)
        webContexts.forEach { ctx ->
            val port = ctx.getBean(ServerProperties::class.java).port
            webTestClient.get().uri("http://localhost:$port/actuator/health/readiness").exchange().expectStatus().isOk
        }
    }
}
