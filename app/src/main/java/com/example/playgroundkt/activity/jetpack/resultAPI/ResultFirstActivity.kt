package com.example.playgroundkt.activity.jetpack.resultAPI

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.BuildConfig
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity
import org.jetbrains.anko.toast
import java.io.File


/**
 * result API
 */
@Route(path = RouterPath.ResultFirstActivity)
class ResultFirstActivity : BaseEntranceActivity() {
    // 因为ComponentActivity和Fragment已经实现了LifecycleObserver
    // LifecycleOwner 会在 Lifecycle 被销毁时自动移除已注册的启动器，所以不用手动摧毁
    // 可以使用多个launcher来处理不同业务，解耦
    private val mActivityLauncher = registerForActivityResult(TestResultContract()) {
        toast("传回来的数据：$it")
        println("传回来的数据：$it")
    }

    private val mBluetoothPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                Log.e("TAG", "bluetooth permnission granted")
            } else {
                Log.e("TAG", "bluetooth permnission denied")
            }
        }

    private val mTakePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            println("take photo bitmap:$it")
        }

    private val mTakeVideoLauncher =
        registerForActivityResult(ActivityResultContracts.TakeVideo()) {
            println("take video bitmap:$it")
        }

    private val mTakePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                println("TakePicture success")
            } else {
                println("TakePicture failed")
            }
        }

    private val mPickContactLauncher =
        registerForActivityResult(ActivityResultContracts.PickContact()) {
            println("mPickContactLauncher $it")
        }

    private var observer: LifecycleResultObserver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observer = LifecycleResultObserver(this.activityResultRegistry).let {
            lifecycle.addObserver(it)
            it
        }

        addButton("launch方式跳转页面") {
            mActivityLauncher.launch("test")
        }

        addButton("observer方式选择image") {
            observer?.let {
                // Open the activity to select an image
                it.selectImage()
            }
        }

        addButton("申请蓝牙权限") {
            mBluetoothPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH)
        }

        addButton("获取联系人Uri") {
            mPickContactLauncher.launch(null)
        }

        addButton("拍照") {
            mTakePhotoLauncher.launch(null)
        }

        addButton("拍视频") {
            //需要WRITE_EXTERNAL_STORAGE权限
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues()
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, "图片名称.mp4")
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                FileProvider.getUriForFile(
                    this, BuildConfig.BUILD_TYPE,
                    File(externalCacheDir!!.absolutePath + "图片名称.mp4")
                )
            }
            mTakeVideoLauncher.launch(uri)
        }

        addButton("拍照并储存") {
            //需要WRITE_EXTERNAL_STORAGE权限
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues()
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, "图片名称.jpg")
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                FileProvider.getUriForFile(
                    this, BuildConfig.BUILD_TYPE,
                    File(externalCacheDir!!.absolutePath + "图片名称.jpg")
                )
            }
            mTakePictureLauncher.launch(uri)
        }
    }

    /**
     * 除了ActivityResultContract 还有一些如下：
     *  StartActivityForResult()
     *  RequestMultiplePermissions()  用于请求一组权限
     *  RequestPermission()           用于请求单个权限
     *  TakePicturePreview()          拍照，返回值为Bitmap图片
     *  TakePicture()                 拍照，并将图片保存到给定的Uri地址，返回true表示保存成功。
     *  TakeVideo()                   拍摄视频，保存到给定的Uri地址，返回一张缩略图。
     *  PickContact()                 从通讯录APP获取联系人
     *  CreateDocument()              提示用户选择一个文档，返回一个(file:/http:/content:)开头的Uri。
     *  OpenDocumentTree()            提示用户选择一个目录，并返回用户选择的作为一个Uri返回，应用程序可以完全管理返回目录中的文档。
     *  OpenMultipleDocuments()       提示用户选择文档（可以选择多个），分别返回它们的Uri，以List的形式。
     *  OpenDocument()
     *  GetMultipleContents()
     *  GetContent()
     */
    class TestResultContract : ActivityResultContract<String, String>() {
        override fun createIntent(context: Context, input: String?): Intent {
            return Intent(context, ResultSecondActivity::class.java).apply {
                putExtra("testString", input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String? {
            val data = intent?.getStringExtra("result")
            return if (resultCode == Activity.RESULT_OK && data != null) data else null
        }
    }
}