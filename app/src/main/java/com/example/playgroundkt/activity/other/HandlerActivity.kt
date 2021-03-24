package com.example.playgroundkt.activity.other

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity

@Route(path = RouterPath.HandlerActivity)
class HandlerActivity : BaseEntranceActivity() {

    var handler1: Handler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            println("koo----- handler1")
        }
    }

    var handler2 = Handler(Looper.myLooper()!!) {
        println("koo----- handler2")
        false
    }
    var handler3 = Handler(Looper.myLooper()!!)

    var handler4 = Handler() {
        println("koo----- handler4")
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addButton("start and view logcat") {
            handler1.sendMessage(handler1.obtainMessage())
            handler2.sendEmptyMessage(0)
            handler3.postDelayed({
                println("koo----- handler3")
            }, 100)


            Thread {
                Looper.prepare()
                handler4.sendEmptyMessage(0)
                Looper.loop()
            }.start()
        }
    }
}