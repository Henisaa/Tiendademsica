@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tiendademsica.archivos

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.tiendademsica.R

@Composable
fun AppScaffold(
    storeVM: StoreViewModel,
    onCartClick: () -> Unit,
    onLogout: () -> Unit,
    onLogin: () -> Unit,
    onAboutClick: () -> Unit,
    onLogoClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.Black,
                drawerContentColor = Color.White
            ) {
                Spacer(Modifier.height(8.dp))

                // Header logo (tap to go Home)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clickable {
                            scope.launch { drawerState.close() }
                            onLogoClick()
                        },
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.musiclogo),
                        contentDescription = "Logo",
                        modifier = Modifier.height(40.dp)
                    )
                }

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onLogoClick()
                    }
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ShoppingCart, null) },
                    label = {
                        Row {
                            Text("Carrito")
                            if (storeVM.cartCount.value > 0) {
                                Spacer(Modifier.width(8.dp))
                                Badge { Text(storeVM.cartCount.value.toString()) }
                            }
                        }
                    },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onCartClick()
                    }
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Info, null) },
                    label = { Text("Quiénes Somos") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onAboutClick()
                    }
                )

                if (Session.isAdmin) {
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Add, null) },
                        label = { Text("Añadir producto") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            // Reuse your FAB navigation: let Home handle it via nav
                            // Easiest is: navigate from MainActivity when you add a drawer callback
                            // or expose another callback here if you want.
                            // For now: call onAboutClick() or create a dedicated lambda if needed.
                            // (Most people add an onAddProduct: () -> Unit. If you want it, add it.)
                        }
                    )
                }

                Divider(Modifier.padding(vertical = 8.dp), color = Color.DarkGray)

                if (Session.currentUser.value != null) {
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.ExitToApp, null) },
                        label = { Text("Cerrar sesión") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            onLogout()
                        }
                    )
                } else {
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Person, null) },
                        label = { Text("Iniciar sesión") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            onLogin()
                        }
                    )
                }
            }
        }
    ) {
        // Keep a simple top bar with only the hamburger + clickable logo
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                        }
                    },
                    title = {
                        Image(
                            painter = painterResource(id = R.drawable.musiclogo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .height(40.dp)
                                .clickable { onLogoClick() }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
                )
            },
            containerColor = Color.Black,
            contentColor = Color.White
        ) { padding ->
            Box(Modifier.padding(padding).background(Color.Black)) { content() }
        }
    }
}
