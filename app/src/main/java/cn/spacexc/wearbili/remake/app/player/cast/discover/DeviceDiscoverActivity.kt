package cn.spacexc.wearbili.remake.app.player.cast.discover

import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.content.getSystemService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

const val PARAM_DLNA_VIDEO_NAME = "videoName"

class DeviceDiscoverActivity : ComponentActivity() {
    val viewModel by viewModels<DeviceDiscoverViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val wifiName = getWifiName()
        val isWifiConnected = wifiName != null
        if (isWifiConnected) {
            lifecycleScope.launch {
                viewModel.discoverDevices()
            }
        }
        setContent {
            DeviceDiscoverScreen(viewModel = viewModel, wifiName = wifiName)
        }
    }

    private fun getWifiName(): String? {
        //获取wifi名字
        val wifiManager = getSystemService<WifiManager>()
        return wifiManager?.connectionInfo?.ssid
    }
}