package com.example.playgroundkt.activity.other

import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity

/**
 *
 * @Description: 复现SharePreference导致的ANR
 * @Author: chunxiong.gu
 * @CreateDate: 2021/3/1 14:56
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/1 14:56
 * @UpdateRemark: 更新说明
 */
@Route(path = RouterPath.SharePreferenceANRActivity)
class SharePreferenceANRActivity : BaseEntranceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sp = getSharedPreferences("testANR", Context.MODE_PRIVATE)

        val time = System.currentTimeMillis()
//        sp.edit().clear().commit()
        println("repeat start ")
        repeat(1000) {
            println("repeat count $it")
            val key = "test integer$it"
//            val result = sp.edit().putInt(key, it).apply()
            val result = sp.getInt(key,1)
            println("repeat result:$result")
        }
        sp.edit().commit()
        println("repeat end : ${System.currentTimeMillis() - time}ms")
    }
}