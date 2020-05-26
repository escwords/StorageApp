package com.words.storageapp.ui.account.createProfile

import androidx.lifecycle.*
import com.words.storageapp.ui.account.user.UserRepository
import com.words.storageapp.database.model.LoggedInUser
import com.words.storageapp.domain.RegisterUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateViewModel(
    private val repository: UserRepository
) : ViewModel() {

    val termsAndCondition: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    /* val firstName = MutableLiveData("")
     val lastName = MutableLiveData("")
     val emailAddres = MutableLiveData("")
     val phone = MutableLiveData("")
     val addres = MutableLiveData("")
     val state = MutableLiveData("")
     val lga = MutableLiveData("")
     val dob = MutableLiveData("")
     val gender = MutableLiveData("")
     val skills = MutableLiveData("")
     val experience = MutableLiveData("")
     val desc = MutableLiveData("")

     val forms = listOf(
         firstName,lastName,emailAddres,phone,addres,state,lga,dob,gender,skills,experience,desc
     )*/

    val value = MediatorLiveData<Boolean>().apply {
        addSource(termsAndCondition) {
            value = it.and(true)
        }
    }

//  @ExperimentalCoroutinesApi
//  suspend fun addProfile(user: LoggedInUser ) = withContext(Dispatchers.IO) {
//      repository.insert(user)
//  }

    @ExperimentalCoroutinesApi
    fun addProfile(user: RegisterUser) = repository.insert(user)


    /*  @InternalCoroutinesApi
      fun createAccount(user:LoggedInUser){
        viewModelScope.launch {
            repository.insert(user).collect{ state ->

                when(state){
                    is State.Loading -> {

                    }
                    is State.Success<*> -> {

                    }
                }
            }
        }
      }*/


    // This function was designed to validate all create profile form before submitting
//    fun isFieldEmpty(inputs:List<LiveData<String>>): LiveData<Boolean> {
//
//        val result = MediatorLiveData<Boolean>()
//
//        //variable that triggers the submit button visible
//        val checkInput = Observer<String> {
//            result.value = inputs.any { it.value?.isNotBlank() ?: false }
//        }
//
//        inputs.forEach {
//            result.addSource(it,checkInput)
//        }
//
//        return result
//    }

    companion object {

        class CreateViewModelFactory(
            private val repository: UserRepository
        ) : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>) =
                CreateViewModel(repository) as T
        }
    }
}