package com.stone.persistent.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.stone.persistent.fragment.FeedsListFragment

class FeedsPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = ArrayList<FeedsListFragment>()

    init {
        fragmentList.add(FeedsListFragment())
        fragmentList.add(FeedsListFragment())
        fragmentList.add(FeedsListFragment())
        fragmentList.add(FeedsListFragment())
        fragmentList.add(FeedsListFragment())
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }
}