package com.example.playgroundkt.activity.animation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.R
import com.example.playgroundkt.RouterPath

@Route(path = RouterPath.ConstraintSetMotionLayoutActivity)
class ConstraintSetMotionLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_set_motion_layout)
    }
}