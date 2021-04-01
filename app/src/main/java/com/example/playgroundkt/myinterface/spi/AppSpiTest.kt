package com.example.playgroundkt.myinterface.spi

import com.example.spi.ISpiTest


/**
 *
 * @Description: java类作用描述
 * @Author: chunxiong.gu
 * @CreateDate: 2021/4/1 10:30
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/1 10:30
 * @UpdateRemark: 更新说明
 */
class AppSpiTest : ISpiTest {
    constructor()

    override fun getCurModuleName(): String {
        return "app"
    }
}