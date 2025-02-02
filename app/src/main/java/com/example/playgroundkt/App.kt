package com.example.playgroundkt

import com.alibaba.android.arouter.launcher.ARouter
import com.example.jetpackmvvm.base.BaseApp
import com.example.jetpackmvvm.ext.util.jetpackMvvmLog
import com.tencent.mmkv.MMKV

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　:
 */

class App : BaseApp() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MMKV.initialize(this.filesDir.absolutePath + "/mmkv")
        //初始化Bugly
        val context = applicationContext
        // 获取当前包名
        val packageName = context.packageName
        jetpackMvvmLog = BuildConfig.DEBUG

        //防止项目崩溃，崩溃后打开错误界面
//        CaocConfig.Builder.create()
//            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
//            .enabled(true)//是否启用CustomActivityOnCrash崩溃拦截机制 必须启用！不然集成这个库干啥？？？
//            .showErrorDetails(false) //是否必须显示包含错误详细信息的按钮 default: true
//            .showRestartButton(false) //是否必须显示“重新启动应用程序”按钮或“关闭应用程序”按钮default: true
//            .logErrorOnRestart(false) //是否必须重新堆栈堆栈跟踪 default: true
//            .trackActivities(true) //是否必须跟踪用户访问的活动及其生命周期调用 default: false
//            .minTimeBetweenCrashesMs(2000) //应用程序崩溃之间必须经过的时间 default: 3000
//            .restartActivity(WelcomeActivity::class.java) // 重启的activity
//            .errorActivity(ErrorActivity::class.java) //发生错误跳转的activity
//            .apply()

//        if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
//        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }

}
