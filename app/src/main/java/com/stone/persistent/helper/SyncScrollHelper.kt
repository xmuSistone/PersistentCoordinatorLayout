package com.stone.persistent.helper

import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.appbar.AppBarLayout
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.stone.persistent.MainActivity
import com.stone.persistent.util.Utils
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 首页滑动帮助类
 */
class SyncScrollHelper(mainActivity: MainActivity) {

    private val statusBarHeight = Utils.getStatusBarHeight(mainActivity)
    private val toolbarHeight = Utils.dp2px(mainActivity, 50f)
    private var screenWidth = Utils.getScreenWidth(mainActivity)
    private var searchBarHeight = Utils.dp2px(mainActivity, 46f)

    private val activity = mainActivity
    private val toolBarLayout = mainActivity.main_toolbar
    private val searchBarLayout = mainActivity.main_search_layout
    private val backIv1 = mainActivity.main_back_img1
    private val backIv2 = mainActivity.main_back_img2
    private val logoImageView = mainActivity.main_top_logo

    companion object {
        private const val BACK_DIMENSION_RATIO2 = 0.992647f
        private const val BACK_DIMENSION_RATIO1 = 1.8125f
    }

    fun initLayout() {
        val toolbarParams = toolBarLayout.layoutParams as ConstraintLayout.LayoutParams
        toolbarParams.setMargins(0, statusBarHeight, 0, 0)
        toolBarLayout.layoutParams = toolbarParams

        val backImgHeight1 = screenWidth / BACK_DIMENSION_RATIO1
        val translationY1 = backImgHeight1 - statusBarHeight - toolbarHeight - searchBarHeight
        backIv1.translationY = -translationY1

        val backImgHeight2 = screenWidth / Companion.BACK_DIMENSION_RATIO2
        val translationY2 = backImgHeight2 - backImgHeight1
        backIv2.translationY = -translationY2

        searchBarLayout.translationY = statusBarHeight + toolbarHeight
    }

    /**
     * 列表滚动时，一些View位置变动
     */
    fun syncListScroll(appBarLayout: AppBarLayout) {
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            val minTranslationY = statusBarHeight + Utils.dp2px(activity, 9f)
            val maxTranslationY = statusBarHeight + toolbarHeight
            val targetTranslationY = maxTranslationY + offset / 2

            // 1. logo的alpha处理
            var alpha = 1 + offset / 2 / (maxTranslationY - minTranslationY)
            if (alpha < 0) {
                alpha = 0f
            }
            logoImageView.alpha = alpha

            // 2. 搜索框位移调整
            searchBarLayout.translationY = if (targetTranslationY < minTranslationY) {
                minTranslationY
            } else {
                targetTranslationY
            }

            // 3. 搜索框大小调整
            val maxMarginRight = Utils.dp2px(activity, 92f)
            var progress = (1 - alpha) * 2f
            if (progress > 1) {
                progress = 1.0f
            }
            val layoutParams = searchBarLayout.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.setMargins(0, 0, (maxMarginRight * progress).toInt(),0)
            searchBarLayout.layoutParams = layoutParams
        })
    }

    /**
     * 下拉刷新View同步处理
     */
    fun syncRefreshPullDown(refreshLayout: SmartRefreshLayout) {
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
                val backImgHeight1 = screenWidth / BACK_DIMENSION_RATIO1
                val backImgHeight2 = screenWidth / BACK_DIMENSION_RATIO2
                val maxTranslationY =
                    backImgHeight1 - statusBarHeight - toolbarHeight - searchBarHeight

                if (offset > maxTranslationY) {
                    val outOfOffset = offset - maxTranslationY

                    backIv1.alpha = 0f
                    toolBarLayout.alpha = 0f
                    searchBarLayout.alpha = 0f

                    backIv1.translationY = 0f

                    val translationY2 = backImgHeight2 - backImgHeight1 - outOfOffset
                    backIv2.translationY = -translationY2
                } else {
                    val alpha = (maxTranslationY - offset) / maxTranslationY
                    backIv1.alpha = alpha
                    toolBarLayout.alpha = alpha
                    searchBarLayout.alpha = alpha

                    val translationY1 = maxTranslationY - offset
                    backIv1.translationY = -translationY1

                    val translationY2 = backImgHeight2 - backImgHeight1
                    backIv2.translationY = -translationY2
                }
            }
        }
        refreshLayout.setOnMultiPurposeListener(purposeListener);
    }


}