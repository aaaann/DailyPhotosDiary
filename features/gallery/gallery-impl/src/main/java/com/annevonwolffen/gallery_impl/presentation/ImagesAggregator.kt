package com.annevonwolffen.gallery_impl.presentation

import com.annevonwolffen.gallery_impl.domain.Image
import com.annevonwolffen.gallery_impl.domain.settings.SortOrder
import com.annevonwolffen.gallery_impl.presentation.models.ImagesGroup

internal interface ImagesAggregator {
    fun groupByDate(images: List<Image>): List<ImagesGroup>

    fun sortByDate(imagesGroups: List<ImagesGroup>, sortOrder: SortOrder): List<ImagesGroup>
}