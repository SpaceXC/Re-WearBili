package cn.spacexc.wearbili.remake.app.login.qrcode.web.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.common.ui.rememberMutableInteractionSource

/**
 * Created by XC-Qan on 2023/4/2.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun LoginScreen(
    state: QrCodeLoginScreenState,
    onQrcodeClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                //.aspectRatio(1f)
                .fillMaxSize(0.7f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(4.dp)
        ) {
            Crossfade(targetState = state.currentLoginStatus, label = "") {
                when (it) {
                    QrCodeLoginStatus.Loading, QrCodeLoginStatus.GettingKey -> {
                        Image(
                            painter = painterResource(id = R.drawable.img_loading_2233),
                            contentDescription = "加载中...",
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }

                    QrCodeLoginStatus.Failed, QrCodeLoginStatus.Timeout, QrCodeLoginStatus.FailedGettingKey -> {
                        Image(
                            painter = painterResource(id = R.drawable.img_loading_2233_error),
                            contentDescription = "加载失败",
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(
                                    interactionSource = rememberMutableInteractionSource(),
                                    indication = null
                                ) {
                                    onQrcodeClicked()
                                }
                        )
                    }

                    QrCodeLoginStatus.Pending, QrCodeLoginStatus.Waiting -> {
                        state.qrCodeBitmap?.let { bitmap ->
                            Image(
                                bitmap = bitmap,
                                contentDescription = "登录二维码",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(6.dp)
                                    .clickable(
                                        interactionSource = rememberMutableInteractionSource(),
                                        indication = null
                                    ) {
                                        onQrcodeClicked()
                                    }
                            )
                        }
                    }

                    QrCodeLoginStatus.Success -> {
                        Text(text = "登录成功")
                    }
                }
            }

        }
        Crossfade(targetState = state.currentLoginStatus, label = "") {
            when (it) {
                QrCodeLoginStatus.Loading -> {
                    Text(
                        text = "二维码加载中",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                QrCodeLoginStatus.Failed -> {
                    Text(
                        text = "加载失败啦",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                QrCodeLoginStatus.Timeout -> {
                    Text(
                        text = "二维码失效啦",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                QrCodeLoginStatus.Pending -> {
                    Text(
                        text = "使用手机客户端扫描二维码",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                QrCodeLoginStatus.Waiting -> {
                    Text(
                        text = "请在手机上轻触确认",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                QrCodeLoginStatus.Success -> {
                    Text(
                        text = "登录成功",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                QrCodeLoginStatus.FailedGettingKey -> {
                    Text(
                        text = "跳转失败, 点击重新登录",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                QrCodeLoginStatus.GettingKey -> {
                    Text(
                        text = "登录成功, 正在跳转",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}