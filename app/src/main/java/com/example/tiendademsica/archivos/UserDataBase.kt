package com.example.tiendademsica.archivos

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [User::class, Product::class, CartItem::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile private var INSTANCE: UserDatabase? = null

        fun getDatabase(ctx: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    ctx.applicationContext,
                    UserDatabase::class.java,
                    "music_store_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(seedCallback())
                    .build()
                INSTANCE = db
                db
            }
        }

        private fun seedCallback() = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Seed async
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val udao = database.userDao()
                        val pdao = database.productDao()
                        // admin
                        if (udao.byUsername("admin") == null) {
                            udao.insert(
                                User(
                                    username = "admin",
                                    email = "admin@store.com",
                                    password = "admin123",
                                    isAdmin = true
                                )
                            )
                        }
                        // 3 productos iniciales
                        if (pdao.count() == 0) {
                            pdao.insert(Product(title="Álbum: Random Access Memories", category=Category.DISCOS, price=12.99))
                            pdao.insert(Product(title="Artista: Daft Punk - Merch", category=Category.ARTISTAS, price=25.00))
                            pdao.insert(Product(title="Vinilo: The Dark Side of the Moon", category=Category.OBJETOS, price=29.90))
                        }
                    }
                }
            }
        }
    }
}

class Converters {
    @TypeConverter fun toCategory(value: String): Category = Category.valueOf(value)
    @TypeConverter fun fromCategory(cat: Category): String = cat.name
}

