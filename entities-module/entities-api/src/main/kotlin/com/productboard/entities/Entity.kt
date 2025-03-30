package com.productboard.entities

import com.fasterxml.jackson.annotation.JsonProperty

data class Entity(val id: EntityId, val name: String)

data class EntityCreate(@JsonProperty val name: String)
