package com.productboard.entities

data class EntityCreatedEvent(val entity: Entity)

data class EntityDeletedEvent(val id: EntityId)
