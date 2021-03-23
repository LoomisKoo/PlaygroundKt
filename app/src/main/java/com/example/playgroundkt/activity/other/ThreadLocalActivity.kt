package com.example.playgroundkt.activity.other

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity

/**
 *
 */
@Route(path = RouterPath.ThreadLocalActivity)
class ThreadLocalActivity : BaseEntranceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addButton("打印ThreadLocal变量") {
            val threadLocal1 = ThreadLocal<Int>()
            val threadLocal2 = ThreadLocal<Int>()
            for (i in 0..5) {
                Thread {
                    threadLocal1.set(i)
                    threadLocal2.set(i)
                    val localValue1 = threadLocal1.get()
                    val localValue2 = threadLocal2.get()
                    println("koo----- curThread:${Thread.currentThread()}     value1:$localValue1     value2:$localValue2")
                }.start()
            }
        }
    }
}