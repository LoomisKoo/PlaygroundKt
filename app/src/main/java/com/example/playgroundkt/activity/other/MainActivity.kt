package com.example.playgroundkt.activity.other

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jetpackmvvm.network.manager.NetState
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.data.HomePageData
import com.example.playgroundkt.R
import com.example.playgroundkt.adapter.HomePageAdapter
import com.example.playgroundkt.base.BaseActivity
import com.example.playgroundkt.databinding.ActivityMainBinding
import com.example.playgroundkt.viewmodel.state.MainViewModel
import me.hgj.jetpackmvvm.demo.app.util.StatusBarUtil
import kotlin.collections.ArrayList
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override fun layoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })

        appViewModel.appColor.value?.let {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
            StatusBarUtil.setColor(this, it, 0)
        }
    }

    override fun createObserver() {
        appViewModel.appColor.observeInActivity(this, Observer {
            supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
            StatusBarUtil.setColor(this, it, 0)
        })
    }

    /**
     * 示例，在Activity/Fragment中如果想监听网络变化，可重写onNetworkStateChanged该方法
     */
    override fun onNetworkStateChanged(netState: NetState) {
        super.onNetworkStateChanged(netState)
        if (netState.isSuccess) {
            Toast.makeText(applicationContext, "有网了!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "断网了!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView()
    }


    private fun initRecyclerView() {
        val entranceList = ArrayList<HomePageData>()
        entranceList.add(HomePageData("OutlineProvider", RouterPath.OutlineProviderActivity))
        entranceList.add(HomePageData("behavior", RouterPath.BehaviorListActivity))
        entranceList.add(HomePageData("animationList", RouterPath.AnimationListActivity))
        entranceList.add(HomePageData("hook", RouterPath.HookActivity))
        entranceList.add(HomePageData("ISP", RouterPath.IspActivity))
        entranceList.add(HomePageData("AOPT", RouterPath.POETActivity))
        entranceList.add(HomePageData("注解", RouterPath.AnnotationActivity))
        entranceList.add(HomePageData("代理", RouterPath.ProxyActivity))
        entranceList.add(HomePageData("反射", RouterPath.ReflectActivity))
        entranceList.add(HomePageData("Hanlder", RouterPath.HandlerActivity))
        entranceList.add(HomePageData("列表显示大图", RouterPath.RvBigImgActivity))
        entranceList.add(HomePageData("ThreadLocal", RouterPath.ThreadLocalActivity))
        entranceList.add(HomePageData("浮窗功能", RouterPath.FloatWindowsActivity))
        entranceList.add(HomePageData("kotlin", RouterPath.KotlinActivity))
        entranceList.add(HomePageData("SP ANR", RouterPath.SharePreferenceANRActivity))
        entranceList.add(HomePageData("Results API", RouterPath.ResultFirstActivity))
        entranceList.add(HomePageData("Shortcuts", RouterPath.ShortcutsActivity))
        entranceList.add(HomePageData("图片缩放", RouterPath.ZoomImageViewActivity))
        entranceList.add(HomePageData("滑动冲突", RouterPath.ScrollConflictActivity))
        entranceList.add(HomePageData("事件分发", RouterPath.EventDispatchActivity))

        val adapter = HomePageAdapter()
        adapter.setNewInstance(entranceList)
        mDatabind.recyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this)
        }
    }

}
