package com.words.storageapp.ui.main

import com.google.firebase.firestore.FirebaseFirestore
import org.imperiumlabs.geofirestore.GeoFirestore

class MainRepository(

) {

    private val collectionRef = FirebaseFirestore.getInstance()
        .collection("skills")

    val geoFireStore = GeoFirestore(collectionRef)


    fun prefetch() {


    }


}