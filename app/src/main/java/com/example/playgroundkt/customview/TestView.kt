package com.example.playgroundkt.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.OverScroller
import android.widget.Scroller
import androidx.annotation.RequiresApi

/**
 * @Description: java类作用描述
 * @Author: chunxiong.gu
 * @CreateDate: 2021/3/10 10:30
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/10 10:30
 * @UpdateRemark: 更新说明
 */
class TestView : View {
    //惯性滑动速度追踪类
    private var velocityTracker: VelocityTracker? = null
    private var mDownX = 0f
    private var mDonwY = 0f
    private var move_x = 0f
    private var move_y = 0f
    private var finalX = 0
    private var finalY = 0
    private var xVelocity = 0
    private var yVelocity = 0
    private var mPaint: Paint? = null
    private var mScroller: Scroller? = null
    private var mOverScroller: OverScroller? = null


    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    private fun init(mContext: Context) {
        mPaint = Paint()
        mPaint!!.textSize = 80f
        mPaint!!.color = Color.RED
        mPaint!!.strokeWidth = 50f
        mScroller = Scroller(mContext)
        mOverScroller = OverScroller(mContext)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawText("TEST！", 0f, 100f, mPaint!!)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //标志着第一个手指按下
                mDownX = x //获取按下时x坐标值
                mDonwY = y //获取按下时y坐标值

                //创建惯性滑动速度追踪类对象
                velocityTracker = VelocityTracker.obtain()
            }
            MotionEvent.ACTION_MOVE -> {
                //按住一点手指开始移动
                move_x = mDownX - x //计算当前已经移动的x轴方向的距离
                move_y = mDonwY - y //计算当前已经移动的y轴方向的距离

                //开始滚动动画
                //第一个参数：x轴开始位置
                //第二个参数：y轴开始位置
                //第三个参数：x轴偏移量
                //第四个参数：y轴偏移量
                if (mScroller!!.isFinished) {
                    println("koo----- startScroll $finalX   $finalY   $move_x    $move_y")
                    mScroller?.startScroll(finalX, finalY, move_x.toInt(), move_y.toInt(), 0);
                }
                invalidate() //目的是重绘view，是的执行computeScroll方法

                //将事件加入到VelocityTracker类实例中
                velocityTracker!!.addMovement(event)
                //计算1秒内滑动的像素个数
                velocityTracker!!.computeCurrentVelocity(1000)
                //X轴方向的速度
                xVelocity = velocityTracker!!.xVelocity.toInt()
                //Y轴方向的速度
                yVelocity = velocityTracker!!.yVelocity.toInt()
            }
            MotionEvent.ACTION_UP -> {

                //获取认为是fling的最小速率
                val mMinimumFlingVelocity =
                    ViewConfiguration.get(context).scaledMaximumFlingVelocity / 100
                if (Math.abs(xVelocity) >= mMinimumFlingVelocity || Math.abs(yVelocity) > mMinimumFlingVelocity) {
                    Log.d("fling", "$finalX  $finalY")
                    mScroller!!.fling(
                        finalX,
                        finalY,
                        -xVelocity,
                        -yVelocity,
                        -width + 100,
                        0,
                        -height + 100,
                        0
                    )
                } else { //缓慢滑动不处理
                }
                invalidate()

                finalX = mScroller!!.finalX
                finalY = mScroller!!.finalY
                velocityTracker!!.recycle()
                velocityTracker!!.clear()
                velocityTracker = null
            }
            MotionEvent.ACTION_CANCEL -> {
                velocityTracker!!.recycle()
                velocityTracker!!.clear()
                velocityTracker = null
            }
        }
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller?.computeScrollOffset() == true) { //判断滚动是否完成，true说明滚动尚未完成，false说明滚动已经完成
            scrollTo(mScroller!!.currX, mScroller!!.currY) //将view直接移动到当前滚动的位置
            println("koo----- computeScroll " + mScroller!!.currX.toFloat() + "   " + mScroller!!.currY.toFloat())
            invalidate() //触发view重绘
        }
    }
}