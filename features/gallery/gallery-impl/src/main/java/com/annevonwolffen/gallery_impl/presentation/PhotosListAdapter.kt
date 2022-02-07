package com.annevonwolffen.gallery_impl.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.annevonwolffen.gallery_impl.R
import com.annevonwolffen.gallery_impl.databinding.PhotoCardLayoutBinding
import com.annevonwolffen.gallery_impl.domain.Image
import com.annevonwolffen.ui_utils_api.image.ImageLoader

class PhotosListAdapter(private val imageLoader: ImageLoader) :
    ListAdapter<Image, PhotosListAdapter.ViewHolder>(DiffUtilCallback()) {

    class DiffUtilCallback : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PhotoCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, imageLoader)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: PhotoCardLayoutBinding, private val imageLoader: ImageLoader) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Image) {
            with(image) {
                binding.tvDate.text = createdAt
                binding.tvDescription.text = description
                // TODO: parse date of week from date
                imageLoader.loadImage(binding.ivPhoto, url, R.drawable.image_progress_loader)
            }
        }
    }
}