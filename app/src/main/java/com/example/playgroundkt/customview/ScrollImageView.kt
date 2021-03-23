package com.example.playgroundkt.customview

import android.content.Context
import android.graphics.Matrix
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

/**
 * @Description: java类作用描述
 * @Author: chunxiong.gu
 * @CreateDate: 2021/3/22 14:11
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/22 14:11
 * @UpdateRemark: 更新说明
 */
class ScrollImageView : AppCompatImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var isGetBounds = false
    var hasScale = false
    private var bounds: Rect? = null
    private var mTranslateY = 0

    fun setMatrix(dy: Int) {
        if (!isGetBounds && drawable.bounds.width() > 0 && width > 0) {
//            isGetBounds = true
            bounds = drawable.bounds
        }


        bounds?.let {
            mTranslateY += dy
            val scaleRate: Float = width / it.width().toFloat()

            if (mTranslateY > 0) {
                mTranslateY = 0
            } else if (mTranslateY < -(it.height() * scaleRate - height)) {
                mTranslateY = -((it.height() * scaleRate - height)).toInt()
            }
            val matrix = Matrix()
            // 设置放缩比例
            matrix.setScale(scaleRate, scaleRate)

            // 若view的高度比src还高，则把src垂直居中
            if (height > it.height() * scaleRate) {
                // 平移到中心
                matrix.preTranslate(0f, (height / 2 - it.height() * scaleRate / 2) / scaleRate)
            } else {
                // 平移
                matrix.preTranslate(0f, mTranslateY.toFloat() / scaleRate)
            }

            println("koo-----2 scaleRate   $scaleRate   ")


            imageMatrix = matrix
        }
    }
}