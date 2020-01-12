package com.stone.persistent

import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.stone.persistent.adapter.CarouselAdapter
import com.stone.persistent.adapter.MenuViewPagerAdapter
import com.stone.persistent.adapter.ProductViewPagerAdapter
import com.stone.persistent.fragment.ProductListFragment
import com.stone.persistent.library.PersistentProvider
import com.stone.persistent.util.CarouselHelper
import com.stone.persistent.util.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_top_content.*

class MainActivity : AppCompatActivity() {

    private val BACK_DIMENSION_RATIO1 = 2.8125f
    private val BACK_DIMENSION_RATIO2 = 0.97826f

    private var SCREEN_WIDTH: Int = 0
    private var STATUS_BAR_HEIGHT: Int = 0
    private var TOOLBAR_HEIGHT: Float = 0f

    private var carouselHelper: CarouselHelper? = null
    private var netHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 0. 下拉刷新Handler
        netHandler = object : Handler() {
            override fun handleMessage(msg: Message?) {
                main_refresh_layout.finishRefresh()
            }
        }

        // 1. 调整状态栏
        initConstants()
        adjustStatusBar()

        // 2. ViewPager绑定Adapter
        val productPagerAdapter = ProductViewPagerAdapter(supportFragmentManager)
        main_viewpager.adapter = productPagerAdapter

        // 3. 轮播图绑定Adapter
        home_carousel_viewpager2.adapter = CarouselAdapter(this)
        home_carousel_viewpager2.currentItem = 2000
        home_carousel_indicator.setViewPager2(home_carousel_viewpager2, 5)
        carouselHelper = CarouselHelper(home_carousel_viewpager2)
        carouselHelper!!.start()

        // 4. 菜单左右滑动
        home_menu_viewpager2.offscreenPageLimit = 2
        home_menu_viewpager2.adapter = MenuViewPagerAdapter(this)
        home_menu_indicator.setViewPager2(home_menu_viewpager2, 2)

        // 5. CoordinatorLayout添加持续滑动的支持
        main_coordinator_layout.setPersistentProvider(object : PersistentProvider {
            override fun getViewPager(): ViewPager {
                return main_viewpager
            }

            override fun getRecyclerView(position: Int): RecyclerView? {
                val listFragment = productPagerAdapter.getItem(position) as ProductListFragment
                return listFragment.getRecyclerView()
            }
        })

        // 6. refreshLayout动态绑定
        bindRefreshLayout()
    }

    private fun initConstants() {
        SCREEN_WIDTH = Utils.getScreenWidth(this)
        STATUS_BAR_HEIGHT = Utils.getStatusBarHeight(this)
        TOOLBAR_HEIGHT = Utils.dp2px(this, 50f)
    }

    private fun bindRefreshLayout() {
        main_refresh_layout.setOnRefreshListener {
            netHandler!!.sendEmptyMessageDelayed(1, 2000)
        }

        val purposeListener = object : SimpleMultiPurposeListener() {
            override fun onHeaderMoving(
                header: RefreshHeader?,
                isDragging: Boolean,
                percent: Float,
                offset: Int,
                headerHeight: Int,
                maxDragHeight: Int
            ) {
                // 监听refreshLayout位置变动
                val backImgHeight1 = SCREEN_WIDTH / BACK_DIMENSION_RATIO1
                val backImgHeight2 = SCREEN_WIDTH / BACK_DIMENSION_RATIO2
                val maxTranslationY = backImgHeight1 - STATUS_BAR_HEIGHT - TOOLBAR_HEIGHT

                if (offset > maxTranslationY) {
                    main_back_img1.alpha = 0f
                    main_back_img1.translationY = 0f

                    val translationY2 = backImgHeight2 - backImgHeight1 - (offset - maxTranslationY)
                    main_back_img2.translationY = -translationY2
                } else {
                    val alpha = (maxTranslationY - offset) / maxTranslationY
                    main_back_img1.alpha = alpha

                    val translationY1 = maxTranslationY - offset
                    main_back_img1.translationY = -translationY1

                    val translationY2 = backImgHeight2 - backImgHeight1
                    main_back_img2.translationY = -translationY2
                }
            }
        }

        main_refresh_layout.setOnMultiPurposeListener(purposeListener)
    }

    private fun adjustStatusBar() {
        Utils.immerseStatusBar(this)
        val toolbarParams = main_toolbar.layoutParams as ConstraintLayout.LayoutParams
        toolbarParams.setMargins(0, STATUS_BAR_HEIGHT, 0, 0)
        main_toolbar.layoutParams = toolbarParams

        val backImgHeight1 = SCREEN_WIDTH / BACK_DIMENSION_RATIO1
        val translationY1 = backImgHeight1 - STATUS_BAR_HEIGHT - TOOLBAR_HEIGHT
        main_back_img1.translationY = -translationY1

        val backImgHeight2 = SCREEN_WIDTH / BACK_DIMENSION_RATIO2
        val translationY2 = backImgHeight2 - backImgHeight1
        main_back_img2.translationY = -translationY2
    }

    override fun onDestroy() {
        super.onDestroy()
        carouselHelper?.stop()
    }
}
