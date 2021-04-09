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