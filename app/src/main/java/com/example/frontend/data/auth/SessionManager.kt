package com.example.frontend.data.auth

import android.content.Context
import android.content.SharedPreferences

class SessionManager(ctx: Context) {
    
    private val prefs: SharedPreferences = ctx.getSharedPreferences("session", Context.MODE_PRIVATE)
    
    fun saveLogin(login: String) {
        prefs.edit().putString("login", login).apply()
    }
    
    fun savePassword(pass: String) {
        prefs.edit().putString("password", pass).apply()
    }
    
    // сохраняет логин и пароль одновременно
    fun saveCredentials(login: String, pass: String) {
        val e = prefs.edit()
        e.putString("login", login)
        e.putString("password", pass)
        e.apply()
    }
    
    fun getLogin(): String {
        return prefs.getString("login", "") ?: ""
    }
    
    fun getPassword(): String {
        return prefs.getString("password", "") ?: ""
    }
    
    fun saveToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }
    
    fun getAuthToken(): String {
        return prefs.getString("token", "") ?: ""
    }
    
    fun saveUserId(id: Long) {
        prefs.edit().putLong("userId", id).apply()
    }
    
    fun saveUserName(name: String) {
        prefs.edit().putString("userName", name).apply()
    }
    
    fun saveUserInfo(id: Long, name: String, dept: String) {
        val e = prefs.edit()
        e.putLong("userId", id)
        e.putString("userName", name)
        e.putString("department", dept)
        e.apply()
    }
    
    fun getUserId(): Long {
        return prefs.getLong("userId", 0)
    }
    
    fun getUserName(): String {
        return prefs.getString("userName", "") ?: ""
    }
    
    fun getUserDepartment(): String {
        return prefs.getString("department", "") ?: ""
    }
    
    fun clearAll() {
        prefs.edit().clear().apply()
    }
    
    // проверяет есть ли сохраненные данные
    fun isLoggedIn(): Boolean {
        val l = getLogin()
        val p = getPassword()
        return l.isNotEmpty() && p.isNotEmpty()
    }
}
