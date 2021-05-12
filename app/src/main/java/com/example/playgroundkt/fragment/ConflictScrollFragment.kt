package com.example.playgroundkt.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.playgroundkt.R
import com.example.playgroundkt.databinding.FragmentConflictScrollBinding
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 *
 * @ProjectName: PlaygroundKt
 * @Package: com.example.playgroundkt.fragment
 * @ClassName: ConflicScrollFragment
 * @Description: java类作用描述
 * @Author: chunxiong.gu
 * @CreateDate: 2021/3/11 18:45
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/11 18:45
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
class ConflictScrollFragment : Fragment() {
    private lateinit var mBinding: FragmentConflictScrollBinding
    val adapter = Adapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentConflictScrollBinding.inflate(inflater, container, false)
        mBinding.btnAdd.onClick {
            val data = ArrayList<String>()
            for (i in 0..10) data.add(i.toString())
            adapter.addData(data)
        }
        // 是否启用内部拦截法处理滑动冲突
        val isInner = arguments?.getBoolean("isInner")

        val data = ArrayList<String>()
        for (i in 0..10) data.add(i.toString())

        adapter.setNewInstance(data)
        //设置adapter
        mBinding.recyclerView.adapter = adapter
        mBinding.recyclerView.layoutManager = GridLayoutManager(activity, 3)
        mBinding.recyclerView.setInnerIntercept(isInner ?: false)

        return mBinding.root
    }

    class Adapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_conflict_scroll) {
        override fun convert(holder: BaseViewHolder, item: String) {
            val textView = holder.getView<TextView>(R.id.text_view)
            textView.text = item
        }
    }
}