package com.productboard.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "entities")
data class DbEntity(@Id @GeneratedValue(strategy = GenerationType.AUTO) val id: UUID? = null, val name: String)
