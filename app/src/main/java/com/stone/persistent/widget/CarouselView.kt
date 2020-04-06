package com.stone.persistent.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import com.stone.persistent.util.CarouselHelper

class CarouselView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val carouselHelper: CarouselHelper
    private val viewPager2: ViewPager2 = ViewPager2(context)

    init {
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        addView(viewPager2, layoutParams)

        carouselHelper = CarouselHelper(viewPager2)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev!!.action == MotionEvent.ACTION_DOWN) {
            carouselHelper.stop()
        } else if (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_CANCEL) {
            carouselHelper.start()
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        carouselHelper.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        carouselHelper.stop()
    }

    fun getViewPager2(): ViewPager2 {
        return viewPager2
    }
}