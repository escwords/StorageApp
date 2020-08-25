package com.words.storageapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.words.storageapp.databinding.FragmentAboutBinding
import com.words.storageapp.ui.search.SearchRepository
import com.words.storageapp.ui.search.SearchResultActivity
import com.words.storageapp.util.InjectorUtil
import com.words.storageapp.util.utilities.USERID
import timber.log.Timber

class AboutFragment : Fragment() {

    private val skillId: String by lazy {
        arguments?.get(USERID) as String
    }

    private val searchRepository: SearchRepository by lazy {
        (activity as SearchResultActivity).repository
    }

    private val detailViewModel: DetailViewModel by viewModels {
        InjectorUtil.provideDetailViewModelFactory(skillId, searchRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAboutBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = detailViewModel
        binding.executePendingBindings()

        Timber.i("About Fragment called")
        return binding.root
    }

}