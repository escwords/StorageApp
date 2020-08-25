package com.words.storageapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.words.storageapp.domain.Photo

class AlbumViewModel : ViewModel() {

    private val albumList = MutableLiveData<List<Photo>>()

    val photos: LiveData<List<Photo>>
        get() = albumList

    fun addPhotoToAlbum(photos: List<Photo>) {
        albumList.postValue(photos)
    }

    companion object {

        class AlbumViewModelFactory : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AlbumViewModel() as T
            }
        }
    }
}