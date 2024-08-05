package cn.spacexc.wearbili.remake.app.update.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Update
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.main.ui.MainActivity
import cn.spacexc.wearbili.remake.app.splash.remote.Version
import cn.spacexc.wearbili.remake.app.welcome.screens.ReWearBiliText
import cn.spacexc.wearbili.remake.common.ui.BilibiliBlue
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.isRound
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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
    versionInfo: Version?,
    viewModel: UpdateViewModel
) {
    TitleBackground(title = "更新", onBack = ::finish, onRetry = {}) {
        versionInfo?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = titleBackgroundHorizontalPadding())
                    .verticalScroll(rememberScrollState()),
            ) {
                UpdateCard(updateInfo = it)
                Spacer(modifier = Modifier.height(3.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp),
                    horizontalAlignment = if (isRound()) Alignment.CenterHorizontally else Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "请加入QQ群以下载安装包",
                        fontFamily = wearbiliFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = BilibiliPink,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = if (isRound()) TextAlign.Center else TextAlign.Start
                    )
                    Text(
                        text = "更新日志",
                        fontFamily = wearbiliFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = versionInfo.updateLog.replace("\\n", "\n"),
                        fontFamily = wearbiliFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = if (isRound()) TextAlign.Center else TextAlign.Start
                    )
                    if (versionInfo.mandatory) {
                        Text(
                            text = "此版本为强制更新",
                            fontFamily = wearbiliFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = BilibiliPink,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = if (isRound()) TextAlign.Center else TextAlign.Start
                        )
                    } else {
                        Text(
                            text = "跳过此版本",
                            fontFamily = wearbiliFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = BilibiliBlue,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickVfx {
                                    startActivity(
                                        Intent(
                                            this@UpdateActivityScreen,
                                            MainActivity::class.java
                                        ).apply {
                                            putExtra("updateInfo", versionInfo)
                                        })
                                    finish()
                                    overridePendingTransition(
                                        R.anim.activity_fade_in,
                                        R.anim.activity_fade_out
                                    )
                                },
                            textAlign = if (isRound()) TextAlign.Center else TextAlign.Start
                        )
                    }
                }
                Spacer(modifier = Modifier.height(titleBackgroundHorizontalPadding()))
            }
        }
    }
}

@Composable
fun UpdateCard(
    updateInfo: Version,
    clickable: Boolean = false,
    context: Activity? = null
) {
    val localDensity = LocalDensity.current
    var cardHeight by remember {
        mutableStateOf(0.dp)
    }
    val outputFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
    val date = Date(updateInfo.updateTime)
    val formattedDate: String = outputFormat.format(date)
    Card(
        innerPaddingValues = PaddingValues(),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        fillMaxSize = false,
        isClickEnabled = clickable,
        onClick = {
            context?.apply {
                startActivity(Intent(this, UpdateActivity::class.java).apply {
                    putExtra("updateInfo", updateInfo)
                })
            }
        }
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_silk_background),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.height(cardHeight)
        )
        Column(
            modifier = Modifier
                .onSizeChanged {
                    with(localDensity) {
                        cardHeight = it.height.toDp()
                    }
                }
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row {
                ReWearBiliText(fontSize = 13.sp)
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "更新",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = wearbiliFontFamily,
                    fontSize = 13.sp
                )
            }
            Text(text = buildAnnotatedString {
                withStyle(SpanStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)) {
                    append(updateInfo.versionName)
                }
                withStyle(SpanStyle(fontSize = 10.sp, fontWeight = FontWeight.Medium)) {
                    append(" #")
                    append(updateInfo.versionCode.toString())
                }
            }, color = Color.White, fontFamily = wearbiliFontFamily)
            IconText(
                text = formattedDate,
                fontSize = 9.sp,
                color = Color.White,
                modifier = Modifier.alpha(0.7f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Update,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}