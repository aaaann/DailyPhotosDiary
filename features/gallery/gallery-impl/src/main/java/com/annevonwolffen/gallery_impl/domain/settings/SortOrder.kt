package com.annevonwolffen.gallery_impl.domain.settings

enum class SortOrder {
    /**
     * по дате по возрастанию
     */
    BY_DATE_ASCENDING,

    /**
     * по дате по убыванию
     */
    BY_DATE_DESCENDING
}

fun String?.sortOrderByName() = SortOrder.valueOf(this ?: SortOrder.BY_DATE_DESCENDING.name)
fun SortOrder.getOpposite() =
    if (this == SortOrder.BY_DATE_ASCENDING) SortOrder.BY_DATE_DESCENDING else SortOrder.BY_DATE_ASCENDING