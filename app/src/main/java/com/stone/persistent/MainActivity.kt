package com.stone.persistent

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.stone.persistent.adapter.CarouselAdapter
import com.stone.persistent.adapter.FeedsPagerAdapter
import com.stone.persistent.adapter.MenuViewPagerAdapter
import com.stone.persistent.helper.HomeIndicatorHelper
import com.stone.persistent.helper.SyncScrollHelper
import com.stone.persistent.extensions.immerseStatusBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_top_content.*

class MainActivity : AppCompatActivity() {

    private var pullRefreshHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. 调整状态栏
        immerseStatusBar()

        // 2. 列表滑动及下拉刷新，View状态同步
        val syncScrollHelper = SyncScrollHelper(this)
        syncScrollHelper.initLayout()
        syncScrollHelper.syncListScroll(main_appbar_layout)
        syncScrollHelper.syncRefreshPullDown(main_refresh_layout)

        // 3. 商品流，ViewPager绑定Adapter
        val feedsPagerAdapter = FeedsPagerAdapter(this)
        main_feeds_viewpager.adapter = feedsPagerAdapter
        val feedsIndicator = HomeIndicatorHelper(this)
        feedsIndicator.setViewPager(main_feeds_viewpager)

        // 4. 轮播图，ViewPager绑定Adapter
        val carouselViewPager = home_carousel_view.getViewPager2()
        carouselViewPager.adapter = CarouselAdapter(this)
        carouselViewPager.currentItem = 2000
        home_carousel_indicator.setViewPager2(carouselViewPager, 5)

        // 5. 菜单按钮，ViewPager左右滑动
        home_menu_viewpager2.offscreenPageLimit = 2
        home_menu_viewpager2.adapter = MenuViewPagerAdapter(this)
        home_menu_indicator.setViewPager2(home_menu_viewpager2, 2)

        // 6. 下拉刷新Handler
        processPullRefresh();
    }

    private fun processPullRefresh() {
        pullRefreshHandler = Handler {
            main_refresh_layout.finishRefresh()
            false
        }

        main_refresh_layout.setOnRefreshListener {
            pullRefreshHandler!!.sendEmptyMessageDelayed(0, 800)
        }
    }
}
