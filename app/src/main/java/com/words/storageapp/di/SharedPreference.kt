package com.words.storageapp.di

import android.content.Context

class SharedPreference(context: Context) : Storage {

    val sharedPref = context.getSharedPreferences("Wokr", Context.MODE_PRIVATE)

    override fun getLocationProperty(key: String): String {
        return sharedPref.getString(key, "")!!
    }

    override fun setLocationProperty(key: String, value: String) {
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }


}