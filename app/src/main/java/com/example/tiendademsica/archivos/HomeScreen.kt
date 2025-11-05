@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.tiendademsica.archivos
import androidx.compose.material3.ExperimentalMaterial3Api
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis

@Composable
fun HomeScreen(storeVM: StoreViewModel, onAddProduct: () -> Unit) {
    val ctx = LocalContext.current
    var tab by remember { mutableStateOf(0) }
    val tabs = listOf("Todo", "Discos", "Artistas", "Objetos")

    LaunchedEffect(tab) {
        val cat = when (tab) {
            1 -> Category.DISCOS
            2 -> Category.ARTISTAS
            3 -> Category.OBJETOS
            else -> null
        }
        storeVM.loadProducts(ctx, cat)
        storeVM.loadCart(ctx)
    }

    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = tab, containerColor = Color.Black, contentColor = Color.White) {
                tabs.forEachIndexed { i, t ->
                    Tab(selected = tab == i, onClick = { tab = i }, text = { Text(t, color = Color.White) })
                }
            }
        },
        floatingActionButton = {
            if (Session.isAdmin) {
                FloatingActionButton(
                    onClick = onAddProduct,
                    containerColor = Color(0xFF1DB954)
                ) { Text("+", color = Color.White) }
            }
        }
    ) { innerPadding ->

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {

            item {
                Spacer(Modifier.height(12.dp))
            }


            items(storeVM.products) { p ->
                val imageRes = p.imageUrl?.let {
                    ctx.resources.getIdentifier(it, "drawable", ctx.packageName)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1) image
                    if (imageRes != null && imageRes != 0) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = p.title,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // 2) text + button
                    Column(Modifier.weight(1f)) {
                        Text(p.title, color = Color.White, maxLines = 1, overflow = Ellipsis)
                        Text("$${"%.2f".format(p.price)}", color = Color.Gray)
                        Text("Stock: ${p.stock}", color = if (p.stock > 0) Color.Gray else Color(0xFFFF6B6B))
                        Spacer(Modifier.height(4.dp))
                        TextButton(
                            onClick = {
                                storeVM.addToCart(ctx, p)
                                Toast.makeText(ctx, "Añadido", Toast.LENGTH_SHORT).show()
                            },
                            enabled = p.stock > 0,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(if (p.stock > 0) "Agregar" else "Agotado",
                                color = if (p.stock > 0) Color(0xFF1DB954) else Color.Gray,
                                maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}
