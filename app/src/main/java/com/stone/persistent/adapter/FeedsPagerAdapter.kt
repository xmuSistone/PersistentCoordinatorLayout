package com.stone.persistent.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.stone.persistent.fragment.FeedsListFragment

class FeedsPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = ArrayList<Fragment>()

    init {
        fragmentList.add(FeedsListFragment())
        fragmentList.add(FeedsListFragment())
        fragmentList.add(FeedsListFragment())
        fragmentList.add(FeedsListFragment())
        fragmentList.add(FeedsListFragment())
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList.get(position)
    }
}