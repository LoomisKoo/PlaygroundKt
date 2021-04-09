package com.example.playgroundkt.activity.other

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.spi.ISpiTest
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity
import java.util.*

/**
 * @see ISP.md
 */
@Route(path = RouterPath.IspActivity)
class SpiActivity : BaseEntranceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addButton("打印通过ISP获取的module名称") {
            val loader = ServiceLoader.load(ISpiTest::class.java)
            val iterator = loader.iterator()
            while (iterator.hasNext()) {
                val moduleName = iterator.next().getCurModuleName()
                println("koo----- serviceLoader moduleName: $moduleName")
            }
        }
    }
}