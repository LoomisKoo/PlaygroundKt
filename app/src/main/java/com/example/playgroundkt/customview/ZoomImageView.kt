package com.example.playgroundkt.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.min


/**
 *
 * @Description: java类作用描述
 * @Author: chunxiong.gu
 * @CreateDate: 2021/3/9 16:13
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/9 16:13
 * @UpdateRemark: 更新说明
 */
class ZoomImageView(context: Context, attrs: AttributeSet?) :
    AppCompatImageView(context, attrs), OnScaleGestureListener, OnTouchListener,
    OnGlobalLayoutListener {
    /**
     * 初始化时的缩放比例，如果图片宽或高大于屏幕，此值将小于0
     */
    private var initScale = 1.0f
    private var once = true

    /**
     * 用于存放矩阵的9个值
     */
    private val matrixValues = FloatArray(9)

    /**
     * 缩放的手势检测
     */
    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private val mScaleMatrix: Matrix = Matrix()

    /**
     * 用于双击检测
     */
    private val mGestureDetector: GestureDetector
    private var isAutoScale = false
    private val mTouchSlop = 0
    private var mLastX = 0f
    private var mLastY = 0f
    private var isCanDrag = false
    private var lastPointerCount = 0
    private var isCheckTopAndBottom = true
    private var isCheckLeftAndRight = true

    constructor(context: Context) : this(context, null) {}

    /**
     * 自动缩放的任务
     * @author zhy
     */
    private inner class AutoScaleRunnable(
        private val mTargetScale: Float,
        /**
         * 缩放的中心
         */
        private var x: Float, y: Float
    ) :
        Runnable {
        private var tmpScale = 0f
        private val y: Float
        override fun run() {
            // 进行缩放
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y)
            checkBorderAndCenterWhenScale()
            imageMatrix = mScaleMatrix
            val currentScale = getScale()
            // 如果值在合法范围内，继续缩放
            if (tmpScale > 1f && currentScale < mTargetScale || tmpScale < 1f && mTargetScale < currentScale) {
                postDelayed(this, 16)
            } else {
                // 设置为目标的缩放比例
                val deltaScale = mTargetScale / currentScale
                mScaleMatrix.postScale(deltaScale, deltaScale, x, y)
                checkBorderAndCenterWhenScale()
                imageMatrix = mScaleMatrix
                isAutoScale = false
            }
        }



        /**
         * 传入目标缩放值，根据目标值与当前值，判断应该放大还是缩小
         * @param targetScale
         */
        init {
            x = x
            this.y = y
            tmpScale = if (getScale() < mTargetScale) {
                Companion.BIGGER
            } else {
                Companion.SMALLER
            }
        }
    }

    companion object {
        const val BIGGER = 1.07f
        const val SMALLER = 0.93f
        private val TAG = ZoomImageView::class.java.simpleName
        const val SCALE_MAX = 3.0f
        private const val SCALE_MID = 1.5f
    }

    /**
     * 对图片进行缩放的控制，首先进行缩放范围的判断，然后设置mScaleMatrix的scale值
     * @param detector
     * @return
     */
    @SuppressLint("NewApi")
    override fun onScale(detector: ScaleGestureDetector): Boolean {
        val scale = getScale()
        var scaleFactor = detector.scaleFactor
        if (drawable == null) return true
        /**
         * 缩放的范围控制
         */
        if (scale < SCALE_MAX && scaleFactor > 1.0f || scale > initScale && scaleFactor < 1.0f) {
            /**
             * 最大值最小值判断
             */
            if (scaleFactor * scale < initScale) {
                scaleFactor = initScale / scale
            }
            if (scaleFactor * scale > SCALE_MAX) {
                scaleFactor = SCALE_MAX / scale
            }
            /**
             * 设置缩放比例
             */
            mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)
            checkBorderAndCenterWhenScale()
            imageMatrix = mScaleMatrix
        }
        return true
    }

    /**
     * 在缩放时，进行图片显示范围的控制
     */
    private fun checkBorderAndCenterWhenScale() {
        val rect = getMatrixRectF()
        var deltaX = 0f
        var deltaY = 0f
        val width = width
        val height = height

        // 如果宽或高大于屏幕，则控制范围
        if (rect.width() >= width) {
            if (rect.left > 0) {
                deltaX = -rect.left
            }
            if (rect.right < width) {
                deltaX = width - rect.right
            }
        }
        if (rect.height() >= height) {
            if (rect.top > 0) {
                deltaY = -rect.top
            }
            if (rect.bottom < height) {
                deltaY = height - rect.bottom
            }
        }
        // 如果宽或高小于屏幕，则让其居中
        if (rect.width() < width) {
            deltaX = width * 0.5f - rect.right + 0.5f * rect.width()
        }
        if (rect.height() < height) {
            deltaY = height * 0.5f - rect.bottom + 0.5f * rect.height()
        }
        Log.e(TAG, "deltaX = $deltaX , deltaY = $deltaY")
        mScaleMatrix.postTranslate(deltaX, deltaY)
    }

    /**
     * 根据当前图片的Matrix获得图片的范围
     * @return
     */
    private fun getMatrixRectF(): RectF {
        val matrix: Matrix = mScaleMatrix
        val rect = RectF()
        val d = drawable
        if (null != d) {
            rect[0f, 0f, d.intrinsicWidth.toFloat()] = d.intrinsicHeight.toFloat()
            matrix.mapRect(rect)
        }
        return rect
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {}

    /**
     * 我们让OnTouchListener的MotionEvent交给ScaleGestureDetector进行处理
     * public boolean onTouch(View v, MotionEvent event){
     * return mScaleGestureDetector.onTouchEvent(event);
     * }
     */
    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (mGestureDetector.onTouchEvent(event)) return true
        mScaleGestureDetector!!.onTouchEvent(event)
        var x = 0f
        var y = 0f
        // 拿到触摸点的个数
        val pointerCount = event.pointerCount
        // 得到多个触摸点的x与y均值
        for (i in 0 until pointerCount) {
            x += event.getX(i)
            y += event.getY(i)
        }
        x = x / pointerCount
        y = y / pointerCount
        /**
         * 每当触摸点发生变化时，重置mLasX , mLastY
         */
        if (pointerCount != lastPointerCount) {
            isCanDrag = false
            mLastX = x
            mLastY = y
        }
        lastPointerCount = pointerCount
        val rectF = getMatrixRectF()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (rectF.width() > width || rectF.height() > height) {
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                if (rectF.width() > width || rectF.height() > height) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                Log.e(TAG, "ACTION_MOVE")
                var dx = x - mLastX
                var dy = y - mLastY
                if (!isCanDrag) {
                    isCanDrag = isCanDrag(dx, dy)
                }
                if (isCanDrag) {
                    if (drawable != null) {
                        // if (getMatrixRectF().left == 0 && dx > 0)
                        // {
                        // getParent().requestDisallowInterceptTouchEvent(false);
                        // }
                        //
                        // if (getMatrixRectF().right == getWidth() && dx < 0)
                        // {
                        // getParent().requestDisallowInterceptTouchEvent(false);
                        // }
                        isCheckTopAndBottom = true
                        isCheckLeftAndRight = isCheckTopAndBottom
                        // 如果宽度小于屏幕宽度，则禁止左右移动
                        if (rectF.width() < width) {
                            dx = 0f
                            isCheckLeftAndRight = false
                        }
                        // 如果高度小雨屏幕高度，则禁止上下移动
                        if (rectF.height() < height) {
                            dy = 0f
                            isCheckTopAndBottom = false
                        }

                        //设置偏移量
                        mScaleMatrix.postTranslate(dx, dy)
                        //再次校验
                        checkMatrixBounds()
                        imageMatrix = mScaleMatrix
                    }
                }
                mLastX = x
                mLastY = y
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                Log.e(TAG, "ACTION_UP")
                lastPointerCount = 0
            }
        }
        return true
    }

    /**
     * 获得当前的缩放比例
     * @return
     */
    fun getScale(): Float {
        mScaleMatrix.getValues(matrixValues)
        return matrixValues[Matrix.MSCALE_X]
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeGlobalOnLayoutListener(this)
    }

    /**
     * 根据图片的宽和高以及屏幕的宽和高，对图片进行缩放以及移动至屏幕的中心。
     * 如果图片很小，那就正常显示，不放大了~
     */
    override fun onGlobalLayout() {
        if (once) {
            val d = drawable ?: return
            Log.e(TAG, d.intrinsicWidth.toString() + " , " + d.intrinsicHeight)
            val width = width
            val height = height
            // 拿到图片的宽和高
            val dw = d.intrinsicWidth
            val dh = d.intrinsicHeight
            var scale = 1.0f
            // 如果图片的宽或者高大于屏幕，则缩放至屏幕的宽或者高
            if (dw > width && dh <= height) {
                scale = width * 1.0f / dw
            }
            if (dh > height && dw <= width) {
                scale = height * 1.0f / dh
            }
            // 如果宽和高都大于屏幕，则让其按按比例适应屏幕大小
            if (dw > width && dh > height) {
                scale = min(width * 1.0f / dw, height * 1.0f / dh)
            }
            initScale = scale
            Log.e(TAG, "initScale = $initScale")
            mScaleMatrix.postTranslate(((width - dw) / 2).toFloat(), ((height - dh) / 2).toFloat())
            mScaleMatrix.postScale(
                scale,
                scale,
                (getWidth() / 2).toFloat(),
                (getHeight() / 2).toFloat()
            )
            // 图片移动至屏幕中心
            imageMatrix = mScaleMatrix
            once = false
        }
    }

    /**
     * 移动时，进行边界判断，主要判断宽或高大于屏幕的
     */
    private fun checkMatrixBounds() {
        val rect = getMatrixRectF()
        var deltaX = 0f
        var deltaY = 0f
        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()
        // 判断移动或缩放后，图片显示是否超出屏幕边界
        if (rect.top > 0 && isCheckTopAndBottom) {
            deltaY = -rect.top
        }
        if (rect.bottom < viewHeight && isCheckTopAndBottom) {
            deltaY = viewHeight - rect.bottom
        }
        if (rect.left > 0 && isCheckLeftAndRight) {
            deltaX = -rect.left
        }
        if (rect.right < viewWidth && isCheckLeftAndRight) {
            deltaX = viewWidth - rect.right
        }
        mScaleMatrix.postTranslate(deltaX, deltaY)
    }

    /**
     * 是否是推动行为
     *
     * @param dx
     * @param dy
     * @return
     */
    private fun isCanDrag(dx: Float, dy: Float): Boolean {
        return Math.sqrt((dx * dx + dy * dy).toDouble()) >= mTouchSlop
    }


    init {
        super.setScaleType(ScaleType.MATRIX)
        mGestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                if (isAutoScale) return true
                val x = e.x
                val y = e.y
                Log.e("DoubleTap", getScale().toString() + " , " + initScale)
                isAutoScale = if (getScale() < SCALE_MID) {
                    //postDelayed(); 16 ：多久实现一次的定时器操作
                    postDelayed(AutoScaleRunnable(SCALE_MID, x, y), 16)
                    true
                } /*else if (getScale() >= SCALE_MID  //连续双击放大 可放开
                                    && getScale() < SCALE_MAX) {
                                ZoomImageView.this.postDelayed(new AutoScaleRunnable(SCALE_MAX, x, y), 16);
                                isAutoScale = true;
                            }*/ else {
                    postDelayed(AutoScaleRunnable(initScale, x, y), 16)
                    true
                }
                return true
            }
        })
        mScaleGestureDetector = ScaleGestureDetector(context, this)
        setOnTouchListener(this)
    }
}