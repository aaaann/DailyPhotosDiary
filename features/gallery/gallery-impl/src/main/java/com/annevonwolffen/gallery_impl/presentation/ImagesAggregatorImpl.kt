package com.annevonwolffen.gallery_impl.presentation

import com.annevonwolffen.gallery_impl.domain.Image
import com.annevonwolffen.gallery_impl.domain.settings.SortOrder
import com.annevonwolffen.gallery_impl.presentation.models.ImagesGroup
import com.annevonwolffen.gallery_impl.presentation.models.toPresentation
import com.annevonwolffen.gallery_impl.presentation.utils.toDateWithoutTime
import javax.inject.Inject

class ImagesAggregatorImpl @Inject constructor() : ImagesAggregator {
    override fun groupByDate(images: List<Image>): List<ImagesGroup> {
        return images.groupBy { it.date.toDateWithoutTime() }
            .map { ImagesGroup(it.key, it.value.map { image -> image.toPresentation() }) }
    }

    override fun sortByDate(imagesGroups: List<ImagesGroup>, sortOrder: SortOrder): List<ImagesGroup> {
        return if (sortOrder == SortOrder.BY_DATE_ASCENDING) {
            imagesGroups.sortedBy { it.date }
        } else {
            imagesGroups.sortedByDescending { it.date }
        }
    }
}