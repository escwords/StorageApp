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

//scope to class to Account Activity scope
class UserRepository(
    val database: AppDatabase,
    val firebaseStorage: FirebaseStorage,
    private val firestore: FirebaseFirestore,
    private val userManager: UserManager
) {
    /*
    * 1. In this class we retrieve logged User data from room data base(i.e using exposing Room, for viewModel)
    * 2. we set up function to insert user on cloud firestore
    * 3. we set up function to insert user on Room database
    * 4. we set up function to update firestore data and  room data
    * 5. we set up function to prefetch firestore data and insert in room
    * 
    * */

    val loggedUser: LiveData<LoggedInUser> = database.loggedInUserDao().getUser()

    private val skilledDocument = firestore.collection("Skills").document(userManager.uid)

    private val skilledCollection = firestore.collection("Skills")

    private val source = Source.SERVER

    suspend fun prefetch() {
        withContext(Dispatchers.IO) {
            val userDocument = skilledDocument.get(source).await()
            val userObject = userDocument.toObject(RegisterUser::class.java)
            if (userObject != null) {
                val currentUser = userObject.toLoggedInUser()
                database.loggedInUserDao().insertUser(currentUser)
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun insert(user: RegisterUser) = flow<State<DocumentReference>> {
        val currentUser = user.apply {
            userId = userManager.uid
        }
        emit(State.loading())
        val insertRef = skilledCollection.add(currentUser).await()
        //emit the documentReference to enable me addOnCompleteListener
        emit(State.success(insertRef))

    }.catch {
        //if exception emit exception with state failed
        emit(State.failed(it.message.toString()))

    }.flowOn(Dispatchers.IO)

    /*fun updateUser(){

    }*/

}