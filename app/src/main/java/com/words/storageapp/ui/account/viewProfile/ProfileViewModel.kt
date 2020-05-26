package com.words.storageapp.ui.account.viewProfile

import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.words.storageapp.ui.account.user.UserRepository

class ProfileViewModel(
    val userId: String,
    userRepository: UserRepository
) {

    val loggedUser = userRepository.loggedUser

    //  val firstName = loggedUser.map { it.first_name }

}