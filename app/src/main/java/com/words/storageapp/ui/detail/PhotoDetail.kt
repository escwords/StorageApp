package com.words.storageapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.words.storageapp.databinding.FragmentPhotoDetailBinding
import com.words.storageapp.util.USERID

/**
 * A simple [Fragment] subclass.
 */
class PhotoDetail : Fragment() {

    private val imageUrl: String by lazy {
        arguments?.get(USERID) as String
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = FragmentPhotoDetailBinding.inflate(inflater, container, false)
        val imageView = view.profileImage

        Glide.with(this)
            .load(imageUrl)
            .into(imageView)

        return view.root
    }

}
