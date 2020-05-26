package com.words.storageapp.ui.account.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.words.storageapp.database.model.LoggedInUser
import kotlinx.coroutines.launch

//This class fetches  logged user data from user Repository and exposes it between two UI
// viewProfile and EditProfile
class UserViewModel(
    private val repository: UserRepository,
    val userManager: UserManager
) : ViewModel() {

    val userData: LiveData<LoggedInUser> = repository.loggedUser

    init {
        initializeData()
    }

    private fun initializeData() {
        viewModelScope.launch {
            repository.prefetch()
        }
    }

    fun updateUser() {

    }

    companion object {

        class UserViewModelFactory(
            private val repository: UserRepository,
            private val userManager: UserManager
        ) : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>) = UserViewModel(
                repository,
                userManager
            ) as T
        }
    }

}