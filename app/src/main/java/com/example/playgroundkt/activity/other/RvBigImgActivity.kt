package com.example.playgroundkt.activity.other

import android.graphics.*
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.playgroundkt.R
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity
import com.example.playgroundkt.customview.ScrollImageView
import java.util.*
import kotlin.collections.HashMap

@Route(path = RouterPath.RvBigImgActivity)
class RvBigImgActivity : BaseEntranceActivity() {
    private val dataList = ArrayList<Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rv = RecyclerView(this)
        rv.layoutManager = LinearLayoutManager(this)
        val adapter = Adapter()

        for (i in 0..30) {
            if (i % 3 == 0) {
                dataList.add(Data(i.toString(), 1, 0))
            } else {
                dataList.add(Data(i.toString(), 0, 0))
            }
        }

        adapter.setNewInstance(dataList)
        rv.adapter = adapter

        addView(rv)

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager

                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager is LinearLayoutManager) {
                    //获取最后一个可见view的位置
                    val lastItemPosition = layoutManager.findLastVisibleItemPosition()
                    //获取第一个可见view的位置
                    val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
                    for (position in firstItemPosition until lastItemPosition) {
                        if (dataList[position].type != 1) continue
                        mImageMap[position]?.setMatrix(dy)
                    }
                }
            }
        })
    }

    val mImageMap = HashMap<Int, ScrollImageView>()

    inner class Adapter : BaseQuickAdapter<Data, BaseViewHolder>(R.layout.item_big_img) {
        override fun convert(holder: BaseViewHolder, item: Data) {
            if (item.type == 0) {
                holder.getView<TextView>(R.id.tv_content).text = item.content
                holder.getView<ImageView>(R.id.iv_content).visibility = View.GONE
                holder.getView<TextView>(R.id.tv_content).visibility = View.VISIBLE
            } else {
                val imageView = holder.getView<ScrollImageView>(R.id.iv_content)
                imageView.let {
                    mImageMap[holder.adapterPosition] = it
                    it.visibility = View.VISIBLE
                }

                holder.getView<TextView>(R.id.tv_content).visibility = View.GONE
            }
        }

    }


    data class Data(
        val content: String,
        val type: Int = 0,
        var dy: Int = 0
    )
}