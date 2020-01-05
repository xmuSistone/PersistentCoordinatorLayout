package com.stone.persistent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.stone.persistent.adapter.CarouselAdapter
import com.stone.persistent.adapter.MenuViewPagerAdapter
import com.stone.persistent.adapter.ProductViewPagerAdapter
import com.stone.persistent.fragment.ProductListFragment
import com.stone.persistent.library.PersistentProvider
import com.stone.persistent.util.CarouselHelper
import com.stone.persistent.util.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_toolbar.*
import kotlinx.android.synthetic.main.home_top_content.*

class MainActivity : AppCompatActivity() {

    private var carouselHelper: CarouselHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 0. 调整状态栏
        adjustStatusBar()

        // 1. ViewPager绑定Adapter
        val prpductPagerAdapter = ProductViewPagerAdapter(supportFragmentManager)
        viewpager.adapter = prpductPagerAdapter

        // 2. 轮播图绑定Adapter
        home_carousel_viewpager2.adapter = CarouselAdapter(this)
        home_carousel_viewpager2.currentItem = 2000
        home_carousel_indicator.setViewPager2(home_carousel_viewpager2, 5)
        carouselHelper = CarouselHelper(home_carousel_viewpager2)
        carouselHelper!!.start()

        // 3. 菜单左右滑动
        home_menu_viewpager2.offscreenPageLimit = 2
        home_menu_viewpager2.adapter = MenuViewPagerAdapter(this)
        home_menu_indicator.setViewPager2(home_menu_viewpager2, 2)

        // 4. CoordinatorLayout添加持续滑动的支持
        coordinator_layout.setPersistentProvider(object : PersistentProvider {
            override fun getViewPager(): ViewPager {
                return viewpager
            }

            override fun getRecyclerView(position: Int): RecyclerView? {
                val listFragment = prpductPagerAdapter.getItem(position) as ProductListFragment
                return listFragment.getRecyclerView()
            }
        })
    }

    private fun adjustStatusBar() {
        Utils.immerseStatusBar(this)
        val statusHeight = Utils.getStatusBarHeight(this)
        val toolbarParams = home_toolbar.layoutParams as ConstraintLayout.LayoutParams
        toolbarParams.setMargins(0, statusHeight, 0, 0)
        home_toolbar.layoutParams = toolbarParams
    }

    override fun onDestroy() {
        super.onDestroy()
        carouselHelper?.stop()
    }
}
