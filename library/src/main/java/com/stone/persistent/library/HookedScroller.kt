package com.stone.persistent.library

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.OverScroller
import java.lang.reflect.Field

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
     * 缓存的scrollerY对象
     */
    private var scrollerYObj: Any

    /**
     * SplineOverScroller 内部的 mDuration 字段
     */
    private var durationField: Field

    init {
        val scrollerYField = OverScroller::class.java.getDeclaredField("mScrollerY")
        scrollerYField.isAccessible = true
        scrollerYObj = scrollerYField.get(this)

        // Android 9.0及以上，非公开Api接口被禁用，无法获取mDuration字段
        // 此处伪装成系统身份，绕过 @hide 检查
        val metaGetDeclaredField = Class::class.java.getDeclaredMethod("getDeclaredField", String::class.java)
        durationField = metaGetDeclaredField.invoke(scrollerYObj.javaClass, "mDuration") as Field
        durationField.isAccessible = true

        uiHandler = Handler(Looper.getMainLooper()) {
            syncFling()
            false
        }
    }

    /**
     * fling传导
     */
    private fun syncFling() {
        val velocityY = currVelocity.toInt()
        if (velocityY > 200) {
            val childRecyclerView = persistentProvider.invoke()
            childRecyclerView?.fling(0, velocityY)
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
        maxY: Int
    ) {
        super.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY)

        if (velocityY < -200) {
            // 获取fling动画时长
            val duration = durationField.get(scrollerYObj) as Int

            // 结束时用handler启动fling传导
            uiHandler.sendEmptyMessageDelayed(1, duration.toLong())
        }
    }

    /**
     * 清空uiHandler中scroller动画Message
     */
    fun clearPendingMessages() {
        uiHandler.removeCallbacksAndMessages(null)
    }
}