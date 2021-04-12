package com.example.playgroundkt.activity.animation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.R
import com.example.playgroundkt.RouterPath

/**
 * MotionLayout 是一个 Google 官方出品用于制作 Android 中的过渡动画的框架。用来它就能轻松的做出一些较为复杂的动画效果
 * 由于 MotionLayout 类继承自 ConstraintLayout 类，因此可以在布局中使用 MotionLayout 替换掉 ConstraintLayout
 */
@Route(path = RouterPath.MotionLayoutActivity)
class TranslateMotionLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_layout_end)
    }
}