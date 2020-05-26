package com.words.storageapp.ui.main

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.words.storageapp.R
import com.words.storageapp.adapters.ScreenSlidePagerAdapter
import com.words.storageapp.databinding.FragmentDetailBinding

class DetailFragment : FragmentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<FragmentDetailBinding>(
            this, R.layout.fragment_detail
        )
        val adapter = ScreenSlidePagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager

        viewPager.adapter = adapter

        val tabLayout = binding.viewpagerTab

        val tabTitle = listOf("Album", "About")

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()
    }

    /* override fun onCreateView(
         inflater: LayoutInflater,
         container: ViewGroup?,
         savedInstanceState: Bundle?
     ): View? {
         val binding = FragmentDetailBinding.inflate(inflater,container,false)

         binding.viewPager.adapter = ScreenSlidePagerAdapter(this)

         return binding.root
     }*/
}