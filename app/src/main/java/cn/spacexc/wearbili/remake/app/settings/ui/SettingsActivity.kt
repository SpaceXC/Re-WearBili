package cn.spacexc.wearbili.remake.app.settings.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.material.icons.outlined.Handyman
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material.icons.outlined.VideoLabel
import androidx.compose.material.icons.outlined.VideoSettings
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.lifecycleScope
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.remake.app.login.qrcode.web.ui.QrCodeLoginActivity
import cn.spacexc.wearbili.remake.app.settings.SettingsManager
import cn.spacexc.wearbili.remake.app.settings.domain.SettingsItem
import cn.spacexc.wearbili.remake.app.settings.experimantal.ExperimentalFunctionsActivity
import cn.spacexc.wearbili.remake.app.settings.scaling.ScaleAdjustingActivity
import cn.spacexc.wearbili.remake.app.settings.toolbar.ui.QuickToolbarCustomizationActivity
import cn.spacexc.wearbili.remake.app.splash.ui.SplashScreenActivity
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.proto.settings.VideoDecoder
import cn.spacexc.wearbili.remake.proto.settings.VideoDisplaySurface
import cn.spacexc.wearbili.remake.proto.settings.copy
import kotlinx.coroutines.launch


class SettingsActivity : ComponentActivity() {
    private val settingsItems = listOf(
        SettingsItem(
            name = "快捷工具栏",
            description = "设置首页快捷方式",
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Handyman,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            },
            action = {
                startActivity(Intent(this, QuickToolbarCustomizationActivity::class.java))
            }
        ),
        SettingsItem(
            name = "缩放调整",
            description = "调整控件大小",
            icon = {
                Icon(
                    imageVector = Icons.Outlined.FormatSize,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            },
            action = {
                startActivity(Intent(this, ScaleAdjustingActivity::class.java))
            }
        ),
        SettingsItem(
            name = "动画",
            description = "动画效果开关",
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Animation,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            },
            action = {
                lifecycleScope.launch {
                    SettingsManager.updateConfiguration {
                        copy {
                            hasAnimation = !hasAnimation
                        }
                    }
                }
            },
            isOn = { SettingsManager.getConfiguration().hasAnimation }
        ),
        SettingsItem(
            name = "低性能播放器",
            description = "限制视频帧率及码率",
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Animation,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            },
            action = {
                lifecycleScope.launch {
                    SettingsManager.updateConfiguration {
                        copy {
                            isVideoLowPerformance = !isVideoLowPerformance
                        }
                    }
                }
            },
            isOn = { SettingsManager.getConfiguration().isVideoLowPerformance }
        ),
        SettingsItem(
            name = "视频解码器",
            description = "软/硬件解码",
            icon = {
                Icon(
                    imageVector = Icons.Outlined.VideoSettings,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            },
            action = {
                lifecycleScope.launch {
                    if (it != null) {
                        SettingsManager.updateConfiguration {
                            copy {
                                videoDecoder = it.option as VideoDecoder
                            }
                        }
                    }
                }
            },
            currentValue = {
                SettingsManager.getConfiguration().videoDecoder
            },
            displayedValue = {
                when (SettingsManager.getConfiguration().videoDecoder) {
                    null -> ""
                    VideoDecoder.Hardware -> "硬解码"
                    VideoDecoder.Software -> "软解码"
                    else -> ""
                }
            },
            options = listOf(
                SettingsItem.Option(
                    VideoDecoder.Software, "软解码"
                ),
                SettingsItem.Option(
                    VideoDecoder.Hardware, "硬解码"
                )
            )
        ),
        SettingsItem(
            name = "视频显示模式",
            description = "无法正常播放时，尝试更改此选项",
            icon = {
                Icon(
                    imageVector = Icons.Outlined.VideoLabel,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            },
            action = {
                lifecycleScope.launch {
                    if (it != null) {
                        SettingsManager.updateConfiguration {
                            copy {
                                videoDisplaySurface = it.option as VideoDisplaySurface
                            }
                        }
                    }
                }
            },
            currentValue = {
                SettingsManager.getConfiguration().videoDisplaySurface
            },
            displayedValue = {
                when (SettingsManager.getConfiguration().videoDisplaySurface) {
                    null -> ""
                    VideoDisplaySurface.SurfaceView -> "SurfaceView"
                    VideoDisplaySurface.TextureView -> "TextureView"
                    else -> ""
                }
            },
            options = listOf(
                SettingsItem.Option(
                    VideoDisplaySurface.SurfaceView, "SurfaceView"
                ),
                SettingsItem.Option(
                    VideoDisplaySurface.TextureView, "TextureView"
                )
            )
        ),
        SettingsItem(
            name = "添加账号",
            description = "添加一个账号",
            icon = {
                Icon(
                    imageVector = Icons.Outlined.PeopleOutline,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            },
            action = {
                startActivity(Intent(this, QrCodeLoginActivity::class.java))
            }
        ),
        SettingsItem(
            name = "实验性功能",
            description = "开启正在试验的功能",
            icon = {
                Icon(
                    painter = painterResource(id = cn.spacexc.wearbili.remake.R.drawable.icon_experimental_function),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            },
            action = {
                startActivity(Intent(this, ExperimentalFunctionsActivity::class.java))
            }
        ),
        SettingsItem(
            name = "登出",
            description = "登出当前的账号",
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            },
            action = {
                logout()
            }
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