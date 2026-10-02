package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Room que representa los productos del catálogo.
 * Contiene identificador único autogenerado y todos los atributos
 * necesarios para el CRUD local.
 */
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val price: Double,
    val description: String,
    val category: String,
    val iconName: String = "cart",
    val stock: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)
