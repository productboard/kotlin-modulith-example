package com.productboard.entities

import com.fasterxml.jackson.annotation.JsonValue
import java.util.UUID

data class EntityId(@JsonValue val id: UUID) {
    override fun toString(): String = id.toString()
}
