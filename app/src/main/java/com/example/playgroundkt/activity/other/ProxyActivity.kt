package com.example.playgroundkt.activity.other

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * 动态代理详解：
 * https://blog.csdn.net/qq_32625839/article/details/81911029
 * https://blog.51cto.com/hongyanwb/625272
 */
@Route(path = RouterPath.ProxyActivity)
class ProxyActivity : BaseEntranceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addButton("静态代理") {
            val proxy = StaticProxyStaticHello()
            proxy.getStaticHello("我是静态代理。。。")
        }

        addButton("动态代理") {
            val proxyHandler = HelloProxyInvocationHandler(DynamicHelloService())
            val proxy = Proxy.newProxyInstance(
                DynamicHelloService::class.java.classLoader,
//                arrayOf(IDynamicHelloService::class.java, IDynamicHelloService2::class.java),
                // 这里和上面注释的一句同样效果
                DynamicHelloService::class.java.interfaces,
                proxyHandler
            )


            (proxy as IDynamicHelloService).getDynamicContent("我是动态代理1")
            (proxy as IDynamicHelloService2).getDynamicContent2("我是动态代理2")
        }
    }

    /**
     * 静态代理
     * 优点：代理使客户端不需要知道实现类是什么，怎么做的，而客户端只需知道代理即可（解耦合）
     * 缺点：
     * 1.代理类和委托类实现了相同的接口，代理类通过委托类实现了相同的方法。这样就出现了大量的代码重复。
     *   如果接口增加一个方法，除了所有实现类需要实现这个方法外，所有代理类也需要实现此方法。增加了代码维护的复杂度
     * 2.代理对象只服务于一种类型的对象，如果要服务多类型的对象。势必要为每一种对象都进行代理，
     *   静态代理在程序规模稍大时就无法胜任了。如上的代码是只为UserManager类的访问提供了代理，
     *   但是如果还要为其他类如Department类提供代理的话，就需要我们再次添加代理Department的代理类
     */


    /**
     * 接口
     */
    interface IStaticHelloService {
        fun getStaticHello(content: String): String
    }

    /**
     * 委托类
     */
    class StaticHelloService : IStaticHelloService {
        override fun getStaticHello(content: String): String {
            return "hello $content"
        }
    }

    /**
     * 代理类
     */
    class StaticProxyStaticHello : IStaticHelloService {
        private var helloService = StaticHelloService()

        override fun getStaticHello(content: String): String {
            beforeProcess()
            val result = helloService.getStaticHello(content)
            println("koo----- $result")
            afterProcess()
            return result
        }

        private fun beforeProcess() {
            println("koo----- 我是委托类方法处理之前要执行的方法。。。。");
        }

        private fun afterProcess() {
            println("koo----- 我是委托类处理之后执行的方法。。。。");
        }
    }


    /**
     * 动态代理
     * 1.代理类在程序运行前不存在、运行时由程序动态生成的代理方式称为动态代理
     * 2.优点：动态代理与静态代理相比较，最大的好处是接口中声明的所有方法都被转移到调用处理器一个集中的方法中处理
     *  （InvocationHandler.invoke）。这样，在接口方法数量比较多的时候，我们可以进行灵活处理，
     *   而不需要像静态代理那样每一个方法进行中转。而且动态代理的应用使我们的类职责更加单一，复用性更强
     */

    /**
     * 接口
     */
    interface IDynamicHelloService {
        fun getDynamicContent(content: String): String
    }

    /**
     * 接口2
     */
    interface IDynamicHelloService2 {
        fun getDynamicContent2(content: String): String
    }

    /**
     * 委托类
     */
    class DynamicHelloService : IDynamicHelloService, IDynamicHelloService2 {
        override fun getDynamicContent(content: String): String {
            return "hello $content"
        }

        /**
         * 动态代理可以方便新增多个类而不用修改Proxy类
         */
        override fun getDynamicContent2(content: String): String {
            return "hello2 $content"
        }
    }

    class HelloProxyInvocationHandler : InvocationHandler {
        private var obj: Any? = null

        constructor(obj: Any?) {
            this.obj = obj
        }

        override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
            beforeProcess()
            val result = method?.invoke(obj, *args.orEmpty())
            println("koo----- $result")
            afterProcess()
            return result
        }

        private fun beforeProcess() {
            println("koo----- 我是委托类方法处理之前要执行的方法。。。。");
        }

        private fun afterProcess() {
            println("koo----- 我是委托类处理之后执行的方法。。。。");
        }
    }
}