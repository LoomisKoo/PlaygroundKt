package com.example.playgroundkt.activity.kotlin

import android.graphics.Bitmap
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

@Route(path = RouterPath.FlowActivity)
class FlowActivity : BaseEntranceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addButton("sample emit") {
            lifecycleScope.launch {
                sample().collect { value -> println("sample emit $value") }
            }
        }

        addButton("区间转化为flow") {
            lifecycleScope.launch {
                (1..5).asFlow()
                    .onEach { delay(300) }// 每次发射延迟300ms
                    .collect {
                        println("区间转化为flow  $it")
                    }
            }
        }

        addButton("字符串list转化为flow") {
            lifecycleScope.launch {
                listOf("a", "b", "c", "d").asFlow()
                    .onEach { delay(300) }
                    .collect {
                        println("字符串list转化为flow  $it")
                    }
            }
        }

        addButton("map") {
            lifecycleScope.launch {
                (1..5).asFlow()
                    .map { doSomething() }
                    .collect {
                        println("map  $it")
                    }
            }
        }

        // transform 操作符
        addButton("transform") {
            lifecycleScope.launch {
                (1..5).asFlow()
                    .transform {
                        // 每次发射都执行以下两行代码
                        emit("transform before doSomething")
                        doSomething()
                    }
                    .collect {
                        println("transform  $it")
                    }
            }
        }

        // take操作符
        addButton("5 take 3") {
            lifecycleScope.launch {
                (1..5).asFlow()
                    .take(3)
                    .collect {
                        println("5 take 3  $it")
                    }
            }
        }

        // takeWhile操作符
        addButton("5 take 3") {
            lifecycleScope.launch {
                (1..5).asFlow()
                    .takeWhile { it <= 4 }
                    .collect {
                        println("5 takeWhile <= 4  $it")
                    }
            }
        }

        // reduce操作符
        addButton("1..5 sum by reduce ") {
            lifecycleScope.launch {
                val sum = (1..5).asFlow()
                    .map { it * it }
                    .reduce { a, b -> a + b } // 求和
                println("reduce sum $sum")
            }
        }

        // fold
        addButton("1..5 sum by fold 10 ") {
            lifecycleScope.launch {
                val sum = (1..5).asFlow()
                    .map { it * it }
                    .fold(10) { a, b -> a + b } // 加上基数10再求和
                println("fold sum $sum")
            }
        }

        // filter操作符
        addButton("1..100 filter dual（输出双数）") {
            lifecycleScope.launch {
                (1..100).asFlow()
                    .onEach { delay(300) }
                    .filter { it % 2 == 0 }
                    .collect { println("filter sum $it") }
            }
        }

        // 通常，withContext 用于在 Kotlin 协程中改变代码的上下文，
        // 但是 flow {...} 构建器中的代码必须遵循上下文保存属性，并且不允许从其他上下文中发射
        // 这种方式会报错
        addButton("主线程中调用Dispatchers.Default的flow") {
            lifecycleScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.Default) {
                    (1..5).asFlow()
                        .onEach { delay(1000) }
                        .collect { println("主线程中调用Dispatchers.Default的flow  $it") }
                }
            }
        }

        // flowOn 这种可以改变 flow 的上下文
        // flowOn 只影响它之前的操作，不影响 collect（这里的 collect 在主线程）
        addButton("主线程中调用Dispatchers.Default的flow flowOn") {
            lifecycleScope.launch(Dispatchers.Main) {
                (1..5).asFlow()
                    .onEach { delay(1000) }
                    .flowOn(Dispatchers.Default)
                    .collect { println("主线程中调用Dispatchers.Default的flow flowOn $it") }
            }
        }

        // buffer
        // 在流上使用 buffer 操作符来并发运行这个 simple 流中发射元素的代码以及收集的代码，
        // 而不是顺序运行
        addButton("buffer") {
            lifecycleScope.launch(Dispatchers.Main) {
                val time = measureTimeMillis {
                    sample().buffer()
                        .collect { println("buffer $it") }
                }
                println("buffer time:$time")
            }
        }

        // conflate
        // 虽然第一个数字仍在处理中，但第二个和第三个数字已经产生，因此第二个是 conflated ，
        // 只有最新的（第三个）被交付给收集器
        addButton("conflate") {
            lifecycleScope.launch(Dispatchers.Main) {
                val time = measureTimeMillis {
                    sample().conflate()
                        .collect {
                            delay(300)
                            println("conflate $it")
                        }
                }
                println("conflate time:$time")
            }
        }

        // 处理最新值
        addButton("collectLatest") {
            lifecycleScope.launch(Dispatchers.Main) {
                val time = measureTimeMillis {
                    sample().collectLatest {
                        println("collectLatest value $it")
                        delay(300)
                        println("collectLatest done $it")
                    }
                }
                println("collectLatest time $time")
            }
        }

        // zip 合并了两个 flow 输出个数以两个之中 size 小的为准
        // 如果 flow 有 delay 则会等待delay完成后再合并输出
        addButton("zip") {
            lifecycleScope.launch(Dispatchers.Main) {
                val nums = (1..5).asFlow()
                val str = flowOf("one", "two", "three")
                nums.zip(str) { a, b -> "$a $b" }
                    .collect {
                        println("zip $it")
                    }
            }
        }

        // combine 每次从取两个flow的最新值来合并输出  直到两个flow的元素都发射完
        addButton("combine") {
            lifecycleScope.launch(Dispatchers.Main) {
                val nums = (1..20).asFlow().onEach { delay(1500) }
                val str = flowOf("one", "two", "three").onEach { delay(2000) }
                nums.combine(str) { a, b -> "$a $b" }
                    .collect {
                        println("combine $it")
                    }
            }
        }

        // flatMapConcat
        // 包含流的流（Flow<Flow<String>>），需要将其combine进行展平为单个流以进行下一步处理
        addButton("flatMapConcat") {
            lifecycleScope.launch(Dispatchers.Main) {
                val startTime = System.currentTimeMillis() // 记录开始时间
                (1..3).asFlow().onEach { delay(100) } // 每 100 毫秒发射一个数字
                    .flatMapConcat { requestFlow(it) }
                    .collect { value -> // 收集并打印
                        println("flatMapConcat $value at ${System.currentTimeMillis() - startTime} ms from start")
                    }
            }
        }

        // flatMapMerge
        // flatMapMerge 会顺序调用代码块（本示例中的 { requestFlow(it) }），
        // 但是并发收集结果流，相当于执行顺序是首先执行 map { requestFlow(it) }
        // 然后在其返回结果上调用 flattenMerge
        addButton("flatMapMerge") {
            lifecycleScope.launch(Dispatchers.Main) {
                val startTime = System.currentTimeMillis() // 记录开始时间
                (1..3).asFlow().onEach { delay(100) } // 每 100 毫秒发射一个数字
                    .flatMapMerge { requestFlow(it) }
                    .collect { value -> // 收集并打印
                        println("flatMapMerge $value at ${System.currentTimeMillis() - startTime} ms from start")
                    }
            }
        }

        // flatMapLatest  当发射了新值之后，上个 flow 就会被取消
        addButton("flatMapLatest") {
            lifecycleScope.launch(Dispatchers.Main) {
                val startTime = System.currentTimeMillis() // 记录开始时间
                (1..3).asFlow().onEach { delay(100) } // 每 100 毫秒发射一个数字
                    .flatMapLatest { requestFlow(it) }
                    .collect { value -> // 收集并打印
                        println("flatMapLatest $value at ${System.currentTimeMillis() - startTime} ms from start")
                    }
            }
        }

        // catch
        // catch只能捕获它调用之前的异常
        addButton("catch") {
            lifecycleScope.launch(Dispatchers.Main) {
                exception()
                    .catch { e -> emit("Caught $e") } // 发射一个异常
                    .collect { value -> println(value) }
            }
        }
    }


    /**
     * flow在collect的时候才执行，所以这里不用suspend关键字
     */
    private fun sample(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(300) // 模拟计算等待
            emit(i) // 发射
        }
    }

    private fun requestFlow(i: Int): Flow<String> = flow {
        emit("$i: First")
        delay(500) // 等待 500 毫秒
        emit("$i: Second")
    }

    private fun exception(): Flow<String> = flow {
        for (i in 1..3) {
            println("Emitting $i")
            emit(i) // 发射下一个值
        }
    }.map {
        check(it <= 1) { "Crashed on $it" }
        "string $it"
    }

    private suspend fun doSomething(): String {
        delay(1000)
        return "doSomething respond"
    }
}