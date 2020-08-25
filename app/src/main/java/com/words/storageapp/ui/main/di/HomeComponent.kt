package com.words.storageapp.ui.main.di

import android.content.Context
import com.words.storageapp.di.dagger.BaseActivityComponent
import com.words.storageapp.di.dagger.SharedPreferenceModule
import com.words.storageapp.ui.account.RegistrationFragment
import com.words.storageapp.ui.main.HomeFragment
import com.words.storageapp.ui.main.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

//@Singleton
//@Component(modules = [MainModule::class, SharedPreferenceModule::class])
//interface MainComponent: BaseActivityComponent<MainActivity> {
//
//    interface Factory{
//
//       // fun create(): MainComponent
//        fun homeActivity(activity: MainActivity): Factory
//        fun sharePreferenceModule(module: SharedPreferenceModule): Factory
//    }
//
//    fun inject(fragment: HomeFragment)
//    fun inject(fragment: RegistrationFragment)
//
//}