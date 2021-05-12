package com.example.playgroundkt.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import com.example.playgroundkt.util.BitmapFillet
import com.github.mmin18.widget.RealtimeBlurView

/**
 * @Description: java类作用描述
 * @Author: chunxiong.gu
 * @CreateDate: 2021/4/17 15:26
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/17 15:26
 * @UpdateRemark: 更新说明
 */
class BlurView(context: Context?, attrs: AttributeSet?) : RealtimeBlurView(context, attrs) {

    override fun drawBlurredBitmap(canvas: Canvas, blurredBitmap: Bitmap, overlayColor: Int) {
        // 由于重写了blur方法直接设置背景，所以这里不再实现
    }

    override fun blur(bitmapToBlur: Bitmap, blurredBitmap: Bitmap) {
        super.blur(bitmapToBlur, blurredBitmap)
        val topCornerBlurBitmap = BitmapFillet.fillet(blurredBitmap, 20, BitmapFillet.CORNER_TOP)
        background = BitmapDrawable(
            context.resources,
            topCornerBlurBitmap
        )
    }
}