package com.annevonwolffen.dailyphotosdiary.screens

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.annevonwolffen.gallery_impl.R
import com.annevonwolffen.gallery_impl.presentation.GalleryFragment
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object GalleryScreen : KScreen<GalleryScreen>() {
    override val layoutId: Int? = R.layout.fragment_gallery
    override val viewClass: Class<*>? = GalleryFragment::class.java

    val shimmer = KView { withId(R.id.shimmer_layout) }
    val addButton = KButton { withId(R.id.btn_add_image) }
    val imagesGroupRecycler = KRecyclerView(
        builder = { withId(R.id.rv_photos) },
        itemTypeBuilder = { itemType(::ImagesItem) })

    class ImagesItem(parent: Matcher<View>) : KRecyclerItem<ImagesItem>(parent) {
        val date = KView(parent) { withId(R.id.tv_date) }
        val imagesRecycler = KRecyclerView(parent,
            builder = {
                isInstanceOf(RecyclerView::class.java)
                withId(R.id.rv_images)
            },
            itemTypeBuilder = { itemType(::ImageItem) })
    }

    class ImageItem(parent: Matcher<View>) : KRecyclerItem<ImageItem>(parent) {
        val dayOfWeek = KTextView { withId(R.id.tv_day_of_week) }
        val description = KTextView { withId(R.id.tv_description) }
    }

    // fun checkImagesList() {
    //     imagesGroupRecycler.childWith<ImagesItem> {
    //     } perform {
    //         imagesRecycler {
    //             isDisplayed()
    //         }
    //     }
    // }
}