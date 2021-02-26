package com.example.playgroundkt.adapter

import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.playgroundkt.data.HomePageData
import com.example.playgroundkt.R
import com.example.playgroundkt.base.DataBindingVH
import com.example.playgroundkt.databinding.ItemHomePageBinding
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *
 * @ProjectName: PlaygroundKt
 * @Package: com.example.playgroundkt.adapter
 * @ClassName: HomePageAdapter
 * @Description: java类作用描述
 * @Author: chunxiong.gu
 * @CreateDate: 2021/2/26 13:53
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/2/26 13:53
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
class HomePageAdapter : BaseQuickAdapter<HomePageData, DataBindingVH>(R.layout.item_home_page) {
    override fun convert(holder: DataBindingVH, item: HomePageData) {
        val binding = holder.getDataBinding() as ItemHomePageBinding
        binding.button.text = item.name
        binding.button.onClick {
            ARouter.getInstance()
                .build(item.routePath)
                .navigation()
        }
    }
}