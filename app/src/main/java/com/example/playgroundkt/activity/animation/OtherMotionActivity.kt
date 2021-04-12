package com.example.playgroundkt.activity.animation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.R
import com.example.playgroundkt.RouterPath

@Route(path = RouterPath.OtherMotionActivity)
class OtherMotionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_motion)
    }
}