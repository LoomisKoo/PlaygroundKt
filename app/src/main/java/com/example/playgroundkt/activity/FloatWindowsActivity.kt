package com.example.playgroundkt.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Route(path = RouterPath.FloatWindowsActivity)
class FloatWindowsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_float_windows)
        showFloatingWindow()
    }

    private fun requestWindowsPermission() {
        //跳转至开启悬浮窗权限
        startActivityForResult(
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            ), 0
        );
    }


    /**
     * 显示悬浮按钮
     */
    private fun showFloatingWindow() {
        if (Settings.canDrawOverlays(this)) {
            // 获取WindowManager服务
            val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager;

            // 设置LayoutParam
            val layoutParams = WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }

            layoutParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL xor WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            layoutParams.format = PixelFormat.RGBA_8888;
            layoutParams.width = 500;
            layoutParams.height = 100;
            layoutParams.x = 700;
            layoutParams.y = 400;
            // 将悬浮窗控件添加到WindowManager
            val btFloat = Button(this);
            btFloat.text = "Floating Window";
            btFloat.setBackgroundColor(Color.BLUE);

            // 加入浮窗
            windowManager.addView(btFloat, layoutParams);

            // 移除浮窗
            lifecycleScope.launch {
                delay(3000)
                windowManager.removeView(btFloat)
            }

        }else{
            requestWindowsPermission()
        }
    }
}