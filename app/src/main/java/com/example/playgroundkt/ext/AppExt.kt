package com.example.playgroundkt.ext

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * @param message 显示对话框的内容 必填项
 * @param title 显示对话框的标题 默认 温馨提示
 * @param positiveButtonText 确定按钮文字 默认确定
 * @param positiveAction 点击确定按钮触发的方法 默认空方法
 * @param negativeButtonText 取消按钮文字 默认空 不为空时显示该按钮
 * @param negativeAction 点击取消按钮触发的方法 默认空方法
 *
 */
fun AppCompatActivity.showMessage(
    message: String,
    title: String = "温馨提示",
    positiveButtonText: String = "确定",
    positiveAction: () -> Unit = {},
    negativeButtonText: String = "",
    negativeAction: () -> Unit = {}
) {
    // todo koo 改为odialog
//    MaterialDialog(this)
//        .cancelable(true)
//        .lifecycleOwner(this)
//        .show {
//            title(text = title)
//            message(text = message)
//            positiveButton(text = positiveButtonText) {
//                positiveAction.invoke()
//            }
//            if (negativeButtonText.isNotEmpty()) {
//                negativeButton(text = negativeButtonText) {
//                    negativeAction.invoke()
//                }
//            }
//            getActionButton(WhichButton.POSITIVE).updateTextColor(SettingUtil.getColor(this@showMessage))
//            getActionButton(WhichButton.NEGATIVE).updateTextColor(SettingUtil.getColor(this@showMessage))
//        }
}

/**
 * @param message 显示对话框的内容 必填项
 * @param title 显示对话框的标题 默认 温馨提示
 * @param positiveButtonText 确定按钮文字 默认确定
 * @param positiveAction 点击确定按钮触发的方法 默认空方法
 * @param negativeButtonText 取消按钮文字 默认空 不为空时显示该按钮
 * @param negativeAction 点击取消按钮触发的方法 默认空方法
 */
fun Fragment.showMessage(
    message: String,
    title: String = "温馨提示",
    positiveButtonText: String = "确定",
    positiveAction: () -> Unit = {},
    negativeButtonText: String = "",
    negativeAction: () -> Unit = {}
) {
    activity?.let {
        // todo koo 改为odialog
//        MaterialDialog(it)
//            .cancelable(true)
//            .lifecycleOwner(viewLifecycleOwner)
//            .show {
//                title(text = title)
//                message(text = message)
//                positiveButton(text = positiveButtonText) {
//                    positiveAction.invoke()
//                }
//                if (negativeButtonText.isNotEmpty()) {
//                    negativeButton(text = negativeButtonText) {
//                        negativeAction.invoke()
//                    }
//                }
//                getActionButton(WhichButton.POSITIVE).updateTextColor(SettingUtil.getColor(it))
//                getActionButton(WhichButton.NEGATIVE).updateTextColor(SettingUtil.getColor(it))
//            }
    }
}

/**
 * 获取进程号对应的进程名
 *
 * @param pid 进程号
 * @return 进程名
 */
fun getProcessName(pid: Int): String? {
    var reader: BufferedReader? = null
    try {
        reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
        var processName = reader.readLine()
        if (!TextUtils.isEmpty(processName)) {
            processName = processName.trim { it <= ' ' }
        }
        return processName
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
    } finally {
        try {
            reader?.close()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }

    }
    return null
}

/**
 * 加入qq聊天群
 */
fun Fragment.joinQQGroup(key: String): Boolean {
    val intent = Intent()
    intent.data =
        Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
    // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    return try {
        startActivity(intent)
        true
    } catch (e: Exception) {
        // 未安装手Q或安装的版本不支持
        ToastUtils.showShort("未安装手机QQ或安装的版本不支持")
        false
    }
}

fun List<*>?.isNull(): Boolean {
    return this?.isEmpty() ?: true
}

fun List<*>?.isNotNull(): Boolean {
    return this != null && this.isNotEmpty()
}

/**
 * 根据索引获取集合的child值
 * @receiver List<T>?
 * @param position Int
 * @return T?
 */
inline fun <reified T> List<T>?.getChild(position: Int): T? {
    //如果List为null 返回null
    return if (this == null) {
        null
    } else {
        //如果position大于集合的size 返回null
        if (position + 1 > this.size) {
            null
        } else {
            //返回正常数据
            this[position]
        }
    }
}

/**
 * 隐藏软键盘
 */
fun hideSoftKeyboard(activity: Activity?) {
    activity?.let { act ->
        val view = act.currentFocus
        view?.let {
            val inputMethodManager =
                act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}

