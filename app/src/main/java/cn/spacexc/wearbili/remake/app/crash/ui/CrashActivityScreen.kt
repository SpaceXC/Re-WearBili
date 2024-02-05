package cn.spacexc.wearbili.remake.app.crash.ui

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.common.copyToClipboard
import cn.spacexc.wearbili.remake.app.splash.ui.SplashScreenActivity
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.ClickableText
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateColorAsState
import kotlinx.coroutines.delay

/**
 * Created by XC-Qan on 2023/7/13.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */


//TODO implement crash screen buttons
@Composable
fun Activity.CrashActivityScreen(
    stacktrace: String,
    description: String,
) {
    TitleBackground(title = "", onBack = ::finish) {
        var currentHighlightedButton by remember {
            mutableStateOf("")
        }
        val copyButtonColor by wearBiliAnimateColorAsState(
            targetValue = if (currentHighlightedButton == "copy") BilibiliPink else Color(
                54,
                54,
                54,
                255
            )
        )
        val qrcodeButtonColor by wearBiliAnimateColorAsState(
            targetValue = if (currentHighlightedButton == "qrcode") BilibiliPink else Color(
                54,
                54,
                54,
                255
            )
        )
        val restartButtonColor by wearBiliAnimateColorAsState(
            targetValue = if (currentHighlightedButton == "restart") BilibiliPink else Color(
                54,
                54,
                54,
                255
            )
        )

        LaunchedEffect(key1 = currentHighlightedButton, block = {
            delay(1000)
            currentHighlightedButton = ""
        })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(TitleBackgroundHorizontalPadding - 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "呜啊＞︿＜!", fontSize = 18.spx, style = AppTheme.typography.h1)
            Text(text = "Sorry...  ごめんなさい...", style = AppTheme.typography.h1)
            val annotatedString = buildAnnotatedString {
                append("对不起！Re:WearBili刚刚出错了！\n")
                append("你可以使用下面的按钮来")
                withStyle(style = SpanStyle(color = BilibiliPink)) {
                    pushStringAnnotation(tag = "copy", annotation = "")
                    append("复制下面的报错日志")
                    pop()
                }
                append("，也可以")
                withStyle(style = SpanStyle(color = BilibiliPink)) {
                    pushStringAnnotation(tag = "qrcode", annotation = "")
                    append("生成带有报错日志信息的二维码")
                    pop()
                }
                append("，并将他们通过Github issue/QQ群等方式发送给开发者。完成这些之后，就可以")
                withStyle(style = SpanStyle(color = BilibiliPink)) {
                    pushStringAnnotation(tag = "restart", annotation = "")
                    append("重启应用")
                    pop()
                }
                append("啦！")
                append("\n")
                append("生成二维码需要网络连接。")
            }
            ClickableText(
                text = annotatedString,
                style = AppTheme.typography.h3,
                modifier = Modifier.alpha(0.7f),
            ) { index ->
                annotatedString.getStringAnnotations(tag = "copy", start = index, end = index)
                    .firstOrNull()?.let {
                    currentHighlightedButton = "copy"
                }
                annotatedString.getStringAnnotations(tag = "qrcode", start = index, end = index)
                    .firstOrNull()?.let {
                    currentHighlightedButton = "qrcode"
                }
                annotatedString.getStringAnnotations(tag = "restart", start = index, end = index)
                    .firstOrNull()?.let {
                    currentHighlightedButton = "restart"
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    innerPaddingValues = PaddingValues(14.dp),
                    borderColor = copyButtonColor,
                    onClick = {
                        stacktrace.copyToClipboard(this@CrashActivityScreen)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = "复制日志到剪贴板",
                        tint = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    innerPaddingValues = PaddingValues(14.dp),
                    borderColor = qrcodeButtonColor,
                    onClick = {

                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.QrCode,
                        contentDescription = "生成日志二维码",
                        tint = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    innerPaddingValues = PaddingValues(14.dp),
                    borderColor = restartButtonColor,
                    onClick = {
                        startActivity(
                            Intent(
                                this@CrashActivityScreen,
                                SplashScreenActivity::class.java
                            )
                        )
                        finish()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.RestartAlt,
                        contentDescription = "重启应用",
                        tint = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
            Text(
                text = stacktrace,
                style = AppTheme.typography.h3,
                fontSize = 9.5.spx,
                modifier = Modifier.alpha(0.6f)
            )
        }
    }
}