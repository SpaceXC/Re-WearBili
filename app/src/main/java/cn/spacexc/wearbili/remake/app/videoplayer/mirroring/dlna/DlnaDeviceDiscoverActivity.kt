package cn.spacexc.wearbili.remake.app.videoplayer.mirroring.dlna

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import com.android.cast.dlna.dmc.DLNACastManager
import org.fourthline.cling.model.meta.Device
import java.util.UUID


/**
 * Created by XC-Qan on 2023/7/16.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

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
        val url = intent.getStringExtra("url")
        DLNACastManager.getInstance().bindCastService(this)
        DLNACastManager.getInstance().registerDeviceListener(listener)
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
        setContent {
            TitleBackground(title = "选择设备") {

                LazyColumn {
                    item {
                        Card(onClick = {
                            DLNACastManager.getInstance().search(null, 60)
                        }) {
                            Text(text = "scan")
                        }
                    }
                    devices.forEach { device ->
                        item {
                            Card(modifier = Modifier.fillMaxWidth(), onClick = {
                                //val url = "https://upos-sz-mirror08c.bilivideo.com/upgcxcode/48/44/175354448/175354448-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&uipk=5&nbs=1&deadline=1689599188&gen=playurlv2&os=08cbv&oi=1974809411&trid=61fec01e28894f39ace33cb917907cd9h&mid=480816699&platform=html5&upsig=a6110ee9c9747a2695c70a59aad8b096&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&bw=57610&logo=80000000"
                                DLNACastManager.getInstance().cast(
                                    device,
                                    CastObject.newInstance(
                                        url!!,
                                        UUID.randomUUID().toString(),
                                        "Test Sample"
                                    )
                                )
                                DLNACastManager.getInstance().play()
                            }) {
                                Text(text = device.details.friendlyName)
                            }
                        }
                    }
                }
            }
        }
        DLNACastManager.getInstance().search(null, 60)

    }

    override fun onDestroy() {
        super.onDestroy()
        DLNACastManager.getInstance().unbindCastService(this)
        DLNACastManager.getInstance().unregisterListener(listener)
    }
}