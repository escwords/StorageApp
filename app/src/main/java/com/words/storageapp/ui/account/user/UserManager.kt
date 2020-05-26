package com.words.storageapp.ui.account.user

import android.location.Location
import com.google.firebase.auth.FirebaseAuth
import com.words.storageapp.di.Storage

class UserManager(
    private val storage: Storage,
    firebaseAuth: FirebaseAuth
) {
    val uid by lazy {
        firebaseAuth.currentUser!!.uid
    }

    val uAddress: String = storage.getLocationProperty("Address")
    val uLongitude: Double? = storage.getLocationProperty("latitude").toDoubleOrNull()
    val uLatitude: Double? = storage.getLocationProperty("longitude").toDoubleOrNull()

    // var locationMain: Location? = null

    fun setUpLocation(location: Location) {
        storage.setLocationProperty("latitude", location.latitude.toString())
        storage.setLocationProperty("longitude", location.longitude.toString())
        // locationMain = location
    }

    fun setUpAddress(address: String) {
        storage.setLocationProperty("Address", address)
    }

}