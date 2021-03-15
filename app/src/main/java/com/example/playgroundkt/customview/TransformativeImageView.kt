package com.example.playgroundkt.customview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.widget.Scroller
import androidx.appcompat.widget.AppCompatImageView
import com.example.playgroundkt.R
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt


/**
 * @Description: java类作用描述
 * @Author: chunxiong.gu
 * @CreateDate: 2021/3/9 17:00
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/9 17:00
 * @UpdateRemark: 更新说明
 */
/**
 * 来源：https://www.jianshu.com/p/938ca88fb16a
 */
open class TransformativeImageView constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) :
    AppCompatImageView(
        context, attrs, defStyleAttr
    ) {
    private var mRevertDuration = DEFAULT_REVERT_DURATION // 回弹动画时间
    private var mMaxScaleFactor = MAX_SCALE_FACTOR // 最大缩放比例
    private var mMinScaleFactor = UNSPECIFIED_SCALE_FACTOR // 此最小缩放比例优先级高于下面两个
    private var mVerticalMinScaleFactor = MIN_SCALE_FACTOR // 图片最初的最小缩放比例
    private var mHorizontalMinScaleFactor = MIN_SCALE_FACTOR // 图片旋转90（或-90）度后的的最小缩放比例
    protected var mMatrix = Matrix() // 用于图片旋转、平移、缩放的矩阵
    private var mImageRect = RectF() // 保存图片所在区域矩形，坐标为相对于本View的坐标
    private var mOpenScaleRevert = false // 是否开启缩放回弹
    private var mOpenRotateRevert = false // 是否开启旋转回弹
    private var mOpenTranslateRevert = false // 是否开启平移回弹
    private var mOpenAnimator = false // 是否开启动画

    //滑动帮助类
    private lateinit var mScroller: Scroller

    private var mVelocityTracker: VelocityTracker? = null

    private var mVelocityX = 0 // x轴移动速度
    private var mVelocityY = 0 // y轴移动速度

    private var mFinalX = 0 // fling的最终X坐标
    private var mFinalY = 0 // fling的最终Y坐标
    private var mDownX = 0f // 按下的x坐标
    private var mDownY = 0f // 按下的y坐标
    private var mDX = 0f // X轴每次MOVE的距离
    private var mDY = 0f // Y轴每次MOVE的距离

    private var mIsInFling = false // 是否在fling中

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)

    private fun obtainAttrs(attrs: AttributeSet?) {
        if (attrs == null) return
        val typedArray = context
            .obtainStyledAttributes(attrs, R.styleable.TransformativeImageView)
        mMaxScaleFactor = typedArray.getFloat(
            R.styleable.TransformativeImageView_max_scale, MAX_SCALE_FACTOR
        )
        mMinScaleFactor = typedArray.getFloat(
            R.styleable.TransformativeImageView_min_scale, UNSPECIFIED_SCALE_FACTOR
        )
        mRevertDuration = typedArray.getInteger(
            R.styleable.TransformativeImageView_revert_duration, DEFAULT_REVERT_DURATION
        )
        mOpenScaleRevert = typedArray.getBoolean(
            R.styleable.TransformativeImageView_open_scale_revert, false
        )
        mOpenRotateRevert = typedArray.getBoolean(
            R.styleable.TransformativeImageView_open_rotate_revert, false
        )
        mOpenTranslateRevert = typedArray.getBoolean(
            R.styleable.TransformativeImageView_open_translate_revert, false
        )
        mOpenAnimator = typedArray.getBoolean(
            R.styleable.TransformativeImageView_open_animator, true
        )
        mScaleBy = typedArray.getInt(
            R.styleable.TransformativeImageView_scale_center, SCALE_BY_IMAGE_CENTER
        )
        typedArray.recycle()

        mScroller = Scroller(context)
    }

    private fun init() {
        // FIXME 修复图片锯齿,关闭硬件加速ANTI_ALIAS_FLAG才能生效
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        scaleType = ScaleType.MATRIX
        mRevertAnimator.duration = mRevertDuration.toLong()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        initImgPositionAndSize()
    }

    /**
     * 初始化图片位置和大小
     */
    private fun initImgPositionAndSize() {
        mMatrix.reset()
        // 初始化ImageRect
        refreshImageRect()

        // 计算缩放比例，使图片适应控件大小
        mHorizontalMinScaleFactor =
            (width / mImageRect.width()).coerceAtMost(height / mImageRect.height())
        mVerticalMinScaleFactor =
            (height / mImageRect.width()).coerceAtMost(width / mImageRect.height())
        var scaleFactor = mHorizontalMinScaleFactor
        // 初始图片缩放比例比最小缩放比例稍大
        scaleFactor *= INIT_SCALE_FACTOR
        mScaleFactor = scaleFactor
        mMatrix.postScale(scaleFactor, scaleFactor, mImageRect.centerX(), mImageRect.centerY())
        refreshImageRect()
        // 移动图片到中心
        mMatrix.postTranslate(
            (right - left) / 2 - mImageRect.centerX(),
            (bottom - top) / 2 - mImageRect.centerY()
        )
        applyMatrix()

        // 如果用户有指定最小缩放比例则使用用户指定的
        if (mMinScaleFactor != UNSPECIFIED_SCALE_FACTOR) {
            mHorizontalMinScaleFactor = mMinScaleFactor
            mVerticalMinScaleFactor = mMinScaleFactor
        }
    }

    private val mDrawFilter =
        PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    override fun onDraw(canvas: Canvas) {
        canvas.drawFilter = mDrawFilter
        super.onDraw(canvas)
    }

    private val mLastPoint1 = PointF() // 上次事件的第一个触点
    private val mLastPoint2 = PointF() // 上次事件的第二个触点
    private val mCurrentPoint1 = PointF() // 本次事件的第一个触点
    private val mCurrentPoint2 = PointF() // 本次事件的第二个触点

    private var mScaleFactor = 1.0f // 当前的缩放倍数
    private var mCanScale = false // 是否可以缩放
    private var mLastMidPoint = PointF() // 图片平移时记录上一次ACTION_MOVE的点
    private var mScaleMidPoint = PointF() // 图片缩放中点

    private val mCurrentMidPoint = PointF() // 当前各触点的中点

    private var mCanDrag = false // 是否可以平移
    private val mLastVector = PointF() // 记录上一次触摸事件两指所表示的向量
    private val mCurrentVector = PointF() // 记录当前触摸事件两指所表示的向量

    private var mCanRotate = false // 判断是否可以旋转
    private val mRevertAnimator = MatrixRevertAnimator() // 回弹动画
    private val mFromMatrixValue = FloatArray(9) // 动画初始时矩阵值
    private val mToMatrixValue = FloatArray(9) // 动画终结时矩阵值

    private var isTransforming = false // 图片是否正在变化

    private var isNeedFling = false // 是否需要惯性滑动
    private var isMultiPoint = false // 是否多点触控


    override fun onTouchEvent(event: MotionEvent): Boolean {
        handleTouchEvent(event)
        super.onTouchEvent(event)
        return true
    }

    private fun handleTouchEvent(event: MotionEvent) {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // 标志着第一个手指按下
                mDownX = event.x //获取按下时x坐标值
                mDownY = event.y //获取按下时y坐标值

                //创建惯性滑动速度追踪类对象
                mVelocityTracker = VelocityTracker.obtain()
                pointDown(event)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                isMultiPoint = true
                val midPoint = getMidPointOfFinger(event)
                mScaleMidPoint.set(midPoint)
                pointDown(event)
            }
            MotionEvent.ACTION_MOVE -> {
                //按住一点手指开始移动
                mDX = mDownX - event.x //计算当前已经移动的x轴方向的距离
                mDY = mDownY - event.y //计算当前已经移动的y轴方向的距离

                translateImg(event)

                //将事件加入到VelocityTracker类实例中
                mVelocityTracker?.addMovement(event)
                //计算1秒内滑动的像素个数
                mVelocityTracker?.computeCurrentVelocity(100)
                //X轴方向的速度
                mVelocityX = mVelocityTracker?.xVelocity?.toInt() ?: 0
                //Y轴方向的速度
                mVelocityY = mVelocityTracker?.yVelocity?.toInt() ?: 0
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isNeedFling = isNeedFling(isMultiPoint)
                if (isNeedFling) {
                    fling()
                } else {
                    boundAnimation()
                }

                isMultiPoint = false

                mCanScale = false
                mCanDrag = false
                mCanRotate = false

                mFinalX = mScroller.finalX
                mFinalY = mScroller.finalY

                invalidate()

                mVelocityTracker?.recycle()
                mVelocityTracker?.clear()
                mVelocityTracker = null
            }
            MotionEvent.ACTION_POINTER_UP -> {
                mCanScale = false
                mCanDrag = false
                mCanRotate = false
            }
        }
    }

    /**
     * 使图片进行平移、旋转、缩放等操作
     */
    private fun translateImg(event: MotionEvent) {
        // 多指操作。可进行缩放和旋转
        if (isMultiPoint) {
            if (mCanScale) {
                scale(event)
            }
            if (mCanRotate) {
                rotate(event)
            }
            // 判断图片是否发生了变换
            if (imageMatrix != mMatrix) isTransforming = true
            applyMatrix()
        }
        // 单指操作。可进行平移
        else {
            if (!mCanDrag) return
            if (mScroller.isFinished) {
                mScroller.startScroll(mFinalX, mFinalY, mDX.toInt(), mDY.toInt(), 0)
                // 这里是为了触发computeScroll()  在该方法里面进行平移
                invalidate()
            }
           
        }
    }

    private fun fling() {
        //获取认为是fling的最小速率
//                val mMinimumFlingVelocity =
//                    ViewConfiguration.get(context).scaledMaximumFlingVelocity / 1000
//                if (abs(xVelocity) >= mMinimumFlingVelocity || abs(yVelocity) > mMinimumFlingVelocity) {

        if (abs(mVelocityX) > 50 || abs(mVelocityY) > 50) {
            // fling前拿到最新的XY值
            mFinalX = mScroller.finalX
            mFinalY = mScroller.finalY

            mScroller.fling(
                mFinalX,
                mFinalY,
                -mVelocityX,
                -mVelocityY,
                -(mImageRect.width().toInt() ushr 1),
                mImageRect.width().toInt() ushr 1,
                -(mImageRect.height().toInt() ushr 1),
                mImageRect.height().toInt() ushr 1
            )
            mIsInFling = true
        } else { // 缓慢滑动则回弹
            boundAnimation()
        }
    }


    /**
     * 是否需要惯性滑动
     */
    private fun isNeedFling(isMultiPoint: Boolean): Boolean {
        return when {
            isMultiPoint -> false // 若多点触控，则不进行fling
            isHasSpaceWithBorder() -> false
            !isHasSpaceWithBorder() -> true
            mOpenTranslateRevert -> false // 这个优先级最低
            else -> true
        }
    }


    /**
     * 图片与view边界是否存在间隙
     */
    private fun isHasSpaceWithBorder(): Boolean {
        // 图片与view边界是否有间隙
        var isHasSpaceWithBorder = false

        // mImageRect中的坐标值为相对View的值
        // 图片宽大于控件时图片与控件之间不能有白边
        if (mImageRect.width() > width) {
            if (mImageRect.left > 0 || mImageRect.right < width) { /*判断图片左右边界与控件之间是否有空隙*/
                isHasSpaceWithBorder = true
            }
        } else { /*宽小于控件则移动到中心*/
            isHasSpaceWithBorder = true
        }
        // 图片高大于控件时图片与控件之间不能有白边
        if (mImageRect.height() > height) {
            if (mImageRect.top > 0 || mImageRect.bottom < height) { /*判断图片上下边界与控件之间是否有空隙*/
                isHasSpaceWithBorder = true
            }
        } else { /*高小于控件则移动到中心*/
            isHasSpaceWithBorder = true
        }

        return isHasSpaceWithBorder
    }

    /**
     * 回弹动画
     */
    private fun boundAnimation() {
        mMatrix.getValues(mFromMatrixValue)
        // 设置矩阵动画初始值
        // 旋转和缩放都会影响矩阵，进而影响后续需要使用到ImageRect的地方，所以检测顺序不能改变
        if (mOpenRotateRevert) checkRotation()
        if (mOpenScaleRevert) checkScale()
        if (mOpenTranslateRevert) resetBorder()

        mMatrix.getValues(mToMatrixValue) // 设置矩阵动画结束值
        if (mOpenAnimator) {
            // 启动回弹动画
            mRevertAnimator.setMatrixValue(mFromMatrixValue, mToMatrixValue)
            mRevertAnimator.cancel()
            mRevertAnimator.start()
        } else {
            applyMatrix()
        }
    }


    private fun borderResetAnimation() {
        mMatrix.getValues(mFromMatrixValue)
        if (mOpenTranslateRevert) resetBorder()
        mMatrix.getValues(mToMatrixValue) // 设置矩阵动画结束值

        if (mOpenAnimator) {
            // 启动回弹动画
            mRevertAnimator.setMatrixValue(mFromMatrixValue, mToMatrixValue)
            mRevertAnimator.cancel()
            mRevertAnimator.start()
        } else {
            applyMatrix()
        }
    }

    private fun pointDown(event: MotionEvent) {
        isTransforming = false
        mRevertAnimator.cancel()
        // 新手指落下则需要重新判断是否可以对图片进行变换
        mCanRotate = false
        mCanScale = false
        mCanDrag = false

        if (event.pointerCount == 2) {
            // 旋转、平移、缩放分别使用三个判断变量，避免后期某个操作执行条件改变
            mCanScale = true
            mLastPoint1[event.getX(0)] = event.getY(0)
            mLastPoint2[event.getX(1)] = event.getY(1)
            mCanRotate = true
            mLastVector[event.getX(1) - event.getX(0)] = event.getY(1) - event.getY(0)
        } else if (event.pointerCount == 1) {
            mCanDrag = true
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {//判断滚动是否完成，true说明滚动尚未完成，false说明滚动已经完成
            val curPoint = PointF(-mScroller.currX.toFloat(), -mScroller.currY.toFloat())
            handleTouchEvent(curPoint)
            applyMatrix()
            invalidate()//触发view重绘
        } else {
            if (mIsInFling && isHasSpaceWithBorder()) {
                mIsInFling = false
                borderResetAnimation()
            }
        }
    }

    /**
     * 旋转
     */
    private fun rotate(event: MotionEvent) {
        // 计算当前两指触点所表示的向量
        mCurrentVector[event.getX(1) - event.getX(0)] = event.getY(1) - event.getY(0)
        // 获取旋转角度
        val degree = getRotateDegree(mLastVector, mCurrentVector)
        mMatrix.postRotate(degree, mImageRect.centerX(), mImageRect.centerY())
        mLastVector.set(mCurrentVector)
    }

    /**
     * 使用Math#atan2(double y, double x)方法求上次触摸事件两指所示向量与x轴的夹角，
     * 再求出本次触摸事件两指所示向量与x轴夹角，最后求出两角之差即为图片需要转过的角度
     *
     * @param lastVector    上次触摸事件两指间连线所表示的向量
     * @param currentVector 本次触摸事件两指间连线所表示的向量
     * @return 两向量夹角，单位“度”，顺时针旋转时为正数，逆时针旋转时返回负数
     */
    private fun getRotateDegree(lastVector: PointF, currentVector: PointF): Float {
        //上次触摸事件向量与x轴夹角
        val lastRad = atan2(lastVector.y.toDouble(), lastVector.x.toDouble())
        //当前触摸事件向量与x轴夹角
        val currentRad = atan2(currentVector.y.toDouble(), currentVector.x.toDouble())
        // 两向量与x轴夹角之差即为需要旋转的角度
        val rad = currentRad - lastRad
        //“弧度”转“度”
        return Math.toDegrees(rad).toFloat()
    }

    private fun handleTouchEvent(curPoint: PointF) {
        val dx = curPoint.x - mLastMidPoint.x
        val dy = curPoint.y - mLastMidPoint.y
        mMatrix.postTranslate(dx, dy)

        mLastMidPoint.set(curPoint)
    }

    /**
     * 计算所有触点的中点
     *
     * @param event 当前触摸事件
     * @return 本次触摸事件所有触点的中点
     */
    private fun getMidPointOfFinger(event: MotionEvent): PointF {
        // 初始化mCurrentMidPoint
        mCurrentMidPoint[0f] = 0f
        val pointerCount = event.pointerCount
        for (i in 0 until pointerCount) {
            mCurrentMidPoint.x += event.getX(i)
            mCurrentMidPoint.y += event.getY(i)
        }
        mCurrentMidPoint.x /= pointerCount.toFloat()
        mCurrentMidPoint.y /= pointerCount.toFloat()
        return mCurrentMidPoint
    }

    private var mScaleBy = SCALE_BY_IMAGE_CENTER
    private val scaleCenter = PointF()

    /**
     * 获取图片的缩放中心，该属性可在外部设置，或通过xml文件设置
     * 默认中心点为图片中心
     *
     * @return 图片的缩放中心点
     */
    private fun getScaleCenter(): PointF {
        // 使用全局变量避免频繁创建变量
        when (mScaleBy) {
            SCALE_BY_IMAGE_CENTER -> scaleCenter[mImageRect.centerX()] = mImageRect.centerY()
            SCALE_BY_FINGER_MID_POINT -> scaleCenter[mScaleMidPoint.x] = mScaleMidPoint.y
        }
        return scaleCenter
    }

    /**
     * 缩放
     */
    private fun scale(event: MotionEvent) {
        val scaleCenter = getScaleCenter()
        // 初始化当前两指触点
        mCurrentPoint1[event.getX(0)] = event.getY(0)
        mCurrentPoint2[event.getX(1)] = event.getY(1)
        // 计算缩放比例
        val scaleFactor = (distance(mCurrentPoint1, mCurrentPoint2)
                / distance(mLastPoint1, mLastPoint2))

        // 更新当前图片的缩放比例
        mScaleFactor *= scaleFactor
        mMatrix.postScale(
            scaleFactor, scaleFactor,
            scaleCenter.x, scaleCenter.y
        )

        mLastPoint1.set(mCurrentPoint1)
        mLastPoint2.set(mCurrentPoint2)
    }

    /**
     * 获取两点间距离
     */
    private fun distance(point1: PointF, point2: PointF): Float {
        val dx = point2.x - point1.x
        val dy = point2.y - point1.y
        return sqrt((dx * dx + dy * dy).toDouble()).toFloat()
    }

    /**
     * 根据当前图片旋转的角度，判断是否回弹
     */
    private fun checkRotation() {
        val currentDegree = currentRotateDegree
        var degree = currentDegree
        // 根据当前图片旋转的角度值所在区间，判断要转到几度
        degree = abs(degree)
        degree = if (degree > 45 && degree <= 135) {
            90f
        } else if (degree > 135 && degree <= 225) {
            180f
        } else if (degree > 225 && degree <= 315) {
            270f
        } else {
            0f
        }
        // 判断顺时针还是逆时针旋转
        degree = if (currentDegree < 0) -degree else degree
        mMatrix.postRotate(degree - currentDegree, mImageRect.centerX(), mImageRect.centerY())
    }

    private val xAxis = floatArrayOf(1f, 0f) // 表示与x轴同方向的向量// 每次重置初始向量的值为与x轴同向
    // 初始向量通过矩阵变换后的向量
    // 变换后向量与x轴夹角
    /**
     * 获取当前图片旋转角度
     *
     * @return 图片当前的旋转角度
     */
    private val currentRotateDegree: Float
        private get() {
            // 每次重置初始向量的值为与x轴同向
            xAxis[0] = 1f
            xAxis[1] = 0f
            // 初始向量通过矩阵变换后的向量
            mMatrix.mapVectors(xAxis)
            // 变换后向量与x轴夹角
            val rad = atan2(
                xAxis[1].toDouble(), xAxis[0].toDouble()
            )
            return Math.toDegrees(rad).toFloat()
        }

    /**
     * 检查图片缩放比例是否超过设置的大小
     */
    private fun checkScale() {
        val scaleCenter = getScaleCenter()
        var scaleFactor = 1.0f

        // 获取图片当前是水平还是垂直
        val imgOrientation = imgOrientation()
        // 超过设置的上限或下限则回弹到设置的限制值
        // 除以当前图片缩放比例mScaleFactor，postScale()方法执行后的图片的缩放比例即为被除数大小
        if (imgOrientation == HORIZONTAL
            && mScaleFactor < mHorizontalMinScaleFactor
        ) {
            scaleFactor = mHorizontalMinScaleFactor / mScaleFactor
        } else if (imgOrientation == VERTICAL
            && mScaleFactor < mVerticalMinScaleFactor
        ) {
            scaleFactor = mVerticalMinScaleFactor / mScaleFactor
        } else if (mScaleFactor > mMaxScaleFactor) {
            scaleFactor = mMaxScaleFactor / mScaleFactor
        }
        mMatrix.postScale(scaleFactor, scaleFactor, scaleCenter.x, scaleCenter.y)
        mScaleFactor *= scaleFactor
    }

    /**
     * 判断图片当前是水平还是垂直
     *
     * @return 水平则返回 `HORIZONTAL`，垂直则返回 `VERTICAL`
     */
    private fun imgOrientation(): Int {
        val degree = abs(currentRotateDegree)
        var orientation = HORIZONTAL
        if (degree > 45f && degree <= 135f) {
            orientation = VERTICAL
        }
        return orientation
    }

    /**
     * 将图片移回控件中心
     */
    private fun resetBorder() {
        // 默认不移动
        var dx = 0f
        var dy = 0f

        // mImageRect中的坐标值为相对View的值
        // 图片宽大于控件时图片与控件之间不能有白边
        if (mImageRect.width() > width) {
            if (mImageRect.left > 0) { /*判断图片左边界与控件之间是否有空隙*/
                dx = -mImageRect.left
            } else if (mImageRect.right < width) { /*判断图片右边界与控件之间是否有空隙*/
                dx = width - mImageRect.right
            }
        } else { /*宽小于控件则移动到中心*/
            dx = width / 2 - mImageRect.centerX()
        }

        // 图片高大于控件时图片与控件之间不能有白边
        if (mImageRect.height() > height) {
            if (mImageRect.top > 0) { /*判断图片上边界与控件之间是否有空隙*/
                dy = -mImageRect.top
            } else if (mImageRect.bottom < height) { /*判断图片下边界与控件之间是否有空隙*/
                dy = height - mImageRect.bottom
            }
        } else { /*高小于控件则移动到中心*/
            dy = height / 2 - mImageRect.centerY()
        }

        if (dx != 0f || dy != 0f) {
            // 由于旋转回弹与缩放回弹会影响图片所在位置，所以此处需要更新ImageRect的值
            refreshImageRect()
            mMatrix.postTranslate(dx, dy)
        }

    }

    /**
     * 更新图片所在区域，并将矩阵应用到图片
     */
    protected fun applyMatrix() {
        refreshImageRect() /*将矩阵映射到ImageRect*/
        imageMatrix = mMatrix
    }

    /**
     * 图片使用矩阵变换后，刷新图片所对应的mImageRect所指示的区域
     */
    private fun refreshImageRect() {
        if (drawable != null) {
            mImageRect.set(drawable.bounds)
            mMatrix.mapRect(mImageRect, mImageRect)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mRevertAnimator.cancel()
    }
    //-----Aninmator-------------------
    /**
     * 图片回弹动画
     */
    private inner class MatrixRevertAnimator : ValueAnimator(),
        AnimatorUpdateListener {
        private var mFromMatrixValue // 动画初始时矩阵值
                : FloatArray? = null
        private var mToMatrixValue // 动画终结时矩阵值
                : FloatArray? = null
        private val mInterpolateMatrixValue // 动画执行过程中矩阵值
                : FloatArray?

        fun setMatrixValue(fromMatrixValue: FloatArray?, toMatrixValue: FloatArray?) {
            mFromMatrixValue = fromMatrixValue
            mToMatrixValue = toMatrixValue
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mMatrix.setValues(toMatrixValue)
                    applyMatrix()
                }
            })
        }

        override fun onAnimationUpdate(animation: ValueAnimator) {
            if (mFromMatrixValue != null && mToMatrixValue != null && mInterpolateMatrixValue != null) {
                // 根据动画当前进度设置矩阵的值
                for (i in 0..8) {
                    val animatedValue = animation.animatedValue as Float
                    mInterpolateMatrixValue[i] = (mFromMatrixValue!![i]
                            + (mToMatrixValue!![i] - mFromMatrixValue!![i]) * animatedValue)
                }
                mMatrix.setValues(mInterpolateMatrixValue)
                applyMatrix()
            }
        }

        init {
            mInterpolateMatrixValue = FloatArray(9)
            setFloatValues(0f, 1f)
            addUpdateListener(this)
        }
    }

    //-------getter and setter---------
    fun setmMaxScaleFactor(mMaxScaleFactor: Float) {
        this.mMaxScaleFactor = mMaxScaleFactor
    }

    companion object {
        private val TAG = TransformativeImageView::class.java.simpleName
        private const val MAX_SCALE_FACTOR = 2.0f // 默认最大缩放比例为2
        private const val UNSPECIFIED_SCALE_FACTOR = -1f // 未指定缩放比例
        private const val MIN_SCALE_FACTOR = 1.0f // 默认最小缩放比例为0.3
        private const val INIT_SCALE_FACTOR = 1.2f // 默认适应控件大小后的初始化缩放比例
        private const val DEFAULT_REVERT_DURATION = 300
        private const val SCALE_BY_IMAGE_CENTER = 0 // 以图片中心为缩放中心
        private const val SCALE_BY_FINGER_MID_POINT = 1 // 以所有手指的中点为缩放中心
        private const val HORIZONTAL = 0
        private const val VERTICAL = 1
    }

    init {
        obtainAttrs(attrs)
        init()
    }
}