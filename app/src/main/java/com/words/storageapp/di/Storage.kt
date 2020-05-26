package com.words.storageapp.di

interface Storage {
    fun getLocationProperty(key: String): String
    fun setLocationProperty(key: String, value: String)
}