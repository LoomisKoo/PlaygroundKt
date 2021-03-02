package com.example.playgroundkt.activity.jetpack

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity

@Route(path = RouterPath.RoomActivity)
class RoomActivity : BaseEntranceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}