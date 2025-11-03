
@file:OptIn(ExperimentalMaterial3Api::class)


package com.example.tiendademsica.archivos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

// NOTA: Asegúrate de importar tus clases personalizadas
// import com.example.tiendademsica.viewmodel.StoreViewModel
// import com.example.tiendademsica.data.Session

@Composable
fun AppScaffold(
    storeVM: StoreViewModel,
    onCartClick: () -> Unit,
    onLogout: () -> Unit,
    onLogin: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Music Store", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
                actions = {
                    // Carrito con badge (siempre)
                    BadgedBox(badge = {
                        if (storeVM.cartCount.value > 0) {
                            Badge { Text(storeVM.cartCount.value.toString()) }
                        }
                    }) {
                        TextButton(onClick = onCartClick) {
                            Text("Carrito", color = Color.White)
                        }
                    }
                    // Cerrar sesión (si hay usuario)
                    if (Session.currentUser.value != null) {
                        TextButton(onClick = onLogout) {
                            Text("Cerrar sesión", color = Color.White)
                        }
                    } else {
                        TextButton(onClick = onLogin){
                            Text("Iniciar Sesion", color = Color.White)
                        }
                    }
                }
            )
        },
        containerColor = Color.Black,
        contentColor = Color.White
    ) { padding ->
        Box(Modifier.padding(padding).background(Color.Black)) {
            content()
        }
    }
}