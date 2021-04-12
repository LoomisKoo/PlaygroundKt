package com.example.playgroundkt.activity.animation

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity

/**
 * 参考链接：https://www.freesion.com/article/27021403334/
 */
@Route(path = RouterPath.AnimationListActivity)
class AnimationListActivity : BaseEntranceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addButton("基本motion动画") {
            ARouter.getInstance().build(RouterPath.MotionLayoutActivity).navigation()
        }

        addButton("登录动画") {
            ARouter.getInstance().build(RouterPath.ConstraintSetMotionLayoutActivity).navigation()
        }

        addButton("仿照Youtube动画效果") {
            ARouter.getInstance().build(RouterPath.MotionYoutubeActivity).navigation()
        }

        addButton("其他动画效果") {
            ARouter.getInstance().build(RouterPath.OtherMotionActivity).navigation()
        }
    }
}