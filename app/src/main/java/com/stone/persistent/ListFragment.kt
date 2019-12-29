package com.stone.persistent

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.list_item.view.*

@SuppressLint("ValidFragment")
class ListFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    private var recyclerView: RecyclerView? = null
    private lateinit var adapter: RecyclerView.Adapter<CustViewHolder>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.recyclerView = this.recycler_view
        this.recyclerView!!.layoutManager = LinearLayoutManager(activity)
        initAdapter()
        this.recyclerView!!.adapter = adapter
    }

    private fun initAdapter() {
        val inflater = LayoutInflater.from(activity)
        adapter = object : RecyclerView.Adapter<CustViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustViewHolder {
                val itemView = inflater.inflate(R.layout.list_item, parent, false)
                return CustViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: CustViewHolder, position: Int) {
                holder.bindPosition(position)
            }

            override fun getItemCount(): Int {
                return 30
            }
        }
    }

    fun getRecyclerView(): RecyclerView? {
        return recyclerView
    }

    inner class CustViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView = itemView.item_text

        fun bindPosition(position: Int) {
            textView.text = "这里是列表的content " + position
        }

    }

}