package com.annevonwolffen.gallery_impl.presentation

import com.annevonwolffen.gallery_impl.domain.Image
import com.annevonwolffen.gallery_impl.presentation.models.ImagesGroup

interface ImagesAggregator {
    fun groupByDate(images: List<Image>): List<ImagesGroup>
}