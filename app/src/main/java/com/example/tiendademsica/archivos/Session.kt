package com.example.tiendademsica.archivos

import androidx.compose.runtime.mutableStateOf

object Session {
    val currentUser = mutableStateOf<User?>(null)
    val isAdmin get() = currentUser.value?.isAdmin == true

    fun logout() {
        currentUser.value = null
    }
}
