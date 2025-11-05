package com.example.tiendademsica.archivos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuienesSomosScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiénes Somos", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
                actions = {
                    TextButton(onClick = onBack) { Text("Volver", color = Color.White) }
                }
            )
        },
        containerColor = Color.Black,
        contentColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Bienvenido a *Tienda de Música*, tu espacio dedicado al arte sonoro y la cultura musical. " +
                        "Aquí encontrarás discos, vinilos y artículos relacionados con tus artistas favoritos.",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                "Nuestro objetivo es ofrecer una experiencia simple y elegante para explorar el mundo de la música, " +
                        "demostrando las capacidades de las herramientas modernas de Android.",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
