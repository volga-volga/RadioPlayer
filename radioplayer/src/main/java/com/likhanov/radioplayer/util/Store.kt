package com.likhanov.radioplayer.util

import android.content.Context
import android.content.SharedPreferences

object Store {
    private const val PREF_NAME = "Player_store"
    const val PREF_CHANNEL = "channel"
    const val PREF_USER_ID = "user_id"

    private lateinit var pref: SharedPreferences

    fun init(context: Context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun storeData(tag: String, data: Any) {
        pref.edit().apply {
            when (data) {
                is String -> putString(tag, data)
                is Boolean -> putBoolean(tag, data)
                is Int -> putInt(tag, data)
                is Float -> putFloat(tag, data)
                is Long -> putLong(tag, data)
            }
            apply()
        }
    }

    fun getString(tag: String) = pref.getString(tag, "") ?: ""
}