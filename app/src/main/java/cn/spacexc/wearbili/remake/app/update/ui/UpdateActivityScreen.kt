package cn.spacexc.wearbili.remake.app.update.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.app.splash.ui.SplashScreenActivity
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
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
fun Context.UpdateActivityScreen(
    versionInfo: SplashScreenActivity.AppUpdatesResult?
) {
    TitleBackground(title = "更新") {
        versionInfo?.let {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputFormat = SimpleDateFormat("yyyy年MM月dd日")
            val date: Date = inputFormat.parse(versionInfo.createdAt) as Date
            val formattedDate: String = outputFormat.format(date)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(TitleBackgroundHorizontalPadding)
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
            }
        }
    }
}