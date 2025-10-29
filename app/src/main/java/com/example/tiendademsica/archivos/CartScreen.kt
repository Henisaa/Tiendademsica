package com.example.tiendademsica.archivos

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun CartScreen(storeVM: StoreViewModel, onCheckout: () -> Unit, onBack: () -> Unit) {
    val ctx = LocalContext.current
    LaunchedEffect(Unit) { storeVM.loadCart(ctx) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Tu carrito", color = Color.White, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        if (storeVM.cart.isEmpty()) {
            Text("No hay productos aún.", color = Color.Gray)
        } else {
            storeVM.cart.forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(row.product.title, color = Color.White)
                        Text("$${"%.2f".format(row.product.price)} x ${row.item.quantity}", color = Color.Gray)
                    }
                    Row {
                        TextButton({ storeVM.dec(ctx, row.product.id) }) { Text("-", color = Color.White) }
                        Text("${row.item.quantity}", color = Color.White)
                        TextButton({ storeVM.inc(ctx, row.product.id) }) { Text("+", color = Color.White) }
                        TextButton({ storeVM.remove(ctx, row.product.id) }) { Text("Quitar", color = Color.White) }
                    }
                }
                Divider(color = Color.DarkGray)
            }

            Spacer(Modifier.height(12.dp))
            Text("Total: $${"%.2f".format(storeVM.cartTotal.value)}", color = Color.White, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (Session.currentUser.value == null) {
                        Toast.makeText(ctx, "Inicia sesión para continuar", Toast.LENGTH_SHORT).show()
                    } else onCheckout()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Ir a pagar", color = Color.White) }
        }

        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onBack) { Text("Volver", color = Color.White) }
    }
}
