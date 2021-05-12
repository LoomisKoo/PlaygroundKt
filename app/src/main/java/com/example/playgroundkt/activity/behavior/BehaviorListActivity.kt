package com.example.playgroundkt.activity.behavior

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity

@Route(path = RouterPath.BehaviorListActivity)
class BehaviorListActivity : BaseEntranceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addButton("bottomSheet"){
            ARouter.getInstance().build(RouterPath.BottomSheetBehaviorActivity).navigation()
        }
    }
}