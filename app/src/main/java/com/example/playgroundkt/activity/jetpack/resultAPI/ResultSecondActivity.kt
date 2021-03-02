package com.example.playgroundkt.activity.jetpack.resultAPI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity
import org.jetbrains.anko.toast


/**
 * result API
 */
@Route(path = RouterPath.ResultSecondActivity)
class ResultSecondActivity : BaseEntranceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = intent.getStringExtra("testString")
        toast("上一个页面传过来的数据：$data")
        println("上一个页面传过来的数据：$data")

        addButton("返回上一页") {
            val intent = Intent().apply {
                putExtra("result","test result")
            }
            setResult(Activity.RESULT_OK,intent)

            finish()
        }
    }

}