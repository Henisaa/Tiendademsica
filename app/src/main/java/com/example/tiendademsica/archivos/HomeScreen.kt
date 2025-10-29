@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.tiendademsica.archivos
import androidx.compose.material3.ExperimentalMaterial3Api
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
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
                    // Ya no necesitamos .align() ni .padding() aquí,
                    // el Scaffold lo posiciona automáticamente.
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
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(p.title, color = Color.White)
                    TextButton(onClick = { storeVM.addToCart(ctx, p); Toast.makeText(ctx, "Añadido", Toast.LENGTH_SHORT).show() }) {
                        Text("Agregar", color = Color(0xFF1DB954))
                    }
                }
            }
        }
    }


}
