package com.example.playgroundkt.activity.other

import android.os.Bundle
import android.view.MotionEvent
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.jetpackmvvm.base.activity.BaseDbActivity
import com.example.playgroundkt.R
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.customview.DispatchEventView.Companion.RETURN_TYPE_FALSE
import com.example.playgroundkt.customview.DispatchEventView.Companion.RETURN_TYPE_SUPER
import com.example.playgroundkt.customview.DispatchEventView.Companion.RETURN_TYPE_TRUE
import com.example.playgroundkt.databinding.ActivityEventDispatchBinding

/**
 * U型图参考：https://www.jianshu.com/p/e99b5e8bd67b
 */
@Route(path = RouterPath.EventDispatchActivity)
class EventDispatchActivity : BaseDbActivity<ActivityEventDispatchBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDatabind.viewgroup1.instanceName = "viewGroup1"
        mDatabind.viewgroup2.instanceName = "viewGroup2"
        mDatabind.view.instanceName = "view"

        mDatabind.viewgroup1.setDispatchReturnType(RETURN_TYPE_SUPER)
        mDatabind.viewgroup1.setInterceptReturnType(RETURN_TYPE_SUPER)
        mDatabind.viewgroup1.setOnTouchReturnType(RETURN_TYPE_SUPER)

        mDatabind.viewgroup2.setDispatchReturnType(RETURN_TYPE_SUPER)
        mDatabind.viewgroup2.setInterceptReturnType(RETURN_TYPE_SUPER)
        mDatabind.viewgroup2.setOnTouchReturnType(RETURN_TYPE_SUPER)

        mDatabind.view.setDispatchReturnType(RETURN_TYPE_SUPER)
        mDatabind.view.setOnTouchReturnType(RETURN_TYPE_SUPER)


    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        println("DispatchEventView activity ")
        return super.dispatchTouchEvent(ev)
    }

    override fun layoutId(): Int {
        return R.layout.activity_event_dispatch
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun showLoading(message: String) {
    }

    override fun dismissLoading() {
    }

    override fun createObserver() {
    }


    /**
     * viewGroup事件分发的伪代码
     */
    private fun dispatchTouchEvent(): Boolean {
        val disAllowIntercept = false
        var isIntercept = false
        if (disAllowIntercept) {
            isIntercept = onIntercept()
        }

        return if (isIntercept) {
            onTouchEvent()
        } else {
            childDispatchEvent()
        }
    }

    /**
     * 伪代码
     */
    private fun onIntercept(): Boolean {
        return false
    }

    /**
     * 伪代码
     */
    private fun onTouchEvent(): Boolean {
        return false
    }

    /**
     * 伪代码
     */
    private fun childDispatchEvent(): Boolean {
        return false
    }
}