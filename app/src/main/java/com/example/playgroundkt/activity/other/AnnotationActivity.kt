package com.example.playgroundkt.activity.other

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.FloatRange
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity
import java.lang.annotation.Inherited

/**
 * 元注解
 *     1.@Retention：注解保留的生命周期
 *     2.@Target：注解对象的作用范围
 *     3.@Inherited：@Inherited标明所修饰的注解，在所作用的类上，是否可以被继承
 *     4.@Documented：javadoc的工具文档化，一般不关心
 *     5.@Repeatable：使得作用的注解可以取多个值
 */

/**
 * @Retention  说标明了注解被生命周期，对应RetentionPolicy的枚举，表示注解在何时生效
 * SOURCE：只在源码中有效，编译时抛弃，如 @Override
 * CLASS：编译class文件时生效
 * RUNTIME：运行时才生效
 *
 * 生命周期长度 SOURCE < CLASS < RUNTIME ，所以前者能作用的地方后者一定也能作用。
 * 一般如果需要在运行时去动态获取注解信息，那只能用 RUNTIME 注解，比如 @Deprecated 使用 RUNTIME 注解
 * 如果要在编译时进行一些预处理操作，比如生成一些辅助代码（如 ButterKnife），就用 CLASS 注解；
 * 如果只是做一些检查性的操作，比如 @Override 和 @SuppressWarnings，使用 SOURCE 注解
 */

/**
 * @Target 标明了注解的适用范围，对应ElementType枚举，明确了注解的有效范围
 * TYPE：类、接口、枚举、注解类型
 * FIELD：类成员（构造方法、方法、成员变量）
 * METHOD/FUNCTION：方法
 * PARAMETER：参数
 * CONSTRUCTOR：构造器
 * LOCAL_VARIABLE：局部变量
 * ANNOTATION_TYPE：注解
 * PACKAGE：包声明
 * TYPE_PARAMETER：类型参数
 * TYPE_USE：类型使用声明
 */

/**
 * @Inherited 注解所作用的类，在继承时默认无法继承父类的注解。除非注解声明了 @Inherited。同时Inherited声明出来的注，只对类有效，对方法／属性无效
 */


/**
 * 常用资源类型注解
 * AnimRes	         动画
 * AnimatorRes	     animator资源类型
 * AnyRes	         任何资源类型
 * ArrayRes	         数组资源类型
 * AttrRes	         属性资源类型
 * BoolRes	         bool类型资源类型
 * ColorRes	         颜色资源类型
 * DimenRes	         长度资源类型
 * DrawableRes	     图片资源类型
 * IdRes	         资源id
 * InterpolatorRes	 动画插值器
 * LayoutRes	     layout资源
 * MenuRes	         menu资源
 * RawRes	         raw资源
 * StringRes         字符串资源
 * StyleRes	         style资源
 * StyleableRes	     Styleable资源类型
 * TransitionRes	 transition资源类型
 * XmlRes	         xml资源
 */

/**
 * Value Constraints注解
 * @size:       fun test(@Size(min = 1,max = 2)String s){}
 * @IntRange:   fun test(@IntRange(from = 1,to = 100)int s)
 * @FloatRange: fun test(@FloatRange(from = 1.0,to = 100.0)float s)
 */

@Route(path = RouterPath.AnnotationActivity)
class AnnotationActivity : BaseEntranceActivity() {
    fun test(@FloatRange(from = 1.0,to = 999999999999323985209384902349999.0) position:Double){

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addButton("获取注解") {
            val test = Test()
            test.test()
            test.test2()
        }

        addButton("获取继承的注解") {
            val test = Test2()
            test.getParentAnnotation()
        }
    }

    @Inherited // 该注解可以被继承。仅针对类，成员属性、方法并不受此注释的影响
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TestClassAnnotation()

    @MustBeDocumented // 可通过javadoc此类的工具进行文档化
    @Target(AnnotationTarget.FUNCTION) // 限制只能使用在方法上
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TestAnnotation(
        @Deprecated("过时测试")
        val value: String,
        val name: String
    )


    @TestClassAnnotation
    open class Test {
        @TestAnnotation("100", "koo")
        @CallSuper  // 子类重写该方法后必须要有实现
        open fun test() {
            val methods = javaClass.declaredMethods
            for (method in methods) {
                if (method.isAnnotationPresent(TestAnnotation::class.java)) {
                    val ety = method.getAnnotation(TestAnnotation::class.java)
                    println("koo----- ${ety.name}   ${ety.value}   ")
                }
            }
        }

        @TestAnnotation("101", "koo")
        fun test2() {
            val methods = javaClass.declaredMethods
            for (method in methods) {
                if (method.isAnnotationPresent(TestAnnotation::class.java)) {
                    val ety = method.getAnnotation(TestAnnotation::class.java)
                    println("koo----- ${ety.name}   ${ety.value}   ")
                }
            }
        }
    }

    class Test2 : Test() {
        fun getParentAnnotation() {
            val ann = javaClass
            if (ann.isAnnotationPresent(TestClassAnnotation::class.java)) {
                println("koo----- 继承 TestClassAnnotation")
            } else {
                println("koo----- 未继承 TestClassAnnotation")
            }
        }
    }
}