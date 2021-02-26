package com.example.playgroundkt.base

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *
 * @ProjectName: PlaygroundKt
 * @Package: com.example.playgroundkt.base
 * @ClassName: DataBindingVH
 * @Description:
 * @Author: chunxiong.gu
 * @CreateDate: 2021/2/26 13:59
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/2/26 13:59
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
class DataBindingVH(view: View) : BaseViewHolder(view) {
    private var mDataBinding: ViewDataBinding? = null

    init {
        mDataBinding = DataBindingUtil.bind(itemView)
    }

    fun getDataBinding(): ViewDataBinding? {
        return mDataBinding
    }
}