package com.words.storageapp.util

import com.words.storageapp.ui.account.user.UserManager
import com.words.storageapp.ui.account.user.UserRepository
import com.words.storageapp.ui.account.user.UserViewModel.Companion.UserViewModelFactory
import com.words.storageapp.ui.account.createProfile.CreateViewModel.Companion.CreateViewModelFactory

object InjectorUtil {

    /*fun provideUserViewModelFactory(repository: UserRepository, userManager: UserManager): UserViewModelFactory {
        return UserViewModelFactory(repository,userManager)
    }*/

    fun provideCreateViewModelFactory(repository: UserRepository): CreateViewModelFactory {
        return CreateViewModelFactory(repository)
    }
}