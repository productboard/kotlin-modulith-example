package com.productboard.entities

import mu.KLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EntityServiceImpl(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val entitiesRepository: EntityRepository,
) : EntityService {

    override fun listAllEntities(): List<Entity> = entitiesRepository.findAll().map(::toApiEntity)

    override fun getEntityById(id: EntityId): Entity? = entitiesRepository.getDbEntitiesById(id.id)?.let(::toApiEntity)

    @Transactional
    override fun createEntity(entityCreate: EntityCreate): Entity {
        val createdEntity = entitiesRepository.save(entityCreate.toDbEntity()).let(::toApiEntity)
        logger.info { "action=entityCreate id=${createdEntity.id}" }
        applicationEventPublisher.publishEvent(EntityCreatedEvent(createdEntity))
        return createdEntity
    }

    @Transactional
    override fun deleteEntity(id: EntityId): Boolean {
        val wasDeleted = entitiesRepository.deleteDbEntityById(id.id) > 0
        if (wasDeleted) {
            applicationEventPublisher.publishEvent(EntityDeletedEvent(id))
        }
        return wasDeleted
    }

    private fun toApiEntity(dbEntity: DbEntity): Entity =
        Entity(id = EntityId(requireNotNull(dbEntity.id)), name = dbEntity.name)

    companion object : KLogging()
}

private fun EntityCreate.toDbEntity(): DbEntity = DbEntity(name = this.name)
