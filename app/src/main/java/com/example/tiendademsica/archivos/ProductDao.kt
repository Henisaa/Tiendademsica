package com.example.tiendademsica.archivos

import androidx.room.*

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(p: Product)

    @Query("SELECT * FROM products ORDER BY id DESC")
    suspend fun all(): List<Product>

    @Query("SELECT * FROM products WHERE category = :cat ORDER BY id DESC")
    suspend fun byCategory(cat: Category): List<Product>

    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int
}
