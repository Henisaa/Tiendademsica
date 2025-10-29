package com.example.tiendademsica.archivos

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    fun login(ctx: Context, username: String, password: String, onResult: (Boolean)->Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val db = UserDatabase.getDatabase(ctx)
            val user = db.userDao().login(username, password)
            Session.currentUser.value = user
            onResult(user != null)
        }
    }
}
