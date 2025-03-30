package com.productboard.entities

import java.util.UUID
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class EntityServiceTest(
    @Autowired private val entitiesService: EntityServiceImpl,
    @Autowired private val entitiesRepository: EntityRepository,
) : EntitiesTest() {

    private val entityCreate = EntityCreate("My entity")
    private val dbEntity = DbEntity(name = entityCreate.name)

    @Test
    fun `should list all entities`() {
        // Given
        val entities = List(5) { DbEntity(name = "Entity$it") }
        entitiesRepository.saveAll(entities)

        // When
        val result = entitiesService.listAllEntities()

        // Then
        assertEquals(entities.size, result.size)
    }

    @Test
    fun `should get entity by id`() {
        // Given
        val createdEntity = entitiesRepository.save(dbEntity)

        // When
        val result = entitiesService.getEntityById(EntityId(createdEntity.id!!))

        // Then
        assertNotNull(result)
        assertEquals(createdEntity.name, result?.name)
    }

    @Test
    fun `should create entity`() {
        // When
        val result = entitiesService.createEntity(entityCreate)

        // Then
        assertNotNull(result)
        assertEquals(entityCreate.name, result.name)
    }

    @Test
    fun `should return false when deleted not existing entity`() {
        // When
        val result = entitiesService.deleteEntity(randomEntityId())

        // Then
        assertFalse(result)
    }

    @Test
    fun `should return true when deleted existing entity`() {
        // Given
        val entity = entitiesRepository.save(dbEntity)

        // When
        val result = entitiesService.deleteEntity(EntityId(entity.id!!))

        // Then
        assertTrue(result)
    }
}

private fun randomEntityId() = EntityId(UUID.randomUUID())
