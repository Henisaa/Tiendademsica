package com.example.tiendademsica.archivos

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CheckoutScreen(storeVM: StoreViewModel, onDone: () -> Unit) {
    val ctx = LocalContext.current
    var name by remember { mutableStateOf("") }
    var card by remember { mutableStateOf("") }
    var exp by remember { mutableStateOf("") }   // MM/AA
    var cvv by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { storeVM.loadCart(ctx) }

    fun luhnValid(number: String): Boolean {
        val digits = number.filter { it.isDigit() }
        if (digits.length !in 13..19) return false
        var sum = 0; var alt = false
        for (i in digits.length - 1 downTo 0) {
            var n = digits[i] - '0'
            if (alt) {
                n *= 2; if (n > 9) n -= 9
            }
            sum += n; alt = !alt
        }
        return sum % 10 == 0
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Pago", color = Color.White, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        Text("Total: $${"%.2f".format(storeVM.cartTotal.value)}", color = Color.White)

        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("Nombre en la tarjeta", color = Color.White) },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1DB954), unfocusedBorderColor = Color.DarkGray
            ), modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = card, onValueChange = { card = it },
            label = { Text("Número de tarjeta", color = Color.White) },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1DB954), unfocusedBorderColor = Color.DarkGray
            ), modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = exp, onValueChange = { exp = it },
                label = { Text("MM/AA", color = Color.White) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1DB954), unfocusedBorderColor = Color.DarkGray
                ), modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = cvv, onValueChange = { cvv = it },
                label = { Text("CVV", color = Color.White) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1DB954), unfocusedBorderColor = Color.DarkGray
                ), modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = address, onValueChange = { address = it },
            label = { Text("Dirección", color = Color.White) },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1DB954), unfocusedBorderColor = Color.DarkGray
            ), modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                val expOk = Regex("""^(0[1-9]|1[0-2])\/\d{2}$""").matches(exp)
                val cvvOk = cvv.length in 3..4 && cvv.all { it.isDigit() }
                if (name.isBlank() || address.isBlank() || !expOk || !cvvOk || !luhnValid(card)) {
                    Toast.makeText(ctx, "Revisa los datos de pago", Toast.LENGTH_SHORT).show()
                } else {
                    storeVM.clearCart(ctx)
                    Toast.makeText(ctx, "Pago exitoso. ¡Gracias!", Toast.LENGTH_LONG).show()
                    onDone()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Pagar ahora", color = Color.White) }

        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onDone) { Text("Cancelar", color = Color.White) }
    }
}
