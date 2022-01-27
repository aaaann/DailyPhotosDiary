package com.annevonwolffen.gallery_impl.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.annevonwolffen.gallery_impl.databinding.PhotoCardLayoutBinding
import com.annevonwolffen.gallery_impl.domain.Photo

class PhotosListAdapter : ListAdapter<Photo, PhotosListAdapter.ViewHolder>(DiffUtilCallback()) {

    class DiffUtilCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PhotoCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: PhotoCardLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            with(photo) {
                binding.tvDate.text = createdAt
                binding.tvDescription.text = description
                // TODO: parse date of week from date
                // TODO: load image with image manager
            }
        }
    }
}