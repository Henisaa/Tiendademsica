package com.example.tiendademsica.archivos

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoreViewModel : ViewModel() {


    val products = mutableStateListOf<Product>()

    val cart = mutableStateListOf<CartItemWithProduct>()
    val cartCount = mutableStateOf(0)
    val cartTotal = mutableStateOf(0.0)

    fun loadProducts(ctx: Context, category: Category? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val dao = UserDatabase.getDatabase(ctx).productDao()
            val list = if (category == null) dao.all() else dao.byCategory(category)
            products.clear()
            products.addAll(list)
        }
    }

    fun addProduct(
        ctx: Context,
        product: Product,
        adminPassword: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val db = UserDatabase.getDatabase(ctx)
            val admin = db.userDao().byUsername("admin")
            val ok = admin != null && admin.password == adminPassword

            if (ok) {
                db.productDao().insert(product)
                val all = db.productDao().all()

                withContext(Dispatchers.Main) {
                    products.clear()
                    products.addAll(all)
                    onResult(true)
                }
            } else {
                withContext(Dispatchers.Main) {
                    onResult(false)
                }
            }
        }
    }

    fun loadCart(ctx: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val cdao = UserDatabase.getDatabase(ctx).cartDao()
            val list = cdao.allWithProduct()
            val count = cdao.totalUnits()
            val total = list.sumOf { it.product.price * it.item.quantity }

            cart.clear()
            cart.addAll(list)
            cartCount.value = count
            cartTotal.value = total
        }
    }

    fun addToCart(ctx: Context, product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            val db = UserDatabase.getDatabase(ctx)
            db.cartDao().upsert(CartItem(productId = product.id, quantity = 1))
            loadCart(ctx)
        }
    }

    fun inc(ctx: Context, productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            UserDatabase.getDatabase(ctx).cartDao().inc(productId)
            loadCart(ctx)
        }
    }

    fun dec(ctx: Context, productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            UserDatabase.getDatabase(ctx).cartDao().dec(productId)
            loadCart(ctx)
        }
    }

    fun remove(ctx: Context, productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            UserDatabase.getDatabase(ctx).cartDao().remove(productId)
            loadCart(ctx)
        }
    }

    fun clearCart(ctx: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            UserDatabase.getDatabase(ctx).cartDao().clear()
            loadCart(ctx)
        }
    }
}
