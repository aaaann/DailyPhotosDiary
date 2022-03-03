package com.annevonwolffen.gallery_impl.domain

import com.annevonwolffen.gallery_api.domain.ImagesExternalInteractor
import com.annevonwolffen.gallery_impl.presentation.Result
import com.annevonwolffen.gallery_impl.presentation.utils.isEqualByDate
import com.annevonwolffen.gallery_impl.presentation.utils.toCalendar
import java.util.Calendar
import javax.inject.Inject

class ImagesExternalInteractorImpl @Inject constructor(private val imagesRepository: ImagesRepository) :
    ImagesExternalInteractor {
    override suspend fun hasImagesForToday(folder: String): Boolean? {
        return when (val imagesResult = imagesRepository.getImages(folder)) {
            is Result.Success -> {
                imagesResult.value.map { it.date.toCalendar() }
                    .any { it.isEqualByDate(Calendar.getInstance()) }
            }
            is Result.Error -> {
                null
            }
        }
    }
}