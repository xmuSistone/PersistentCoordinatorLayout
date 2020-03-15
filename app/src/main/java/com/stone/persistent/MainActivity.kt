package com.stone.persistent

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.stone.persistent.adapter.CarouselAdapter
import com.stone.persistent.adapter.FeedsPagerAdapter
import com.stone.persistent.adapter.MenuViewPagerAdapter
import com.stone.persistent.helper.SyncScrollHelper
import com.stone.persistent.util.CarouselHelper
import com.stone.persistent.util.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_top_content.*
import kotlinx.android.synthetic.main.main_feeds_tabs.*

class MainActivity : AppCompatActivity() {

    private var carouselHelper: CarouselHelper? = null
    private var uiHandler: Handler? = null
    private val tabList = ArrayList<TextView>()

    companion object {
        private val COLOR_TAB_NORMAL by lazy { Color.parseColor("#333333") }
        private val COLOR_TAB_SELECTED by lazy { Color.parseColor("#ff0000") }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. 调整状态栏
        Utils.immerseStatusBar(this)

        // 2. 列表滑动及下拉刷新，View状态同步
        val syncScrollHelper = SyncScrollHelper(this)
        syncScrollHelper.initLayout()
        syncScrollHelper.syncListScroll(main_appbar_layout)
        syncScrollHelper.syncRefreshPullDown(main_refresh_layout)

        // 3. 商品流，ViewPager绑定Adapter
        val feedsPagerAdapter = FeedsPagerAdapter(supportFragmentManager)
        main_feeds_viewpager.adapter = feedsPagerAdapter
        bindTabsIndicator()

        // 4. 轮播图，ViewPager绑定Adapter
        home_carousel_viewpager2.adapter = CarouselAdapter(this)
        home_carousel_viewpager2.currentItem = 2000
        home_carousel_indicator.setViewPager2(home_carousel_viewpager2, 5)
        carouselHelper = CarouselHelper(home_carousel_viewpager2)
        carouselHelper!!.start()

        // 5. 菜单按钮，ViewPager左右滑动
        home_menu_viewpager2.offscreenPageLimit = 2
        home_menu_viewpager2.adapter = MenuViewPagerAdapter(this)
        home_menu_indicator.setViewPager2(home_menu_viewpager2, 2)

        // 6. 下拉刷新Handler
        uiHandler = object : Handler() {
            override fun handleMessage(msg: Message?) {
                main_refresh_layout.finishRefresh()
            }
        }
        main_refresh_layout.setOnRefreshListener {
            uiHandler!!.sendEmptyMessageDelayed(0, 800)
        }
    }

    /**
     * 商品流左右滑动，指示器
     */
    private fun bindTabsIndicator() {
        val tabClickListener = View.OnClickListener {
            val index = tabList.indexOf(it)
            main_feeds_viewpager.currentItem = index
        }

        tabList.add(main_feeds_tab1)
        tabList.add(main_feeds_tab2)
        tabList.add(main_feeds_tab3)
        tabList.add(main_feeds_tab4)
        tabList.add(main_feeds_tab5)

        for (itemTab in tabList) {
            itemTab.setOnClickListener(tabClickListener)
        }

        main_feeds_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                onTabChanged(position)
            }
        })
        onTabChanged(0)
    }

    private fun onTabChanged(position: Int) {
        val num = tabList.size
        for (i in 0 until num) {
            val itemTab = tabList[i]
            if (i == position) {
                itemTab.setTextColor(COLOR_TAB_SELECTED)
                itemTab.paint.isFakeBoldText = true
                itemTab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            } else {
                itemTab.setTextColor(COLOR_TAB_NORMAL)
                itemTab.paint.isFakeBoldText = false
                itemTab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            }
        }
    }
}
