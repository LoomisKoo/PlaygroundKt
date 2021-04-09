package com.example.playgroundkt.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.jetpackmvvm.base.activity.BaseDbActivity
import com.example.playgroundkt.R
import com.example.playgroundkt.databinding.ActivityBaseEntranceBinding
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *
 * @Description: 各项功能入口页面的baseActivity
 * @Author: chunxiong.gu
 * @CreateDate: 2021/2/28 10:41
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/2/28 10:41
 * @UpdateRemark: 更新说明
 */
open class BaseEntranceActivity : BaseDbActivity<ActivityBaseEntranceBinding>() {
    override fun layoutId(): Int {
        return R.layout.activity_base_entrance
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun showLoading(message: String) {
    }

    override fun dismissLoading() {
    }

    override fun createObserver() {
    }

    /**
     * @param btnName 按钮名称
     * @param onClickFunc 按钮点击事件函数
     */
    fun addButton(btnName: String, onClickFunc: () -> Unit): Button {
        return Button(this).let {
            it.text = btnName
            it.onClick { onClickFunc() }
            it.isAllCaps = false
            mDatabind.llRoot.addView(it)
            it
        }
    }

    /**
     * 加入一个view
     */
    fun addView(view: View, onClickFunc: () -> Unit) {
        view.onClick { onClickFunc() }
        mDatabind.llRoot.addView(view)
    }

    /**
     * 加入一个view
     */
    fun addView(view: View) {
        mDatabind.llRoot.addView(view)
    }

}