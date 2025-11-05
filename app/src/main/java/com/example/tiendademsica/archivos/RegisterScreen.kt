package com.example.tiendademsica.archivos

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val db = remember { UserDatabase.getDatabase(context) }
    val dao = db.userDao()
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Crear cuenta",
                color = Color.White,
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario", color = Color.White) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1DB954),
                    unfocusedBorderColor = Color.DarkGray
                ),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico", color = Color.White) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1DB954),
                    unfocusedBorderColor = Color.DarkGray
                ),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = Color.White) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1DB954),
                    unfocusedBorderColor = Color.DarkGray
                ),
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
            )

            Button(
                onClick = {
                    scope.launch {
                        when {
                            username.isBlank() || email.isBlank() || password.isBlank() -> {
                                Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                            }

                            dao.byUsername(username) != null -> {
                                Toast.makeText(context, "El usuario ya existe", Toast.LENGTH_SHORT).show()
                            }
                            !email.contains("@") || email.endsWith("@yopmail.com") -> {
                                Toast.makeText(context, "Correo no válido o no permitido", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                
                                dao.insert(User(username = username, email = email, password = password))
                                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                onRegisterSuccess()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB9-4))
            ) {
                Text("Registrar", color = Color.White)
            }

            TextButton(onClick = { onBackToLogin() }) {
                Text("¿Ya tienes cuenta? Inicia sesión", color = Color.White)
            }
            TextButton(onClick = { onBack() }){
                Text("Volver", color = Color.White)
            }
        }
    }
}