package com.stone.persistent.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stone.persistent.R
import com.stone.persistent.adapter.ProductListAdapter
import kotlinx.android.synthetic.main.fragment_product_list.*

class ProductListFragment : Fragment(R.layout.fragment_product_list) {

    private var recyclerView: RecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.recyclerView = this.recycler_view
        this.recyclerView!!.layoutManager = LinearLayoutManager(activity)
        this.recyclerView!!.adapter = ProductListAdapter(activity!!)
    }

    fun getRecyclerView(): RecyclerView? {
        return recyclerView
    }
}