package com.example.playgroundkt.data

/**
 *
 * @ProjectName: PlaygroundKt
 * @Package: com.example.playgroundkt.data
 * @ClassName: ReflectData
 * @Description: java类作用描述
 * @Author: chunxiong.gu
 * @CreateDate: 2021/3/25 16:05
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/25 16:05
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
class ReflectPersonData {
    private var name: String = "koo"
    private var age: Int = 21

    constructor(name: String, age: Int) {
        this.name = name
        this.age = age
    }

    constructor(name: String) {
        this.name = name
    }

    constructor()

    fun getName(): String {
        return name
    }

    fun getAge(): Int {
        return age
    }

    private fun setName(name: String) {
        this.name = name
    }


}