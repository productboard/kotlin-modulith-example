package com.productboard.restapi

import com.productboard.entities.Entity
import com.productboard.entities.EntityCreate
import com.productboard.entities.EntityId
import com.productboard.entities.EntityService
import java.util.UUID
import mu.KLogging
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/entities")
class EntityController(private val entityService: EntityService) {

    @GetMapping fun getAllEntities(): List<Entity> = entityService.listAllEntities()

    @PostMapping
    fun createEntity(@RequestBody entityCreate: EntityCreate): Entity =
        entityService.createEntity(entityCreate).also { logger.info { "action=createEntityRestApi id=${it.id}" } }

    @GetMapping("/{id}")
    fun getEntityById(@PathVariable id: String): Entity? = entityService.getEntityById(EntityId(UUID.fromString(id)))

    @DeleteMapping("/{id}")
    fun deleteEntity(@PathVariable id: String): Boolean = entityService.deleteEntity(EntityId(UUID.fromString(id)))

    companion object : KLogging()
}
