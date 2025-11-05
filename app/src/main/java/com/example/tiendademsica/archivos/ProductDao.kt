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

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun byId(id: Int): Product?

    @Query("SELECT * FROM products WHERE title = :title LIMIT 1")
    suspend fun byTitle(title: String): Product?

    @Query("UPDATE products SET stock = stock + :delta WHERE id = :id")
    suspend fun changeStock(id: Int, delta: Int)

    @Query("UPDATE products SET stock = :newStock WHERE id = :id")
    suspend fun setStock(id: Int, newStock: Int)
}
