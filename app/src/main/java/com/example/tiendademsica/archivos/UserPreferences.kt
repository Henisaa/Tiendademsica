package com.example.tiendademsica.archivos

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject



class UserPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("users_data", Context.MODE_PRIVATE)

    fun getUsers(): MutableList<User> {
        val jsonString = prefs.getString("users", "[]")
        val jsonArray = JSONArray(jsonString)
        val users = mutableListOf<User>()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            users.add(
                User(
                    username = obj.getString("username"),
                    email = obj.getString("email"),
                    password = obj.getString("password")
                )
            )
        }
        return users
    }

    fun userExists(username: String): Boolean {
        return getUsers().any { it.username.equals(username, ignoreCase = true) }
    }

    fun saveUser(user: User) {
        val users = getUsers()
        users.add(user)

        val jsonArray = JSONArray()
        for (u in users) {
            val obj = JSONObject()
            obj.put("username", u.username)
            obj.put("email", u.email)
            obj.put("password", u.password)
            jsonArray.put(obj)
        }

        prefs.edit().putString("users", jsonArray.toString()).apply()
    }

    fun validateLogin(username: String, password: String): Boolean {
        return getUsers().any { it.username == username && it.password == password }
    }
}
