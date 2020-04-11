package com.stone.persistent.library

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.OverScroller

/**
 * 这是注入到Behavior的scroller
 */
class HookedScroller(context: Context, persistentProvider: () -> PersistentRecyclerView?) :
    OverScroller(context) {

    private val persistentProvider = persistentProvider

    /**
     * sync同步用的handler
     */
    private val uiHandler: Handler

    /**
     * FPS刷新间隔（ms）
     */
    private val refreshInterval: Int

    /**
     * 缓存的scrollerY对象
     */
    private var scrollerYObj: Any

    init {
        // 获取系统刷新频率
        val refreshRate = getRefreshRate(context)
        refreshInterval = (1000 / refreshRate).toInt()

        val scrollerYField = OverScroller::class.java.getDeclaredField("mScrollerY")
        scrollerYField.isAccessible = true
        scrollerYObj = scrollerYField.get(this)

        uiHandler = Handler(Looper.getMainLooper()) {
            syncFling()
            false
        }
    }

    /**
     * fling传导
     */
    private fun syncFling() {
        val velocityY = this.getVelocityY()
        if (velocityY < -200) {
            val childRecyclerView = persistentProvider.invoke()
            childRecyclerView?.fling(0, -velocityY)
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
    private fun getRefreshRate(context: Context): Float {
        var windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return windowManager.defaultDisplay.refreshRate
    }
}