package com.annevonwolffen.gallery_impl.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.annevonwolffen.gallery_impl.R
import com.annevonwolffen.gallery_impl.databinding.ImageCardLayoutBinding
import com.annevonwolffen.gallery_impl.presentation.models.Image
import com.annevonwolffen.gallery_impl.presentation.utils.toCalendar
import com.annevonwolffen.gallery_impl.presentation.utils.toString
import com.annevonwolffen.ui_utils_api.image.ImageLoader

class ImagesListAdapter(
    private val imageLoader: ImageLoader,
    private val onClick: (Image) -> Unit
) :
    ListAdapter<Image, ImagesListAdapter.ViewHolder>(DiffUtilCallback()) {

    class DiffUtilCallback : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean = oldItem == newItem

        override fun getChangePayload(oldItem: Image, newItem: Image): Any? {
            // TODO: calculate payload; example: https://proandroiddev.com/diffutil-is-a-must-797502bc1149
            return super.getChangePayload(oldItem, newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ImageCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, imageLoader, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            holder.bind(getItem(position), payloads)
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    class ViewHolder(
        private val binding: ImageCardLayoutBinding,
        private val imageLoader: ImageLoader,
        private val onClick: (Image) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Image, payloads: MutableList<Any>? = null) {
            // TODO: parse payloads
            with(image) {
                binding.tvDate.text = date.toCalendar().toString(binding.root.resources)
                binding.tvDescription.text = description
                imageLoader.loadImage(binding.ivPhoto, url, R.drawable.image_progress_loader)
                binding.root.setOnClickListener { onClick.invoke(this) }
            }
        }
    }
}