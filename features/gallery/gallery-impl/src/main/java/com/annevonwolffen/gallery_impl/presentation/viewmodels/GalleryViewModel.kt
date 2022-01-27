package com.annevonwolffen.gallery_impl.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annevonwolffen.gallery_impl.domain.Photo
import com.annevonwolffen.gallery_impl.domain.PhotosInteractor
import com.annevonwolffen.gallery_impl.presentation.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GalleryViewModel(private val photosInteractor: PhotosInteractor) : ViewModel() {

    val photos: StateFlow<Result<List<Photo>>>
        get() = _photos
    private val _photos = MutableStateFlow(Result.Success<List<Photo>>(emptyList()))

    fun loadPhotos() {
        viewModelScope.launch {
            photosInteractor.loadPhotos().also { _photos.value = Result.Success(it) }
        }
    }
}