package com.example.playgroundkt.activity.other

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity

/**
 * 1.ThreadLocal 为变量在每个线程中都创建了一个副本，那么每个线程可以访问自己内部的副本变量
 * 2.每个 Thread 维护着一个 ThreadLocalMap 的引用
 * 3.ThreadLocalMap 是 ThreadLocal 的内部类，用Entry来进行存储
 * 4.ThreadLocal 创建的副本是存储在自己的 threadLocals 中的，也就是自己的 ThreadLocalMap
 * 5.ThreadLocalMap 的键值为 ThreadLocal 对象，而且可以有多个 threadLocal 变量，因此保存在map中
 * 6.在进行 get 之前，必须先 set，否则会报空指针异常，当然也可以初始化一个，但是必须重写 initialValue() 方法
 * 7.ThreadLocal 本身并不存储值，它只是作为一个key来让线程从 ThreadLocalMap 获取 value
 * 8.ThreadLocal 是一个弱引用，当为 null 时，会被当成垃圾回收，但是 ThreadLocalMap 的 value 不为 null，造成内存泄漏（使用完ThreadLocal后，执行remove操作，避免泄露）
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
                    threadLocal2.set(i + 1)
                    val localValue1 = threadLocal1.get()
                    val localValue2 = threadLocal2.get()
                    println("koo----- curThread:${Thread.currentThread()}     value1:$localValue1     value2:$localValue2")
                }.start()
            }
        }
    }
}