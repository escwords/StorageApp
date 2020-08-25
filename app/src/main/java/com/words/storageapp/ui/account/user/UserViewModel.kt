package com.words.storageapp.ui.account.user

import androidx.lifecycle.*
import com.words.storageapp.database.model.LoggedInUser

//This class fetches  logged user data from user Repository and exposes it between two UI
// viewProfile and EditProfile
class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    val userData: LiveData<LoggedInUser> = liveData {
        emitSource(repository.loggedUser)
    }


//    companion object {
//
//        class UserViewModelFactory(
//            private val repository: UserRepository,
//            private val userManager: UserManager
//        ) : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel?> create(modelClass: Class<T>) = UserViewModel(
//                repository
//            ) as T
//        }
//    }

}