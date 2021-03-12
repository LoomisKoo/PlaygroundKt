package com.example.playgroundkt.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

/**
 *
 * @Description: 解决滑动冲突的viewPager
 * @Author: chunxiong.gu
 * @CreateDate: 2021/3/11 17:22
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/11 17:22
 * @UpdateRemark: 更新说明
 */
class ScrollConflictViewPager : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var mLastXIntercept = 0
    private var mLastYIntercept = 0
    private var mIsOuter = false

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // 父类实现了滑动冲突处理，这里注释掉 模拟滑动冲突
//        super.onInterceptTouchEvent(ev);

        return if (mIsOuter) outerIntercept(ev) else innerIntercept(ev)
    }

    fun setIsOuter(isOuter: Boolean) {
        mIsOuter = isOuter
    }

    /**
     * 内部拦截法处理滑动冲突
     */
    private fun innerIntercept(ev: MotionEvent): Boolean {
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            super.onInterceptTouchEvent(ev);
            return false;
        }
        return true
    }

    /**
     * 外部拦截法处理滑动冲突
     */
    private fun outerIntercept(ev: MotionEvent): Boolean {
        // 以下是外部拦截法进行处理滑动冲突
        var isIntercept = false

        val x = ev.x
        val y = ev.y

        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                isIntercept = false
                super.onInterceptTouchEvent(ev);
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = x - mLastXIntercept
                val dy = y - mLastYIntercept

                // 水平滑动，拦截
                isIntercept = abs(dx) > abs(dy)
            }
            MotionEvent.ACTION_UP -> {
                isIntercept = false
            }
            MotionEvent.ACTION_CANCEL -> {
                isIntercept = false
            }
        }

        mLastXIntercept = x.toInt()
        mLastYIntercept = y.toInt()

        return isIntercept
    }
}