package com.words.storageapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.words.storageapp.R
import com.words.storageapp.adapters.ScreenSlidePagerAdapter
import com.words.storageapp.databinding.FragmentDetailBinding
import com.words.storageapp.ui.search.SearchRepository
import com.words.storageapp.ui.search.SearchResultActivity
import com.words.storageapp.util.InjectorUtil
import com.words.storageapp.util.utilities.USERID
import timber.log.Timber

class DetailFragment : Fragment(), View.OnClickListener {

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

        //watch out this variable
        val binding = FragmentDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            detailModel = detailViewModel
            executePendingBindings()
        }
        binding.profileImage.setOnClickListener(this)

        val adapter = ScreenSlidePagerAdapter(this, skillId)

        val viewPager: ViewPager2 = binding.viewPager

        viewPager.adapter = adapter
        val tabLayout = binding.myViewPagerTab

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        return binding.root
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            0 -> getString(R.string.albumText)
            1 -> getString(R.string.aboutText)
            else -> null
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.profileImage -> {
                val action = R.id.action_detailFragment_to_photoDetail
                detailViewModel.imageUrl.observe(this, Observer { imageLink ->
                    if (imageLink != null) {
                        val bundle = bundleOf(USERID to imageLink)
                        findNavController().navigate(action, bundle)
                    }
                })
            }
            R.id.call_btn -> {
                Timber.i("Call Intent called")
            }
            R.id.msg_btn -> {
                Timber.i("Msg Intent called")
            }
        }
    }
}