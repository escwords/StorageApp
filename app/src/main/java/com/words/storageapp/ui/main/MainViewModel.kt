package com.words.storageapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.words.storageapp.util.utilities.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor() : ViewModel() {

}

enum class DataState {
    Success,
    Failed
}