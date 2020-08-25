package com.words.storageapp.util

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.words.storageapp.R

@BindingAdapter("imgSrcUri")
fun ImageView.convertUriToImage(imageUrl: String?) {
    imageUrl?.let {
        //val uri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(this.context)
            .load(imageUrl)
            .into(this)
    }
    // apply(RequestOptions().placeholder(loading_animation).error(R.drawable.ic_broken_image)
}

@BindingAdapter("status")
fun accountStatus(status: TextView, state: Boolean) {
    if (state) {
        status.text = "Active"
    } else {
        status.text = "InActive"
    }
}