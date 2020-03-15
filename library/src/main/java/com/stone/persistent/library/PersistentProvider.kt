package com.stone.persistent.library

import androidx.recyclerview.widget.RecyclerView

interface PersistentProvider {

    /**
     * 获取当前的RecyclerView
     */
    fun getCurrentRecyclerView(): RecyclerView?
}