package com.example.playgroundkt.activity.other

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
import kotlin.collections.LinkedHashMap

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
                    for (position in firstItemPosition until lastItemPosition - 1) {
                        if (dataList[position].type != 1) continue
                        var translateY = adapter.data[position].translateY
                        translateY = mImageMap[position]?.setMatrix(translateY, dy) ?: 0
                        adapter.data[position].translateY = translateY
                    }
                }
            }
        })
    }

    /**
     * 最多保存3个imageview的实例（LRU算法）
     */
    val mImageMap = MaxHashMap<Int, ScrollImageView>(3)

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


    class MaxHashMap<K, V>(val maxSize: Int) : LinkedHashMap<K, V>(maxSize, 1f, false) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
            return (size > maxSize);
        }
    }


    data class Data(
        val content: String,
        val type: Int = 0,
        var translateY: Int = 0
    )
}