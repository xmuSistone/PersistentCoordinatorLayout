package com.stone.persistent.library

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.viewpager.widget.ViewPager

/**
 * 加强垂直滑动时的"非拦截处理"，交给底层RecyclerView消费
 */
class PersistentViewPager : ViewPager {

    private val mTouchSlop: Int
    private var downX: Float = 0f
    private var downY: Float = 0f

    private var dragState: Int = DRAG_IDLE

    companion object {
        private const val DRAG_IDLE = 0
        private const val DRAG_VERTICAL = 1
        private const val DRAG_HORIZONTAL = 2
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    init {
        val configuration = ViewConfiguration.get(context)
        mTouchSlop = configuration.scaledTouchSlop
    }

    /**
     * 修改拦截方法，主要原因是CoordinatorLayout用了nesting机制，viewPager本身的touch拦截用ev.getY()判定不准
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            // ACTION_DOWN 触摸按下，保存临时变量
            dragState = DRAG_IDLE
            downX = ev.rawX
            downY = ev.rawY
        } else if (ev.action == MotionEvent.ACTION_MOVE) {
            // ACTION_MOVE 判定垂直还是水平滑动
            if (dragState == DRAG_IDLE) {
                val xDistance = Math.abs(ev.rawX - downX)
                val yDistance = Math.abs(ev.rawY - downY)

                if (xDistance > yDistance && xDistance > mTouchSlop) {
                    // 水平滑动
                    dragState = DRAG_HORIZONTAL
                    return true
                } else if (yDistance > xDistance && yDistance > mTouchSlop) {
                    // 垂直滑动
                    dragState = DRAG_VERTICAL
                    return false
                }
            } else if (dragState == DRAG_VERTICAL) {
                // 如果是垂直滑动，则不予拦截，touch事件下发给RecyclerView处理
                return false
            }
        }

        // 当然，默认还是交给ViewPager固有的拦截函数处理
        return super.onInterceptTouchEvent(ev)
    }

}