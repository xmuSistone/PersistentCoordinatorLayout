package com.stone.persistent.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.stone.persistent.R
import com.stone.persistent.model.ProductItemModel
import kotlinx.android.synthetic.main.item_feeds_product.view.*

class FeedsListAdapter(context: Context) : Adapter<FeedsListAdapter.ProductViewHolder>() {

    private var inflater = LayoutInflater.from(context)
    private val dataList = ArrayList<ProductItemModel>()

    init {
        dataList.add(
            ProductItemModel(
                "单筒手机拍照望远镜 高倍高清微光夜视成人非红外演唱会望眼镜 升级版 送手机夹 三脚架",
                R.mipmap.goods1,
                1.084f,
                "112.00"
            )
        )
        dataList.add(
            ProductItemModel(
                "男士短款钱包复古真皮零钱夹头层牛皮竖款皮包 黑色",
                R.mipmap.goods2,
                0.776946f,
                "299.90"
            )
        )
        dataList.add(
            ProductItemModel(
                "男士保暖内衣高领打底衫冬季加绒加厚紧身防寒上衣单件秋衣男内穿 6611加绒黑色 M",
                R.mipmap.goods3,
                0.776946f,
                "65.50"
            )
        )

        dataList.add(
            ProductItemModel(
                "红木把玩健身手球老人生日礼物送长辈祝寿老年人实用礼物品爸爸父亲生日礼物",
                R.mipmap.goods4,
                0.756f,
                "88.00"
            )
        )

        dataList.add(
            ProductItemModel(
                "恒源祥羊毛衫女中长款2018新款秋冬韩版半高领打底毛衣长袖针织衫",
                R.mipmap.goods5,
                0.5784f,
                "99.00"
            )
        )

        dataList.add(
            ProductItemModel(
                "百圣牛PASNEW新款运动手表 多功能防水表夜光秒表倒计时游泳表学生大数字电子表",
                R.mipmap.goods6,
                0.776946f,
                "75.90"
            )
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = inflater.inflate(R.layout.item_feeds_product, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindPosition(position)
    }

    override fun getItemCount(): Int {
        return 80
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindPosition(position: Int) {
            val itemData = dataList.get(position % 6)
            itemView.goods_title_tv.text = itemData.goodName
            itemView.goods_imageview.setImageResource(itemData.imagRes)
            itemView.goods_price_tv.text = itemData.price

            val imageLp = itemView.goods_imageview.layoutParams as ConstraintLayout.LayoutParams
            imageLp.dimensionRatio = itemData.dimensionRatio.toString()
            itemView.goods_imageview.layoutParams = imageLp
        }

    }
}