package cn.spacexc.wearbili.remake.app.player.cast.discover

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.common.domain.log.TAG
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.spx

@Composable
fun Activity.DeviceDiscoverScreen(
    viewModel: DeviceDiscoverViewModel,
    wifiName: String?
) {
    LaunchedEffect(key1 = viewModel.deviceList) {
        Log.d(TAG, "devices updated: ${viewModel.deviceList}")
    }

    TitleBackground(title = "", onBack = ::finish, onRetry = {}) {
        if (wifiName == null) {
            Text(
                text = "需要连接Wi-Fi才能投屏哦",
                fontSize = 16.spx,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
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
                    }

                }
                viewModel.deviceList.forEach { device ->
                    item {
                        Card(modifier = Modifier.fillMaxWidth(), onClick = {

                        }, innerPaddingValues = PaddingValues(horizontal = 12.dp, vertical = 14.dp))
                        {
                            IconText(
                                text = device.friendlyName,
                                color = Color.White,
                                fontSize = 14.spx,
                                fontWeight = FontWeight.Medium
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = if (device.isBilibiliDevice) R.drawable.icon_cloud_tv_bilibili else R.drawable.icon_tv
                                    ),
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