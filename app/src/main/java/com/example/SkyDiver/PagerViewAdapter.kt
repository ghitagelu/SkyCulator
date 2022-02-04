@file:Suppress("DEPRECATION")

package com.example.SkyDiver

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.SkyDiver.Fragments.*

internal class PagerViewAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                OverviewFragment()
            }
            1 -> {
                ListFragment()
            }

            else -> OverviewFragment()
        }
    }

    override fun getCount(): Int {

        return 2
    }

}

