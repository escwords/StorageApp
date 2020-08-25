package com.words.storageapp.di

//import android.content.Context
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.storage.FirebaseStorage
//import com.words.storageapp.ui.account.user.UserManager
//import com.words.storageapp.ui.account.user.UserRepository
//import com.words.storageapp.database.AppDatabase
//import com.words.storageapp.ui.account.user.UserViewModel
//import com.words.storageapp.ui.search.SearchRepository
//
//class AppDiContainer(context: Context) {
//
//    //lazy is used here for late initialization
//    val userManager: UserManager by lazy {
//        UserManager(SharedPreference(context))
//    }
//
//    val appDatabase: AppDatabase by lazy {
//        AppDatabase.getInstance(context)
//    }
//
//    val searchRepository: SearchRepository by lazy {
//        SearchRepository(
//            appDatabase
//        )
//    }
//}
