package com.example.tiendademsica.archivos

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Category { DISCOS, ARTISTAS, OBJETOS }

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: Category,
    val price: Double,
    val imageUrl: String? = null,
)
