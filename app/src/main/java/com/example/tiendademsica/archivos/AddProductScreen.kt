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
fun AddProductScreen(storeVM: StoreViewModel, onBack: () -> Unit) {
    if (!Session.isAdmin) { onBack(); return }

    val ctx = LocalContext.current
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(Category.DISCOS) }
    var adminPass by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Nuevo producto", color = Color.White, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(value = title, onValueChange = { title = it },
            label = { Text("Título", color = Color.White) },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1DB954), unfocusedBorderColor = Color.DarkGray
            ), modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = price, onValueChange = { price = it },
            label = { Text("Precio", color = Color.White) },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1DB954), unfocusedBorderColor = Color.DarkGray
            ), modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // Selector simple de categoría
        Row {
            Category.values().forEach {
                FilterChip(
                    selected = category == it,
                    onClick = { category = it },
                    label = { Text(it.name.lowercase().replaceFirstChar(Char::uppercase), color = Color.White) },
                    leadingIcon = null,
                    modifier = Modifier.padding(end = 8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.DarkGray,
                        selectedContainerColor = Color(0xFF1DB954)
                    )
                )
            }
        }
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = adminPass, onValueChange = { adminPass = it },
            label = { Text("Contraseña admin (confirmación)", color = Color.White) },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1DB954), unfocusedBorderColor = Color.DarkGray
            ), modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val p = price.toDoubleOrNull()
                if (title.isBlank() || p == null) {
                    Toast.makeText(ctx, "Completa los datos", Toast.LENGTH_SHORT).show()
                } else {
                    storeVM.addProduct(
                        ctx,
                        Product(title = title, category = category, price = p),
                        adminPassword = adminPass
                    ) { ok ->
                        if (ok) { Toast.makeText(ctx, "Producto añadido", Toast.LENGTH_SHORT).show(); onBack() }
                        else    { Toast.makeText(ctx, "Credencial admin inválida", Toast.LENGTH_SHORT).show() }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954))
        ) { Text("Publicar", color = Color.White) }
    }
}
