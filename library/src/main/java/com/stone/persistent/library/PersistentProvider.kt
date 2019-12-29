package com.stone.persistent.library

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager

interface PersistentProvider {

    /**
     * CoordinatorLayout底部左右滑动的ViewPager
     */
    fun getViewPager(): ViewPager

    /**
     * 根据position获取相应的recyclerView
     */
    fun getRecyclerView(position: Int): RecyclerView?
}