package cn.spacexc.wearbili.remake.app.player.videoplayer.mirroring.dlna

import android.Manifest
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PARAM_IS_BANGUMI
import cn.spacexc.wearbili.remake.app.player.videoplayer.mirroring.dlna.controller.ui.DlnaControllerActivity
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_CID
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.spx
import com.android.cast.dlna.dmc.DLNACastManager
import org.fourthline.cling.model.meta.Device


/**
 * Created by XC-Qan on 2023/7/16.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val PARAM_DLNA_VIDEO_NAME = "videoName"

class DlnaDeviceDiscoverActivity : ComponentActivity() {
    var devices by mutableStateOf(emptyList<Device<*, *, *>>())

    private val listener = object : com.android.cast.dlna.dmc.OnDeviceRegistryListener {
        override fun onDeviceAdded(device: Device<*, *, *>) {
            device.logd("newDevice")
            val temp = devices.toMutableList()
            temp.add(device)
            devices = temp
        }

        override fun onDeviceUpdated(device: Device<*, *, *>) {
            device.logd("update")
            val deviceInList =
                devices.indexOfFirst { it.details.serialNumber == device.details.serialNumber }
            if (deviceInList != -1) {
                val temp = devices.toMutableList()
                temp[deviceInList] = device
                devices = temp
            }
        }

        override fun onDeviceRemoved(device: Device<*, *, *>) {
            device.logd("remove")
            val deviceInList =
                devices.indexOfFirst { it.details.serialNumber == device.details.serialNumber }
            if (deviceInList != -1) {
                val temp = devices.toMutableList()
                temp.removeAt(deviceInList)
                devices = temp
            }
        }
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
            if (granted.all { it.value }) {
                DLNACastManager.getInstance().search(null, 60)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoName = intent.getStringExtra(PARAM_DLNA_VIDEO_NAME)
        val videoCid = intent.getLongExtra(PARAM_VIDEO_CID, 0L)
        val isBangumi = intent.getBooleanExtra(PARAM_IS_BANGUMI, false)
        videoCid.logd("videoCid")
        DLNACastManager.getInstance().bindCastService(this)
        DLNACastManager.getInstance().registerDeviceListener(listener)
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
        val wifiName = getWifiName()?.replace("\"", "")
        setContent {
            TitleBackground(title = "", onBack = ::finish) {
                if (wifiName != null) {
                    LaunchedEffect(key1 = Unit, block = {
                        DLNACastManager.getInstance().search(null, 60)
                    })
                    LazyColumn(contentPadding = PaddingValues(8.dp)) {
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "选择设备",
                                    fontSize = 22.spx,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                IconText(
                                    text = wifiName,
                                    color = Color.White,
                                    modifier = Modifier.alpha(0.8f),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.spx
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Wifi,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                                Text(
                                    text = "选择与您手表网络相同的设备并开始投射视频内容",
                                    color = Color.White,
                                    fontSize = 12.spx,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.alpha(0.7f)
                                )
                                IconText(text = "轻点以搜索设备",
                                    color = BilibiliPink,
                                    modifier = Modifier
                                        .alpha(0.8f)
                                        .clickVfx {
                                            DLNACastManager
                                                .getInstance()
                                                .search(null, 60)
                                        },
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.spx
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        tint = BilibiliPink
                                    )
                                }
                            }

                        }
                        devices.forEach { device ->
                            item {
                                Card(modifier = Modifier.fillMaxWidth(), onClick = {
                                    //val url = "https://upos-sz-mirror08c.bilivideo.com/upgcxcode/48/44/175354448/175354448-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&uipk=5&nbs=1&deadline=1689599188&gen=playurlv2&os=08cbv&oi=1974809411&trid=61fec01e28894f39ace33cb917907cd9h&mid=480816699&platform=html5&upsig=a6110ee9c9747a2695c70a59aad8b096&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&bw=57610&logo=80000000"
                                    /*DLNACastManager.getInstance().cast(
                                        device,
                                        CastObject.newInstance(
                                            url!!,
                                            UUID.randomUUID().toString(),
                                            "Test Sample"
                                        )
                                    )
                                    DLNACastManager.getInstance().play()*/
                                    WearbiliCastManager.connectTo(device)
                                    startActivity(
                                        Intent(
                                            this@DlnaDeviceDiscoverActivity,
                                            DlnaControllerActivity::class.java
                                        ).apply {
                                            putExtra(PARAM_DLNA_VIDEO_NAME, videoName)
                                            putExtra(PARAM_VIDEO_CID, videoCid)
                                            putExtra(PARAM_IS_BANGUMI, isBangumi)
                                        })
                                }) {
                                    IconText(
                                        text = device.details.friendlyName,
                                        color = Color.White,
                                        fontSize = 14.spx,
                                        fontWeight = FontWeight.Medium
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Tv,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        DLNACastManager.getInstance().search(null, 60)

    }

    private fun getWifiName(): String? {
        //获取wifi名字
        val wifiManager = getSystemService<WifiManager>()
        return wifiManager?.connectionInfo?.ssid
    }

    override fun onDestroy() {
        super.onDestroy()
        DLNACastManager.getInstance().unbindCastService(this)
        DLNACastManager.getInstance().unregisterListener(listener)
    }
}