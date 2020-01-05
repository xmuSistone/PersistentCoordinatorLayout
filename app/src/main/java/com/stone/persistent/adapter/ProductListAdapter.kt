package com.stone.persistent.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.stone.persistent.R
import kotlinx.android.synthetic.main.list_item.view.*

class ProductListAdapter(context: Context) : Adapter<ProductListAdapter.ProductViewHolder>() {

    private var inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = inflater.inflate(R.layout.list_item, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindPosition(position)
    }

    override fun getItemCount(): Int {
        return 30
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView = itemView.item_text

        fun bindPosition(position: Int) {
            textView.text = "这里是列表的content " + position
        }

    }
}