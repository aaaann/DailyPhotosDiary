package com.annevonwolffen.gallery_impl.presentation.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annevonwolffen.gallery_impl.domain.Photo
import com.annevonwolffen.gallery_impl.domain.PhotosInteractor
import com.annevonwolffen.gallery_impl.domain.UploadImage
import com.annevonwolffen.gallery_impl.presentation.AddImageBottomSheet.AddImage
import com.annevonwolffen.gallery_impl.presentation.Result
import com.annevonwolffen.gallery_impl.presentation.State
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File

class AddImageViewModel(private val imagesInteractor: PhotosInteractor) : ViewModel() {

    val uriFlow: StateFlow<Uri?>
        get() = _uriFlow
    private val _uriFlow: MutableStateFlow<Uri?> = MutableStateFlow(null)

    var file: File? = null
    var uri: Uri? = null

    val uploadedImageFlow get() = _uploadedImageFlow.receiveAsFlow()
    private val _uploadedImageFlow = Channel<State<List<Photo>>>(CONFLATED)

    val addImageEvent get() = _addImageEvent.receiveAsFlow()
    private val _addImageEvent = Channel<AddImage>(CONFLATED)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.w(TAG, "Ошибка при загрузке картинок: $throwable")
    }

    // fun setUri(uri: Uri?) {
    //     _uriFlow.value = uri
    // }

    fun saveImage(image: UploadImage) {
        viewModelScope.launch(exceptionHandler) {
            imagesInteractor.uploadImages(TEST_FOLDER, image).also {
                _uploadedImageFlow.send(
                    when (it) {
                        is Result.Success -> State.Success(it.value)
                        is Result.Error -> State.Error(it.errorMessage)
                    }
                )
            }
        }
    }

    fun addImage(addImageCommand: AddImage) {
        viewModelScope.launch { _addImageEvent.send(addImageCommand) }
    }

    private companion object {
        const val TAG = "AddImageViewModel"
        const val TEST_FOLDER = "testfolder"
    }
}