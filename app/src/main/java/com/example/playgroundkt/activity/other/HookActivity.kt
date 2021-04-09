package com.example.playgroundkt.activity.other

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity

/**
 * hookActivity: https://blog.csdn.net/gdutxiaoxu/article/details/81459910
 */
@Route(path = RouterPath.HookActivity)
class HookActivity : BaseEntranceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val btnHook = Button(this).let {
            it.text = "hook view的点击事件"
            it.setOnClickListener { println("koo----- 原始点击事件") }
            it.isAllCaps = false
            mDatabind.llRoot.addView(it)
            it
        }
        hookOnClick(btnHook)
    }


    private fun hookOnClick(view: View) {
        val listenerInfoMethod = View::class.java.getDeclaredMethod("getListenerInfo")
        listenerInfoMethod.isAccessible = true
        val listenerInfo = listenerInfoMethod.invoke(view)
        val listenerClz = Class.forName("android.view.View\$ListenerInfo")
        val mOnClickListener = listenerClz.getDeclaredField("mOnClickListener")
        mOnClickListener.isAccessible = true

        val originOnClickListener: View.OnClickListener =
            mOnClickListener.get(listenerInfo) as View.OnClickListener

        mOnClickListener.set(listenerInfo, HookOnClickListener(originOnClickListener))
    }


    class HookOnClickListener : View.OnClickListener {
        private var mOriginalListener: View.OnClickListener? = null

        constructor(listener: View.OnClickListener?) {
            mOriginalListener = listener
        }

        override fun onClick(v: View?) {
            println("koo----- hook点击前的事件")
            mOriginalListener?.onClick(v)
            println("koo----- hook点击后的事件")
        }
    }
}




























