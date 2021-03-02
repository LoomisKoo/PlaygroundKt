package com.example.playgroundkt.activity.jetpack.resultAPI

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

/**
 *
 * @Description: 在非activity/fragment种可以这样使用ResultAPI
 * @Author: chunxiong.gu
 * @CreateDate: 2021/3/2 14:37
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/2 14:37
 * @UpdateRemark: 更新说明
 */

class LifecycleResultObserver(private val registry: ActivityResultRegistry) :
    DefaultLifecycleObserver {
     lateinit var getContent: ActivityResultLauncher<String>

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        getContent = registry.register("test_key", owner,
            ActivityResultContracts.GetContent()){
        }
    }

    fun selectImage() {
        getContent.launch("image/*")
    }
}