package com.productboard.entities

import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EntityRepository : CrudRepository<DbEntity, UUID> {
    fun getDbEntitiesById(id: UUID): DbEntity?

    fun deleteDbEntityById(id: UUID): Int
}
