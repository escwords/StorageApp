package com.words.storageapp.ui.account.viewProfile

import androidx.lifecycle.*
import com.words.storageapp.database.model.LoggedInUser
import com.words.storageapp.domain.Photo
import com.words.storageapp.domain.RegisterUser
import com.words.storageapp.ui.account.user.UserRepository
import com.words.storageapp.util.utilities.Event
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    val userData: LiveData<LoggedInUser> = liveData {
        emitSource(repository.loggedUser)
    }

    val user = MutableLiveData<RegisterUser>()
    private val serverPhotos = MutableLiveData<List<Photo>>()

    val isAcctActive: LiveData<Event<Boolean?>> = userData.map {
        Event(it.accountActive) ?: Event(false)
    }


    fun initializeAccount(loggedInUser: LoggedInUser) {
        viewModelScope.launch {
            repository.setUpAccount(loggedInUser)
        }
    }

    fun updateAccount(loggedInUser: LoggedInUser) {
        viewModelScope.launch {
            repository.insert(loggedInUser)
        }
    }

}