package com.example.playgroundkt.activity.other

import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.aptnote.butterknife.BindFieldView
import com.example.playgroundkt.R
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.util.BindFieldViewUtil

/**
 * @see ../doc/javaPoet.md
 * APT(Annotation Processing Tool)即注解处理器，是一种处理注解的工具，确切的说它是javac的一个工具，它用来在编译时扫描和处理注解。注解处理器以Java代码(或者编译过的字节码)作为输入，生成.java文件作为输出
 * JavaPoet是square推出的开源java代码生成框架，提供Java Api生成.java源文件。这个框架功能非常有用，我们可以很方便的使用它根据注解、数据库模式、协议格式等来对应生成代码。通过这种自动化生成代码的方式，可以让我们用更加简洁优雅的方式要替代繁琐冗杂的重复工作
 * JavaPoet 用于动态生成java文件（简单仿照 butterknife）
 * JavaPoet 是一款可以自动生成Java文件的第三方依赖
 * 简洁易懂的API，上手快
 * 让繁杂、重复的Java文件，自动化生成，提高工作效率，简化流程
 */
@Route(path = RouterPath.POETActivity)
class APTActivity : AppCompatActivity() {

    @JvmField
    @BindFieldView(id = R.id.text_view)
    var textView: TextView? = null

    /**
     *  {@link ImageView.ScaleType}
     * {@link onCreate()}
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apt)
        BindFieldViewUtil.getInstance().bind(this)
        textView?.text = "8888888"
    }
}