package com.example.playgroundkt.activity.other

import android.graphics.Outline
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.jetpackmvvm.base.activity.BaseDbActivity
import com.example.playgroundkt.R
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.databinding.ActivityOutlineProviderBinding

@Route(path = RouterPath.OutlineProviderActivity)
class OutlineProviderActivity : BaseDbActivity<ActivityOutlineProviderBinding>() {
    override fun layoutId(): Int {
        return R.layout.activity_outline_provider
    }

    override fun initView(savedInstanceState: Bundle?) {
        val provider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                view?.height?.let { outline?.setRoundRect(0, 0, view.width, it,500f) }
                outline?.alpha = 0.5f
            }
        }


        mDatabind.view.clipToOutline = false

        mDatabind.view.outlineProvider = provider
    }

    override fun showLoading(message: String) {
    }

    override fun dismissLoading() {
    }

    override fun createObserver() {
    }
}