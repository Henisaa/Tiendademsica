package com.example.tiendademsica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tiendademsica.archivos.AddProductScreen
import com.example.tiendademsica.archivos.AppScaffold
import com.example.tiendademsica.archivos.CartScreen
import com.example.tiendademsica.archivos.CheckoutScreen
import com.example.tiendademsica.archivos.HomeScreen
import com.example.tiendademsica.archivos.LoginScreen
import com.example.tiendademsica.archivos.RegisterScreen
import com.example.tiendademsica.archivos.Session
import com.example.tiendademsica.archivos.StoreViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ctx = this@MainActivity

        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                val nav = rememberNavController()
                val storeVM = remember { StoreViewModel() }

                AppScaffold(
                    storeVM = storeVM,
                    onCartClick = { nav.navigate("cart") },
                    onLogout = {
                        Session.logout()
                        storeVM.clearCart(ctx)
                        nav.navigate("login") { popUpTo(0) }
                    }
                ) {
                    NavHost(navController = nav, startDestination = "login") {

                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    nav.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onRegisterClick = { nav.navigate("register") }
                            )
                        }

                        composable("register") {
                            RegisterScreen(
                                onRegisterSuccess = { nav.popBackStack() },
                                onBackToLogin = { nav.popBackStack() }
                            )
                        }

                        composable("home") {
                            HomeScreen(
                                storeVM = storeVM,
                                onAddProduct = { nav.navigate("add") }
                            )
                        }

                        composable("add") {
                            AddProductScreen(
                                storeVM = storeVM,
                                onBack = { nav.popBackStack() }
                            )
                        }

                        composable("cart") {
                            CartScreen(
                                storeVM = storeVM,
                                onCheckout = { nav.navigate("checkout") },
                                onBack = { nav.popBackStack() }
                            )
                        }

                        composable("checkout") {
                            CheckoutScreen(
                                storeVM = storeVM,
                                onDone = {
                                    nav.navigate("home") {
                                        popUpTo("home") { inclusive = false }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
