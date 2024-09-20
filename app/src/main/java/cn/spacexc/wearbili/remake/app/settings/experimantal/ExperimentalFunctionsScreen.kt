package cn.spacexc.wearbili.remake.app.settings.experimantal

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.settings.LocalConfiguration
import cn.spacexc.wearbili.remake.app.settings.SettingsManager
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.Switch
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.proto.settings.AppConfiguration
import cn.spacexc.wearbili.remake.proto.settings.copy

const val EXPERIMENTAL_MULTI_ACCOUNTS = "experimental.multi.account"
const val EXPERIMENTAL_LARGE_VIDEO_CARD = "experimental.large.video.card"
const val EXPERIMENTAL_FLOATING_SUBTITLE = "experimental.global.floating.subtitle"
const val EXPERIMENTAL_FADE_SUBTITLE = "experimental.fade.subtitle.animation"

@Composable
fun ExperimentalFunctionsScreen(navController: NavController) {
    TitleBackground(
        navController = navController,
        title = "",
        onRetry = { },
        onBack = navController::navigateUp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = titleBackgroundHorizontalPadding(), vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_experimental_function),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(28.dp)
                //.padding(20.dp)
            )
            Text(
                text = "实验性功能",
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                fontFamily = wearbiliFontFamily
            )
            Text(
                text = "这里是正在实验的功能\n" +
                        "在可能出现不稳定运行的同时\n" +
                        "随时可能移除或调整",
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                modifier = Modifier.alpha(0.8f),
                fontFamily = wearbiliFontFamily,
                textAlign = TextAlign.Center
            )
            ExperimentalFunctionCard(
                imageId = R.drawable.img_banner_experimental_multi_account,
                name = "好多账号！",
                description = "允许切换多个不同账户",
                functionId = EXPERIMENTAL_MULTI_ACCOUNTS
            )
            ExperimentalFunctionCard(
                imageId = R.drawable.img_banner_experimantal_large_video_card,
                name = "是大卡片！",
                description = "推荐页由列表更换为卡片",
                functionId = EXPERIMENTAL_LARGE_VIDEO_CARD
            )
            ExperimentalFunctionCard(
                imageId = R.drawable.img_banner_experimental_floating_subtitle,
                name = "浮动字幕",
                description = "音频模式下开启全局浮动字幕",
                functionId = EXPERIMENTAL_FLOATING_SUBTITLE
            )
            ExperimentalFunctionCard(
                name = "逐字字幕",
                description = "音频模式下，字幕逐字淡入显示",
                functionId = EXPERIMENTAL_FADE_SUBTITLE
            )
        }
    }
}

@kotlinx.serialization.Serializable
object ExperimentalFunctionsScreen

@Composable
fun ExperimentalFunctionCard(
    @DrawableRes imageId: Int,
    name: String,
    description: String,
    functionId: String
) {
    val configuration = LocalConfiguration.current
    var isOn by remember {
        mutableStateOf(configuration.getActivatedExperimentalFunctions().contains(functionId))
    }
    LaunchedEffect(key1 = isOn) {
        val tempList = configuration.getActivatedExperimentalFunctions().toMutableList()
        if (isOn) {
            if (!configuration.getActivatedExperimentalFunctions().contains(functionId))
                tempList.add(functionId)
        } else {
            tempList.remove(functionId)
        }
        SettingsManager.updateConfiguration {
            copy {
                activatedExperimentFunctions = tempList.joinToString(",")
            }
        }
    }
    Card(innerPaddingValues = PaddingValues(), shape = RoundedCornerShape(13)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12))
                )
                IconText(
                    text = "实验性",
                    spacingWidth = 5.sp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    fontSize = 11.sp,
                    fontFamily = wearbiliFontFamily,
                    fontWeight = FontWeight.Medium
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_experimental_function),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = wearbiliFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = description,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontFamily = wearbiliFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.alpha(0.8f)
                    )
                }
                Switch(isOn = isOn) {
                    isOn = it
                }
            }
        }
    }
}

@Composable
fun ExperimentalFunctionCard(
    name: String,
    description: String,
    functionId: String
) {
    val configuration = LocalConfiguration.current
    var isOn by remember {
        mutableStateOf(configuration.getActivatedExperimentalFunctions().contains(functionId))
    }
    LaunchedEffect(key1 = isOn) {
        val tempList = configuration.getActivatedExperimentalFunctions().toMutableList()
        if (isOn) {
            if (!configuration.getActivatedExperimentalFunctions().contains(functionId))
                tempList.add(functionId)
        } else {
            tempList.remove(functionId)
        }
        SettingsManager.updateConfiguration {
            copy {
                activatedExperimentFunctions = tempList.joinToString(",")
            }
        }
    }
    Card(innerPaddingValues = PaddingValues(), shape = RoundedCornerShape(18.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = wearbiliFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = description,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontFamily = wearbiliFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.alpha(0.8f)
                    )
                }
                Switch(isOn = isOn) {
                    isOn = it
                }
            }
        }
    }
}

fun AppConfiguration.getActivatedExperimentalFunctions() = activatedExperimentFunctions.split(",")