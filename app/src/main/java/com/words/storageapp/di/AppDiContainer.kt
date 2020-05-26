package com.words.storageapp.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.words.storageapp.ui.account.user.UserManager
import com.words.storageapp.ui.account.user.UserRepository
import com.words.storageapp.ui.account.user.UserViewModel.Companion.UserViewModelFactory
import com.words.storageapp.database.AppDatabase
import com.words.storageapp.ui.main.SearchRepository

class AppDiContainer(context: Context) {

    private val sharedPreference = SharedPreference(context)

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val appDatabase = AppDatabase.getInstance(context)

    val userManager = UserManager(
        sharedPreference,
        firebaseAuth
    )

    val fireStore = FirebaseFirestore.getInstance()

    val firebaseStorage = FirebaseStorage.getInstance()


    /* val userRepository = UserRepository(
         appDatabase,firebaseStorage,fireStore,userManager
     )*/

    val searchRepository = SearchRepository(fireStore, appDatabase, userManager)

    var userFlowContainer: UserFlowContainer? = null

}

class UserFlowContainer(
    appDatabase: AppDatabase,
    firestore: FirebaseFirestore,
    firebaseStorage: FirebaseStorage,
    userManager: UserManager
) {

    val userRepository = UserRepository(
        appDatabase,
        firebaseStorage,
        firestore,
        userManager
    )

    val userViewModelFactory = UserViewModelFactory(userRepository, userManager)
}
