package com.words.storageapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.words.storageapp.ui.main.AboutFragment
import com.words.storageapp.ui.main.AlbumFragment

class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    companion object {
        const val ALBUM_FRAGMENT_INT = 0
        const val ABOUT_FRAGMENT_INT = 1
    }

    private val getTabFragment: Map<Int, () -> Fragment> = mapOf(
        ALBUM_FRAGMENT_INT to { AlbumFragment() },
        ABOUT_FRAGMENT_INT to { AboutFragment() }
    )

    override fun getItemCount(): Int = getTabFragment.size

    override fun createFragment(position: Int): Fragment {
        return getTabFragment[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }


}