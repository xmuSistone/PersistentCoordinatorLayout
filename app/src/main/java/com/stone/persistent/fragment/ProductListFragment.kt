package com.stone.persistent.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.stone.persistent.R
import com.stone.persistent.adapter.ProductListAdapter
import com.stone.persistent.util.Utils
import com.stone.persistent.widget.GridItemDecoration
import kotlinx.android.synthetic.main.fragment_product_list.*

class ProductListFragment : Fragment(R.layout.fragment_product_list) {

    private var recyclerView: RecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.recyclerView = this.recycler_view
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        this.recyclerView!!.layoutManager = layoutManager
        this.recyclerView!!.addItemDecoration(GridItemDecoration(Utils.dp2px(activity!!, 8f)))
        this.recyclerView!!.adapter = ProductListAdapter(activity!!)
    }

    fun getRecyclerView(): RecyclerView? {
        return recyclerView
    }
}