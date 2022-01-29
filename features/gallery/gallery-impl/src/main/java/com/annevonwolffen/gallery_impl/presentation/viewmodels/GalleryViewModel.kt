package com.annevonwolffen.gallery_impl.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annevonwolffen.gallery_impl.domain.Photo
import com.annevonwolffen.gallery_impl.domain.PhotosInteractor
import com.annevonwolffen.gallery_impl.presentation.Result
import com.annevonwolffen.gallery_impl.presentation.State
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GalleryViewModel(private val photosInteractor: PhotosInteractor) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.w(TAG, "Ошибка при загрузке картинок: $throwable")
        _photos.value = State.Error(throwable.message)
    }

    val photos: StateFlow<State<List<Photo>>>
        get() = _photos
    private val _photos: MutableStateFlow<State<List<Photo>>> = MutableStateFlow(State.Loading)

    fun loadPhotos() {
        viewModelScope.launch(exceptionHandler) {
            photosInteractor.loadPhotos(TEST_FOLDER).also {
                _photos.value =
                    when (it) {
                        is Result.Success -> State.Success(it.value)
                        is Result.Error -> State.Error(it.errorMessage)
                    }
            }
        }
    }

    private companion object {
        const val TAG = "GalleryViewModel"
        const val TEST_FOLDER = "testfolder"
    }
}