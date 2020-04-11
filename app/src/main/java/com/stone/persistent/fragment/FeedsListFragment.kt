package com.stone.persistent.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.stone.persistent.R
import com.stone.persistent.adapter.FeedsListAdapter
import com.stone.persistent.extensions.dp2px
import com.stone.persistent.widget.GridItemDecoration
import com.stone.persistent.widget.PersistentStaggeredGridLayoutManager
import kotlinx.android.synthetic.main.fragment_feeds_list.*

class FeedsListFragment : Fragment(R.layout.fragment_feeds_list) {

    private var recyclerView: RecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.recyclerView = this.recycler_view
        val layoutManager = PersistentStaggeredGridLayoutManager()
        this.recyclerView!!.layoutManager = layoutManager
        this.recyclerView!!.addItemDecoration(GridItemDecoration(activity!!.dp2px(8f)))
        this.recyclerView!!.adapter = FeedsListAdapter(activity!!)
    }
}