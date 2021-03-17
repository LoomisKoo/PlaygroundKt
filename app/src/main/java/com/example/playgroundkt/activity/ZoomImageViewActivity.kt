package com.example.playgroundkt.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.R
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.databinding.ActivityZoomImageBinding

@Route(path = RouterPath.ZoomImageViewActivity)
class ZoomImageViewActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityZoomImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityZoomImageBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}