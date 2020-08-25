package com.words.storageapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.words.storageapp.databinding.ItemAlbumListBinding
import com.words.storageapp.domain.Photo

class EditPhotoAdapter : ListAdapter<Photo, EditPhotoAdapter.PhotoEditViewHolder>(itemUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoEditViewHolder {
        return PhotoEditViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PhotoEditViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class PhotoEditViewHolder(val binding: ItemAlbumListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photoItem: Photo) {
            binding.photo = photoItem

            Glide.with(itemView.context)
                .load(photoItem.photoUrl)
                .into(binding.imageItem)

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): PhotoEditViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return PhotoEditViewHolder(ItemAlbumListBinding.inflate(inflater, parent, false))
            }
        }
    }


    companion object {
        val itemUtil = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.photoId == newItem.photoId
            }

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem == newItem
            }
        }
    }
}