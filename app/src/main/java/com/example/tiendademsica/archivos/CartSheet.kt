package com.example.tiendademsica.archivos

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartSheet(storeVM: StoreViewModel, onClose: () -> Unit) {
    val ctx = LocalContext.current
    LaunchedEffect(Unit) { storeVM.loadCart(ctx) }

    ModalBottomSheet(onDismissRequest = onClose, containerColor = Color(0xFF111111)) {
        Column(Modifier.padding(16.dp)) {
            Text("Tu carrito", color = Color.White, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))
            storeVM.cart.forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(row.product.title, color = Color.White)
                    Row {
                        TextButton({ storeVM.dec(ctx, row.product.id) }) { Text("-", color = Color.White) }
                        Text("${row.item.quantity}", color = Color.White)
                        TextButton({ storeVM.inc(ctx, row.product.id) }) { Text("+", color = Color.White) }
                        TextButton({ storeVM.remove(ctx, row.product.id) }) { Text("Quitar", color = Color.White) }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = { storeVM.remove(ctx, -1) }) { /* no-op */ }
        }
    }
}
