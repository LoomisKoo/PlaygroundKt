package com.example.playgroundkt.activity.kotlin.coroutine

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity
import kotlinx.coroutines.*
import java.lang.Exception

/**
 *
 * @ProjectName: PlaygroundKt
 * @Package: com.example.playgroundkt.activity
 * @ClassName: CoroutineActivity
 * @Description: java类作用描述
 * @Author: chunxiong.gu
 * @CreateDate: 2021/2/26 15:42
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/2/26 15:42
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

/**
 * 该activity测试的功能如下
 * 1.GlobalScope和lifecycleScope启动协程的区别（是否具有生命周期感知）
 * 2.协程串行、并发、同步
 * 3.协程正常取消、退出
 * 4.协程种的异常传递、捕获机制
 */

@Route(path = RouterPath.LaunchTypeActivity)
class LaunchTypeActivity : BaseEntranceActivity() {
    private var globalJob: Job? = null

    /**
     * 运行时退出页面，观察打印的log。GlobalScope方式启动的协程仍在打印
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("koo----- ------------------onCreate--------------------")

        TextView(this).let {
            it.text = "点击以下按钮可测试对应的功能项，然后手动切换activity的生命周期（比如回到桌面），观察log的打印即可"
            it.textSize = 15f
            it.setPadding(10, 10, 10, 10)
        }


        addButton("globalScope") {
            globalScope()
        }
        addButton("lifecycleScope") {
            lifecycleScope()
        }
        addButton("lifecycleScopeWithTimeout") {
            lifecycleScopeWithTimeout()
        }
        addButton("suspendFunction") {
            suspendFunction()
        }
        addButton("runBlocking") {
            runBlocking()
        }
    }

    override fun createObserver() {
    }

    override fun onResume() {
        super.onResume()
        println("koo----- ------------------onResume--------------------")
    }

    override fun onRestart() {
        super.onRestart()
        println("koo----- ------------------onRestart--------------------")
    }

    override fun onStart() {
        super.onStart()
        println("koo----- ------------------onStart--------------------")

    }

    override fun onPause() {
        super.onPause()
        println("koo----- ------------------onPause--------------------")
    }

    override fun onStop() {
        super.onStop()
        println("koo----- ------------------onStop--------------------")
    }

    override fun onDestroy() {
        super.onDestroy()
//        globalJob?.cancel()
//        println("koo----- globalJob?.cancel()")
        println("koo----- ------------------onDestroy--------------------")
    }

    /**
     * GlobalScope方式启动协程
     * 这种方式的生命周期的process的 不推荐使用
     */
    private fun globalScope() {
        globalJob = GlobalScope.launch {
            repeat(1000) { i ->
                delay(1000)
                println("koo-----  GlobalScope.launch  $i")
            }
        }
    }

    /**
     * lifecycleScope方式启动协程
     * 这种方式的生命周期与activity或fragment绑定 推荐使用
     */
    private fun lifecycleScope() {
        lifecycleScope.launchWhenResumed {
            repeat(1000) { i ->
                delay(1000)
                println("koo-----  lifecycleScope.launch  $i")
            }
        }
    }

    /**
     * lifecycleScope方式启动协程
     * 这种方式的生命周期与activity或fragment绑定 推荐使用
     */
    private fun lifecycleScopeWithTimeout() {
        lifecycleScope.launchWhenResumed {
            try {
                // 这种方式不会抛出异常
//                withTimeoutOrNull(2000) {
                // 这种方式会抛出异常
                withTimeout(2000) {
                    repeat(1000) { i ->
                        delay(1000)
                        println("koo-----  lifecycleScope.launch  $i")
                    }
                }
            } catch (e: Exception) {
                // 这里是可以catch到异常的，但是没有try-catch  也不会崩溃 todo koo 为何？
                println("koo----- $e")
            }
        }
    }

    /**
     *
     */
    private fun runBlocking() {
        println("koo----- runBlocking start")
        // 这里代表阻塞
        runBlocking {
            // 这里会挂起线程
            lifecycleScope.launchWhenResumed {
                delay(5000)
            }
            // 这方式会阻塞
            launch {
                delay(10000)
            }
        }
        println("koo----- runBlocking end")
    }

    /**
     * viewModelScope方式启动协程
     * 这种方式的生命周期与viewModel绑定 推荐使用(只能在viewModel种使用)
     */
    fun viewModelScope() {
//        viewModelScope.launch {
//        }
    }

    /**
     *
     */
    private fun suspendFunction() {
        lifecycleScope.launchWhenResumed {
            withContext(Dispatchers.Main) {
                println("koo----- suspendFunction start")
                suspendFunction2()
                println("koo----- suspendFunction end")
            }
        }
    }

    private suspend fun suspendFunction2() {
        println("koo----- suspendFunction2 start")
        delay(2000)
        println("koo----- suspendFunction2 end")
    }
}