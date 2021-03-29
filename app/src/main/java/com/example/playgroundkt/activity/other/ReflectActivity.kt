package com.example.playgroundkt.activity.other

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity
import com.example.playgroundkt.data.ReflectPersonData
import java.lang.reflect.Method
import java.lang.reflect.Modifier


/**
 * 参考：https://www.jianshu.com/p/9be58ee20dee
 */
@Route(path = RouterPath.ReflectActivity)
class ReflectActivity : BaseEntranceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addButton("获取name和age") {
            val name = getPrivateValue("name")
            val age = getPrivateValue("age")
            println("koo----- name:$name  age:$age")
        }

        addButton("通过class获取实例") {
            val instance = getInstance(ReflectPersonData::class.java) as ReflectPersonData?
            instance?.getName()
            println("koo----- instance   name:${instance?.getName()}  age:${instance?.getAge()}")
        }

        addButton("通过构造函数获取实例") {
            getInstanceByConstruct()
        }

        addButton("获取类的一些信息") {
            getClassInfo()
        }

        addButton("调用方法") {
            invokeMethod()
        }

        addButton("获取类加载器名称") {
            getClassLoaderInfo()
        }
    }

    /**
     * 通过反射获取私有的成员变量
     *
     * @return
     */
    private fun getPrivateValue(fieldName: String): Any? {
        val person = ReflectPersonData()
        try {
            // 获取class的几种方式
//            val personClass = Class.forName("ReflectPersonData")
//            val personClass = Class.forName("com.example.playgroundkt.data.ReflectPersonData")
//            val personClass = person.javaClass
            val personClass = ReflectPersonData::class.java

            val field = personClass.getDeclaredField(fieldName)
            // 参数值为true，打开禁用访问控制检查
            //setAccessible(true) 并不是将方法的访问权限改成了public，而是取消java的权限控制检查。
            //所以即使是public方法，其accessible 属相默认也是false
            field.isAccessible = true
            return field.get(person)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 通过反射获取实例
     */
    private fun getInstance(oneClass: Class<out Any>): Any? {
        oneClass.newInstance()
        // 由于这里不能带参数，所以实例化的这个类一定要有无参构造函数
        return oneClass.newInstance()
    }

    /**
     * 通过构造函数获取实例
     */
    private fun getInstanceByConstruct() {
        val personClass = ReflectPersonData::class.java
        val constructors = personClass.declaredConstructors
        try {
            val person1 = constructors[0].newInstance() as ReflectPersonData
            val person2 = constructors[1].newInstance("koo") as ReflectPersonData
            val person3 = constructors[2].newInstance("koo", 21) as ReflectPersonData
            println(
                "koo----- " +
                        " person1 ${person1.getName()}   ${person1.getAge()}" +
                        " person2 ${person2.getName()}   ${person2.getAge()}" +
                        " person3 ${person3.getName()}   ${person3.getAge()}"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 通过Java反射机制得到类的一些属性： 继承的接口，父类，函数信息，成员信息，类型等
     */
    private fun getClassInfo() {
        try {
            val personClass = ReflectPersonData::class.java
            // 父类class
            val superClass = personClass.superclass
            println("koo----- 父类名称：${superClass.name}")

            println("koo----- =================================")

            val fields = personClass.declaredFields
            for (field in fields) {
                println("koo----- 类中的成员: ${field.name}")
            }

            println("koo----- =================================")

            val methods = personClass.methods
            for (method in methods) {
                println("koo----- 函数名：${method.name}")
                println("koo----- 函数返回类型：${method.returnType}")
                println("koo----- 函数访问修饰符：${Modifier.toString(method.modifiers)}")
                println("koo----- 函数代码写法： $method")
            }

            println("koo----- =================================")

            val classInterface = personClass.interfaces
            for (i in classInterface) {
                println("koo----- 实现的接口类名: ${i.name}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    /**
     * 调用方法
     */
    private fun invokeMethod() {
        try {
            val personClass = ReflectPersonData::class.java
            val instance = personClass.newInstance()

            val method1: Method = personClass.getDeclaredMethod("getName")
            var name: String = method1.invoke(instance) as String

            println("koo----- name:$name")

            val method2: Method = personClass.getDeclaredMethod("setName", String::class.java)
            method2.isAccessible = true
            method2.invoke(instance, "changeName")
            name = method1.invoke(instance) as String

            println("koo----- changeName:$name")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 获取类加载器的信息
     */
    private fun getClassLoaderInfo() {
        val personClass = ReflectPersonData::class.java
        val loaderName = personClass.classLoader::class.java.name
        println("koo----- loaderName$loaderName")

    }
}