package com.stone.persistent.library

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.WindowManager
import android.view.animation.Interpolator
import android.widget.OverScroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout

/**
 * 定制的CoordinatorLayout，第一个子View必须为AppbarLayout
 * 如果底部不用ViewPager的话，可以设置其adapter->getCount=0
 */
class PersistentCoordinatorLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr) {

    private lateinit var appBarLayout: AppBarLayout
    private var overScroller: HookedScroller? = null

    private var persistentProvider: PersistentProvider? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount === 2 && getChildAt(0) is AppBarLayout) {
            this.appBarLayout = getChildAt(0) as AppBarLayout
            this.appBarLayout.viewTreeObserver.addOnGlobalLayoutListener {
                if (overScroller == null) {
                    hookScroller()
                }
            }
        } else {
            throw RuntimeException("CustCoordinatorLayout's first child must be AppbarLayout")
        }
    }

    /**
     * 反射behavior塞入hookScroller
     */
    private fun hookScroller() {
        val lp = appBarLayout.layoutParams as LayoutParams
        val behavior = lp.behavior as AppBarLayout.Behavior

        val scrollerField =
            AppBarLayout.Behavior::class.java.superclass.superclass.getDeclaredField("scroller")
        scrollerField.isAccessible = true
        if (scrollerField.get(behavior) == null) {
            overScroller = HookedScroller(context)
            scrollerField.set(behavior, overScroller)
        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        // 手指按下时，所有scroll动画都需要停止
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            // behavior滑动停止
            overScroller?.clearPendingMessages()
            overScroller?.forceFinished(true)

            // 底部的RecyclerView滑动停止
            if (ev.y < appBarLayout.bottom && persistentProvider != null) {
                val currentViewPager = persistentProvider!!.getViewPager()
                val currentItem = currentViewPager.currentItem
                val currentRecyclerView = persistentProvider?.getRecyclerView(currentItem)
                currentRecyclerView?.stopScroll()
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    /**
     * 判断是否已经悬停在顶部
     */
    private fun isStickingTop(): Boolean {
        return appBarLayout.totalScrollRange + appBarLayout.top === 0
    }

    fun setPersistentProvider(persistentProvider: PersistentProvider) {
        this.persistentProvider = persistentProvider

        val viewPager = this.persistentProvider!!.getViewPager()
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING && !isStickingTop()) {
                    val len = viewPager.adapter?.count
                    for (i in 0 until len!!) {
                        if (i != viewPager.currentItem) {
                            val recyclerView = persistentProvider.getRecyclerView(i)
                            recyclerView?.scrollToPosition(0)
                        }
                    }
                }
            }
        })
    }

    inner class HookedScroller : OverScroller {

        private val refreshInterval: Int
        private var scrollerYObj: Any
        private val uiHandler: Handler

        /**
         * 构造函数
         */
        @JvmOverloads
        constructor(context: Context, interpolator: Interpolator? = null) : super(
            context,
            interpolator
        )

        init {
            // 获取系统刷新频率
            val refreshRate = getRefreshRate()
            refreshInterval = (1000 / refreshRate).toInt()

            val scrollerYField = OverScroller::class.java.getDeclaredField("mScrollerY")
            scrollerYField.isAccessible = true
            scrollerYObj = scrollerYField.get(this)

            uiHandler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    syncFling()
                }
            }
        }

        /**
         * fling传导
         */
        private fun syncFling() {
            val velocityY = this.getVelocityY()
            if (velocityY < -200 && persistentProvider != null) {
                val currentItem = persistentProvider!!.getViewPager().currentItem
                val currentScrollableView = persistentProvider!!.getRecyclerView(currentItem)
                if (currentScrollableView is RecyclerView) {
                    currentScrollableView.fling(0, -velocityY)
                }
            }
        }

        /**
         * 监听OverScroller.fling()，为后续的syncFling埋下种子
         */
        override fun fling(
            startX: Int,
            startY: Int,
            velocityX: Int,
            velocityY: Int,
            minX: Int,
            maxX: Int,
            minY: Int,
            maxY: Int,
            overX: Int,
            overY: Int
        ) {
            super.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, overX, overY)

            if (velocityY < -200) {
                // 获取fling动画时长
                val durationField = scrollerYObj.javaClass.getDeclaredField("mDuration")
                durationField.isAccessible = true
                val duration = durationField.get(scrollerYObj) as Int

                // 在fling动画结束的前一帧，用handler启动fling传导
                val flingInterval = duration - refreshInterval
                uiHandler.sendEmptyMessageDelayed(1, flingInterval.toLong())
            }
        }

        /**
         * 清空uiHandler中scroller动画Message
         */
        fun clearPendingMessages() {
            uiHandler.removeCallbacksAndMessages(null)
        }


        /**
         * 获取Y方向Scroller速率
         */
        private fun getVelocityY(): Int {
            val mCurrVelocityField = scrollerYObj.javaClass.getDeclaredField("mCurrVelocity")
            mCurrVelocityField.isAccessible = true
            val mCurrVelocity = mCurrVelocityField.get(scrollerYObj) as Float
            return mCurrVelocity.toInt()
        }

        /**
         * 获取刷新频率
         */
        private fun getRefreshRate(): Float {
            var windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            return windowManager.defaultDisplay.refreshRate
        }
    }

}