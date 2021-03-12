package com.example.playgroundkt.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

/**
 * @Description: 测试事件分发的ViewGroup
 * @Author: chunxiong.gu
 * @CreateDate: 2021/3/12 16:17
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/12 16:17
 * @UpdateRemark: 更新说明
 */
class DispatchEventView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    companion object {
        const val RETURN_TYPE_SUPER = 0
        const val RETURN_TYPE_TRUE = 1
        const val RETURN_TYPE_FALSE = 2
    }

    private var dispatchReturnType = RETURN_TYPE_SUPER
    private var onTouchReturnType = RETURN_TYPE_SUPER


    var instanceName = ""

    fun setViewInstanceName(name: String) {
        instanceName = name
    }

    fun setDispatchReturnType(type: Int) {
        dispatchReturnType = type
    }

    fun setOnTouchReturnType(type: Int) {
        onTouchReturnType = type
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        println("DispatchEventView  dispatchTouchEvent           $instanceName         ${getType(dispatchReturnType)}")
        return when (dispatchReturnType) {
            RETURN_TYPE_SUPER -> super.dispatchTouchEvent(ev)
            RETURN_TYPE_FALSE -> false
            RETURN_TYPE_TRUE -> true
            else -> super.dispatchTouchEvent(ev)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        println("DispatchEventView  onTouchEvent                 $instanceName         ${getType(onTouchReturnType)}")

        return when (onTouchReturnType) {
            RETURN_TYPE_SUPER -> super.onTouchEvent(event)
            RETURN_TYPE_FALSE -> false
            RETURN_TYPE_TRUE -> true
            else -> super.onTouchEvent(event)
        }
    }

    private fun getType(type: Int): String {
        return when (type) {
            RETURN_TYPE_SUPER -> "super"
            RETURN_TYPE_FALSE -> "false"
            RETURN_TYPE_TRUE -> "true"
            else -> ""
        }
    }

}