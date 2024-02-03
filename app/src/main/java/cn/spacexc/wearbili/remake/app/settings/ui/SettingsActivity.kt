package cn.spacexc.wearbili.remake.app.settings.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.remake.app.settings.domain.SettingsItem
import cn.spacexc.wearbili.remake.app.settings.toolbar.ui.QuickToolbarCustomizationActivity
import cn.spacexc.wearbili.remake.app.splash.ui.SplashScreenActivity
import cn.spacexc.wearbili.remake.common.ToastUtils
import kotlinx.coroutines.launch


class SettingsActivity : ComponentActivity() {
    private val settingsItems = listOf(
        SettingsItem(
            name = "快捷工具栏",
            description = "设置首页快捷方式",
            icon = {

            },
            action = {
                startActivity(Intent(this, QuickToolbarCustomizationActivity::class.java))
            }
        ),
        SettingsItem(
            name = "登出",
            description = "登出当前的账号",
            icon = {
                Icon(
                    imageVector = Icons.Outlined.ExitToApp,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            },
            action = ::logout
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingsActivityScreen(items = settingsItems)
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            ToastUtils.showText("正在登出...")
            val isSuccess = UserUtils.logout()
            if (isSuccess) {
                ToastUtils.showText("登出成功")
                startActivity(Intent(this@SettingsActivity, SplashScreenActivity::class.java))
                finish()
            } else {
                ToastUtils.showText("登出失败")
            }
        }
    }
}