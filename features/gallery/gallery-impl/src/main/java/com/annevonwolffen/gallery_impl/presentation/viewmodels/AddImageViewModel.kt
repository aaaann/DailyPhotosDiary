package com.annevonwolffen.gallery_impl.presentation.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annevonwolffen.gallery_impl.domain.Photo
import com.annevonwolffen.gallery_impl.domain.PhotosInteractor
import com.annevonwolffen.gallery_impl.domain.UploadImage
import com.annevonwolffen.gallery_impl.presentation.Result
import com.annevonwolffen.gallery_impl.presentation.State
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddImageViewModel(private val imagesInteractor: PhotosInteractor) : ViewModel() {

    val uriFlow: StateFlow<Uri?>
        get() = _uriFlow
    private val _uriFlow: MutableStateFlow<Uri?> = MutableStateFlow(null)

    val uploadedImageFlow: StateFlow<State<List<Photo>>>
        get() = _uploadedImageFlow // TODO: change to channel (single event)
    private val _uploadedImageFlow: MutableStateFlow<State<List<Photo>>> = MutableStateFlow(State.Success(emptyList()))

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.w(TAG, "Ошибка при загрузке картинок: $throwable")
    }

    fun setUri(uri: Uri?) {
        _uriFlow.value = uri
    }

    fun saveImage(image: UploadImage) {
        viewModelScope.launch(exceptionHandler) {
            imagesInteractor.uploadImages(TEST_FOLDER, image).also {
                _uploadedImageFlow.value =
                    when(it) {
                        is Result.Success -> State.Success(it.value)
                        is Result.Error -> State.Error(it.errorMessage)
                    }
            }
        }
    }

    private companion object {
        const val TAG = "AddImageViewModel"
        const val TEST_FOLDER = "testfolder"
    }
}