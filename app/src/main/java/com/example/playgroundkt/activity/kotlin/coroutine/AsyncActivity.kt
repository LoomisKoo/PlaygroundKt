package com.example.playgroundkt.activity.kotlin.coroutine

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity
import kotlinx.coroutines.*
import org.jetbrains.anko.toast
import kotlin.system.measureTimeMillis

@Route(path = RouterPath.AsyncActivity)
class AsyncActivity : BaseEntranceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addButton("计算两个1000ms的任务串行时间") {

            lifecycleScope.launch(Dispatchers.Main) {

                val time = measureTimeMillis {
                    val one = doSomethingUsefulOne()
                    val two = doSomethingUsefulTwo()
                    println("The answer is ${one + two}")
                }
                println("串行两个1000ms任务的时间：$time ms")
                toast("串行两个1000ms任务的时间：$time ms")
            }
        }

        addButton("计算两个1000ms的任务并行时间") {
            lifecycleScope.launch(Dispatchers.Main) {
                val time = measureTimeMillis {
                    val one = async { doSomethingUsefulOne() }
                    val two = async { doSomethingUsefulTwo() }
                    println("The answer is ${one.await() + two.await()}")
                }
                println("并行两个1000ms任务的时间：$time ms")
                toast("并行两个1000ms任务的时间：$time ms")
            }
        }

        addButton("惰性计算两个1000ms的任务并行时间") {
            lifecycleScope.launch(Dispatchers.Main) {
                val time = measureTimeMillis {
                    //  async 可以通过将 start 参数设置为 CoroutineStart.LAZY 而变为惰性的。
                    //  在这个模式下，只有结果通过 await 获取的时候协程才会启动（这样启动是串行），
                    //  或者在 Job 的 start 函数调用的时候（这样启动是并行）。
                    val one = async (start = CoroutineStart.LAZY){ doSomethingUsefulOne() }
                    val two = async (start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
                    one.start()// 启动第一个
                    two.start()// 启动第二个
                    println("The answer is ${one.await() + two.await()}")
                }
                println("惰性并行两个1000ms任务的时间：$time ms")
                toast("惰性并行两个1000ms任务的时间：$time ms")
            }
        }
    }

    private suspend fun doSomethingUsefulOne(): Int {
        delay(1000L) // 假设我们在这里做了些有用的事
        return 13
    }

    private suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L) // 假设我们在这里也做了一些有用的事
        return 29
    }
}