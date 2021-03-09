package com.example.playgroundkt

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.activity.BaseEntranceActivity

/**
 * 这里展示的是动态添加，静态添加方式参考xml文件夹下的shoertcuts.xml，并在androidManifest.xml的启动activity声明
 * 静态方式声明，仅会在应用抽屉种生成新的启动图标（基于三星OneUI3.0测试）
 * 动态方式声明，仅会在长按应用图标时显示相关图标（基于三星OneUI3.0测试）
 */
@Route(path = RouterPath.ShortcutsActivity)
class ShortcutsActivity : BaseEntranceActivity() {
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addButton("动态添加快捷方式") {
            addShortcuts()
        }

        addButton("移除所有快捷方式") {
            removeShortcuts()
        }

        addButton("移除指定id的快捷方式") {
            removeShortcutsByIds()
        }

        addButton("更新指定id的快捷方式") {
            updateShortcutsByIds()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun addShortcuts() {
        val shortcutManager = (getSystemService(ShortcutManager::class.java)) as ShortcutManager;

        val shortcut = getShortcutsInfo("testttttttttt1", "id1")
        val shortcut2 = getShortcutsInfo("testttttttttt2", "id2")

        shortcutManager.dynamicShortcuts = listOf(shortcut, shortcut2);
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun removeShortcuts() {
        val shortcutManager = (getSystemService(ShortcutManager::class.java)) as ShortcutManager;
        shortcutManager.removeAllDynamicShortcuts()
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun removeShortcutsByIds() {
        val shortcutManager = (getSystemService(ShortcutManager::class.java)) as ShortcutManager;
        shortcutManager.removeDynamicShortcuts(listOf("id1"))
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun updateShortcutsByIds() {
        val shortcutManager = (getSystemService(ShortcutManager::class.java)) as ShortcutManager;
        shortcutManager.updateShortcuts(listOf(getShortcutsInfo("test","id1")))
    }


    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun getShortcutsInfo(setLongLabel: String, id: String): ShortcutInfo {
        val shortcut = ShortcutInfo.Builder(this, id)  // 快捷方式的ID
            .setShortLabel("Website")    // 快捷方式短名称
            .setLongLabel(setLongLabel)    // 快捷方式长名称(优先显示，长度太短则显示短的)
            .setIcon(Icon.createWithResource(this, R.drawable.ic_launcher_background))    //快捷方式图标
            .setIntent(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.mysite.example.com/")
                )
            )    //快捷方式绑定的操作
            .build();
        return shortcut
    }
}