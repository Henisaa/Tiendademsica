package com.example.tiendademsica.archivos

import androidx.room.*

@Entity(
    tableName = "cart_items",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("productId")]
)
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val quantity: Int = 1
)

data class CartItemWithProduct(
    @Embedded val item: CartItem,
    @Relation(parentColumn = "productId", entityColumn = "id")
    val product: Product
)

@Dao
interface CartDao {
    // NEW: try to increment existing row; returns affected rows
    @Query("UPDATE cart_items SET quantity = quantity + 1 WHERE productId = :pid")
    suspend fun incIfExists(pid: Int): Int

    // NEW: plain insert (no REPLACE on auto PK)
    @Insert
    suspend fun insert(item: CartItem): Long

    // keep the rest
    @Transaction
    @Query("SELECT * FROM cart_items")
    suspend fun allWithProduct(): List<CartItemWithProduct>

    @Query("UPDATE cart_items SET quantity = quantity + 1 WHERE productId = :pid")
    suspend fun inc(pid: Int)

    @Query("UPDATE cart_items SET quantity = quantity - 1 WHERE productId = :pid AND quantity > 1")
    suspend fun dec(pid: Int)

    @Query("DELETE FROM cart_items WHERE productId = :pid")
    suspend fun remove(pid: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clear()

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart_items")
    suspend fun totalUnits(): Int
}
