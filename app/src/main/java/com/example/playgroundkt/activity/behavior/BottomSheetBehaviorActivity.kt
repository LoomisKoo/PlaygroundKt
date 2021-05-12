package com.example.playgroundkt.activity.behavior

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.jetpackmvvm.base.activity.BaseDbActivity
import com.example.playgroundkt.R
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.databinding.ActivityBottomSheetBehaviorBinding

@Route(path = RouterPath.BottomSheetBehaviorActivity)
class BottomSheetBehaviorActivity : BaseDbActivity<ActivityBottomSheetBehaviorBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val content =
            "本文地址http://www.cnblogs.com/goagent/p/5159125.html本文地址啊本文。地址。啊http://www.cnblogs.com/goagent/p/5159125.html"
        mDatabind.tvContent.text = content
        mDatabind.tvContent.post {
//            val content = mDatabind.tvContent.autoSplitText(mDatabind.tvContent)
            mDatabind.tvContent.text = content
//            mDatabind.tvContent.invalidate()
            mDatabind.tvContent.requestLayout()

        }
    }

    override fun layoutId(): Int {
        return R.layout.activity_bottom_sheet_behavior
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun showLoading(message: String) {
    }

    override fun dismissLoading() {
    }

    override fun createObserver() {
    }
}