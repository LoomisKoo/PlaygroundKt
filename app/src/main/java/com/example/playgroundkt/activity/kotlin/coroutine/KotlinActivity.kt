package com.example.playgroundkt.activity.kotlin.coroutine

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity

@Route(path = RouterPath.KotlinActivity)
class KotlinActivity : BaseEntranceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addButton("协程启动方式") {
            ARouter.getInstance()
                .build(RouterPath.LaunchTypeActivity)
                .navigation()
        }

        addButton("协程同步与异步") {
            ARouter.getInstance()
                .build(RouterPath.AsyncActivity)
                .navigation()
        }
        addButton("flow") {
            ARouter.getInstance()
                .build(RouterPath.FlowActivity)
                .navigation()
        }
    }
}