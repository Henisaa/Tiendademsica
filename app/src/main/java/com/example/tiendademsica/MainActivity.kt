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
import com.example.tiendademsica.archivos.QuienesSomosScreen
import com.example.tiendademsica.archivos.SplashScreen

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
                        nav.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onLogin = {
                        nav.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onAboutClick = {
                        nav.navigate("about")   // 👈 now wired
                    },
                    onLogoClick = {
                        nav.navigate("home"){
                            popUpTo("home") { inclusive = true }
                        }
                    }
                ) {
                    NavHost(navController = nav, startDestination = "splash") {

                        composable("splash") {
                            SplashScreen(
                                onFinished = {
                                    // Decide where to land; you can send to "home" directly
                                    nav.navigate("home") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                },
                                logoResId = R.drawable.musiclogo // put a real drawable name here
                            )
                        }

                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    nav.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onRegisterClick = { nav.navigate("register") },
                                onBack = { nav.navigate("home") {popUpTo("home") { inclusive = false} }
                                }
                            )
                        }

                        composable("register") {
                            RegisterScreen(
                                onRegisterSuccess = { nav.popBackStack() },
                                onBackToLogin = { nav.navigate("login") },
                                onBack = {nav.navigate("home")}
                            )
                        }

                        composable("home") {
                            HomeScreen(
                                storeVM = storeVM,
                                onAddProduct = { nav.navigate("add") }
                            )
                        }

                        composable("about") {
                            // you said: don’t use popBackStack → use navigate
                            QuienesSomosScreen(onBack = {
                                nav.navigate("home") { popUpTo("home") { inclusive = false } }
                            })
                        }
                        composable("add") {
                            AddProductScreen(
                                storeVM = storeVM,
                                onBack = {
                                    nav.navigate("home") {
                                        popUpTo("home") { inclusive = false }
                                    }
                                }
                            )
                        }

                        composable("cart") {
                            CartScreen(
                                storeVM = storeVM,
                                onCheckout = { nav.navigate("checkout") },
                                onBack = {
                                    nav.navigate("home") {
                                        popUpTo("home") { inclusive = false }
                                    }
                                }
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
