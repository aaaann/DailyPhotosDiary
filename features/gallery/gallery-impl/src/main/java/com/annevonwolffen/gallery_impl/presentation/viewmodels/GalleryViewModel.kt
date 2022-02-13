package com.annevonwolffen.gallery_impl.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annevonwolffen.gallery_impl.domain.Image
import com.annevonwolffen.gallery_impl.domain.ImagesInteractor
import com.annevonwolffen.gallery_impl.presentation.Result
import com.annevonwolffen.gallery_impl.presentation.State
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GalleryViewModel(imagesInteractor: ImagesInteractor) : ViewModel() {

    val images: StateFlow<State<List<Image>>> = imagesInteractor.loadImages(TEST_FOLDER)
        .map {
            when (it) {
                is Result.Success -> State.Success(it.value)
                is Result.Error -> State.Error(it.errorMessage)
            }
        }
        .catch { t ->
            Log.w(TAG, "Ошибка при получении изображений: $t")
            State.Error(t.message)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = State.Loading
        )

    private companion object {
        const val TAG = "GalleryViewModel"
        const val TEST_FOLDER = "testfolder"
    }
}