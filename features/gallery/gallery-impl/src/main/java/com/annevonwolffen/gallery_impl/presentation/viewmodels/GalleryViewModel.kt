package com.annevonwolffen.gallery_impl.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annevonwolffen.gallery_impl.domain.ImagesInteractor
import com.annevonwolffen.gallery_impl.domain.settings.GallerySettingsInteractor
import com.annevonwolffen.gallery_impl.domain.settings.SortOrder
import com.annevonwolffen.gallery_impl.presentation.ImagesAggregator
import com.annevonwolffen.gallery_impl.presentation.Result
import com.annevonwolffen.gallery_impl.presentation.State
import com.annevonwolffen.gallery_impl.presentation.models.ImagesGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GalleryViewModel(
    imagesInteractor: ImagesInteractor,
    private val imagesAggregator: ImagesAggregator,
    private val settingsInteractor: GallerySettingsInteractor
) : ViewModel() {

    val images: StateFlow<State<List<ImagesGroup>>>
        get() = _images.combine(_sortOrder) { imagesState, order ->
            when (imagesState) {
                is Result.Success -> {
                    State.Success(imagesAggregator.sortByDate(imagesState.value, order))
                }
                is Result.Error -> {
                    State.Error(imagesState.errorMessage)
                }
            }
        }
            .catch { t ->
                Log.w(TAG, "Ошибка при получении изображений: $t")
                emit(State.Error(t.message))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = State.Loading
            )

    private val _sortOrder: Flow<SortOrder> = settingsInteractor.sortOrderFlow

    private val _images: Flow<Result<List<ImagesGroup>>> = imagesInteractor.loadImages(TEST_FOLDER)
        .map {
            when (it) {
                is Result.Success -> {
                    Result.Success(imagesAggregator.groupByDate(it.value))
                }
                is Result.Error -> {
                    Result.Error(it.errorMessage)
                }
            }
        }

    fun toggleImagesSort() {
        viewModelScope.launch {
            settingsInteractor.toggleSortOrder()
        }
    }

    private companion object {
        const val TAG = "GalleryViewModel"
        const val TEST_FOLDER = "testfolder"
    }
}