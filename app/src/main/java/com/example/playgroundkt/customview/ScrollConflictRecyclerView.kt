package com.example.playgroundkt.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * @Description: 解决滑动冲突的recyclerview
 * @Author: chunxiong.gu
 * @CreateDate: 2021/3/11 20:50
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/11 20:50
 * @UpdateRemark: 更新说明
 */
class ScrollConflictRecyclerView : RecyclerView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mLastX = 0
    private var mLastY = 0

    private var mIsInnerIntercept = false

    /**
     * 设置是否启用内部拦截法处理滑动冲突
     */
    fun setInnerIntercept(isInner: Boolean) {
        mIsInnerIntercept = isInner
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (mIsInnerIntercept) {
            val x = ev.x
            val y = ev.y
            when (ev.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    // 请求父view不拦截
                    // 这里down事件请求不拦截，后续的mov等事件都由该view处理
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = x - mLastX
                    val dy = y - mLastY

                    if (abs(dx) > abs(dy)) {
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
                MotionEvent.ACTION_UP -> {
                }
                MotionEvent.ACTION_CANCEL -> {
                }
            }
            mLastX = x.toInt()
            mLastY = y.toInt()
        }

        return super.dispatchTouchEvent(ev)
    }
}