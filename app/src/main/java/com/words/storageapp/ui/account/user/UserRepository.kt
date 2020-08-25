package com.words.storageapp.ui.account.user

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.storage.FirebaseStorage
import com.words.storageapp.database.AppDatabase
import com.words.storageapp.database.model.LoggedInUser
import com.words.storageapp.domain.RegisterUser
import com.words.storageapp.domain.toLoggedInUser
import com.words.storageapp.util.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

//scope to class to Account Activity scope
class UserRepository @Inject constructor(
    val database: AppDatabase
) {

    val loggedUser: LiveData<LoggedInUser> = database.loggedInUserDao().getUser()

    suspend fun updateUser(user: LoggedInUser): Boolean {
        return withContext(Dispatchers.IO) {
            database.loggedInUserDao().update(user)
            true
        }
    }

    suspend fun insert(user: LoggedInUser) {
        withContext(Dispatchers.IO) {
            database.loggedInUserDao().insertUser(user)
        }
    }

    suspend fun delete(): Boolean {
        return withContext(Dispatchers.IO) {
            database.loggedInUserDao().deleteUser()
            true
        }
    }

    suspend fun setUpAccount(user: LoggedInUser): Boolean {
        return withContext(Dispatchers.IO) {
            database.loggedInUserDao().setUpAccount(user)
            true
        }
    }


}