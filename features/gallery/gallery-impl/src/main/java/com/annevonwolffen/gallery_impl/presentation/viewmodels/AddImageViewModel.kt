package com.annevonwolffen.gallery_impl.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annevonwolffen.gallery_impl.domain.Image
import com.annevonwolffen.gallery_impl.domain.ImagesInteractor
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

class AddImageViewModel(private val imagesInteractor: ImagesInteractor) : ViewModel() {

    val fileFlow: StateFlow<File?> get() = _fileFlow
    private val _fileFlow: MutableStateFlow<File?> = MutableStateFlow(null)

    val imageUploadedEvent get() = _imageUploadedEvent.receiveAsFlow()
    private val _imageUploadedEvent = Channel<State<Unit>>(CONFLATED)

    val imageDeletedEvent get() = _imageDeletedEvent.receiveAsFlow()
    private val _imageDeletedEvent = Channel<State<Unit>>(CONFLATED)

    val addImageEvent get() = _addImageEvent.receiveAsFlow()
    private val _addImageEvent = Channel<AddImage>(CONFLATED)

    val dismissBottomSheetEvent get() = _dismissBottomSheetEvent.receiveAsFlow()
    private val _dismissBottomSheetEvent = Channel<Unit>(CONFLATED)

    val progressLoaderState: StateFlow<Boolean> get() = _progressLoaderState
    private val _progressLoaderState = MutableStateFlow(false)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.w(TAG, "Ошибка при загрузке изображения: $throwable")
        _progressLoaderState.value = false
    }

    private val deleteExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.w(TAG, "Ошибка при удалении изображения: $throwable")
        _progressLoaderState.value = false
    }

    fun setFile(file: File?) {
        _fileFlow.value = file
    }

    fun saveImage(image: Image) {
        viewModelScope.launch(exceptionHandler) {
            _progressLoaderState.value = true
            imagesInteractor.uploadImageToDatabase(TEST_FOLDER, image).also {
                _progressLoaderState.value = false
                when (it) {
                    is Result.Success -> {
                        _imageUploadedEvent.send(State.Success(Unit))
                        imagesInteractor.uploadFileToStorage(TEST_FOLDER, image.copy(id = it.value))
                    }
                    is Result.Error -> {
                        _imageUploadedEvent.send(State.Error(it.errorMessage))
                    }
                }
            }
        }
    }

    fun addImage(addImageCommand: AddImage) {
        viewModelScope.launch { _addImageEvent.send(addImageCommand) }
    }

    fun deleteImage(image: Image) {
        viewModelScope.launch(deleteExceptionHandler) {
            _progressLoaderState.value = true
            imagesInteractor.deleteImage(TEST_FOLDER, image).also {
                _progressLoaderState.value = false
                when (it) {
                    is Result.Success -> {
                        _imageDeletedEvent.send(State.Success(Unit))
                        imagesInteractor.deleteFileFromStorage(TEST_FOLDER, image)
                    }
                    is Result.Error -> {
                        _imageDeletedEvent.send(State.Error(it.errorMessage))
                    }
                }
            }
        }
    }

    fun dismissBottomSheet() {
        viewModelScope.launch { _dismissBottomSheetEvent.send(Unit) }
    }

    private companion object {
        const val TAG = "AddImageViewModel"
        const val TEST_FOLDER = "testfolder"
    }
}