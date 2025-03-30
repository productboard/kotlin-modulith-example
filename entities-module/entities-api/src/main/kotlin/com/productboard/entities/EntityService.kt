package com.productboard.entities

import com.productboard.modulith.PublicModuleInterface

interface EntityService : PublicModuleInterface {
    fun listAllEntities(): List<Entity>

    fun getEntityById(id: EntityId): Entity?

    fun createEntity(entityCreate: EntityCreate): Entity

    fun deleteEntity(id: EntityId): Boolean
}
