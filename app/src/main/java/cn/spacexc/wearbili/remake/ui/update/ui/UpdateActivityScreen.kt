package cn.spacexc.wearbili.remake.ui.update.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SystemUpdateAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.common.domain.data.DataStoreManager
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.AppUpdatesResult
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.ui.main.ui.MainActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


/**
 * Created by XC-Qan on 2023/4/30.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@SuppressLint("SimpleDateFormat")
@Composable
fun Activity.UpdateActivityScreen(
    versionInfo: AppUpdatesResult?,
    viewModel: UpdateViewModel
) {
    val downloadPercent by viewModel.downloadProgress.collectAsState()
    val downloadSpeed by viewModel.downloadSpeed.collectAsState()
    val downloadTimeLeft by viewModel.downloadTimeLeft.collectAsState()

    val scope = rememberCoroutineScope()
    TitleBackground(title = "更新", onBack = ::finish) {
        versionInfo?.let {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputFormat = SimpleDateFormat("yyyy年MM月dd日")
            val date: Date = inputFormat.parse(versionInfo.createdAt) as Date
            val formattedDate: String = outputFormat.format(date)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = TitleBackgroundHorizontalPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "发现新版本",
                    style = AppTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = versionInfo.versionName,
                    style = AppTheme.typography.h1,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "版本 #${versionInfo.versionCode}",
                    style = AppTheme.typography.h2,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "更新时间: $formattedDate",
                    style = AppTheme.typography.h2,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = versionInfo.updateLog,
                    style = AppTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
                if (viewModel.isDownloading) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "$downloadPercent%",
                            style = AppTheme.typography.body1,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = downloadSpeed,
                            style = AppTheme.typography.body1,
                            textAlign = TextAlign.Center
                        )
                    }
                    LinearProgressIndicator(
                        progress = downloadPercent / 100f,
                        modifier = Modifier.fillMaxWidth(),
                        color = BilibiliPink
                    )
                    Text(
                        text = "剩余时间：$downloadTimeLeft",
                        style = AppTheme.typography.body1,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        outerPaddingValues = PaddingValues(vertical = 8.dp),
                        innerPaddingValues = PaddingValues(12.dp),
                        shape = RoundedCornerShape(30),
                        onClick = {
                            viewModel.download(versionInfo.downloadAddress, versionInfo.versionCode)
                        }
                    ) {
                        IconText(
                            text = "下载并更新！",
                            fontSize = 12.spx,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.align(
                                Alignment.Center
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.SystemUpdateAlt,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                    Text(
                        text = "跳过此版本",
                        style = AppTheme.typography.body1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(0.6f)
                            .padding(6.dp)
                            .clickVfx {
                                scope.launch {
                                    DataStoreManager
                                        .getInstance(this@UpdateActivityScreen)
                                        .saveInt("latestSkippedVersion", versionInfo.versionCode)
                                    startActivity(
                                        Intent(
                                            this@UpdateActivityScreen,
                                            MainActivity::class.java
                                        )
                                    )
                                    finish()
                                    overridePendingTransition(
                                        R.anim.activity_fade_in,
                                        R.anim.activity_fade_out
                                    )
                                }
                            }
                    )
                }
            }
        }
    }
}