package com.stone.persistent.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.stone.persistent.fragment.FeedsListFragment

class FeedsPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return FeedsListFragment()
    }

    override fun getItemCount(): Int {
        return 5
    }
}