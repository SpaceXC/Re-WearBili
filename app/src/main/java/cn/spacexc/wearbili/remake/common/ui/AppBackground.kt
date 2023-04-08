package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.settings.SettingsManager
import cn.spacexc.wearbili.remake.common.ui.theme.WearBiliTheme
import cn.spacexc.wearbili.remake.common.ui.theme.time.DefaultTimeSource

/**
 * Created by XC-Qan on 2023/3/21.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */


/**
 * 根背景，理论上每一处可见UI都应该以此为背景
 * 已包含全局主题
 */
@Composable
fun CirclesBackground(
    modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit
) {
    val localDensity = LocalDensity.current
    var boxWidth by remember {
        mutableStateOf(0.dp)
    }   //需要获取父容器宽度来计算两个圆圈的宽度, 不直接设置fraction参数是因为大小不太对
    //TODO 研究一下换成fraction参数

    WearBiliTheme {
        if (SettingsManager.getInstance().isDarkTheme) {
            Box(modifier = Modifier.fillMaxSize()) {
                content()
            }
        } else {
            Box(modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
                .onGloballyPositioned {
                    boxWidth = with(localDensity) { it.size.width.toDp() }
                }) {
                Image(
                    painter = painterResource(id = R.drawable.img_circle_top_right),
                    contentDescription = null,
                    modifier = Modifier
                        .align(
                            Alignment.TopEnd
                        )
                        .size(boxWidth * 0.75f)
                )
                Image(
                    painter = painterResource(id = R.drawable.img_circle_bottom_left),
                    contentDescription = null,
                    modifier = Modifier
                        .align(
                            Alignment.BottomStart
                        )
                        .size(boxWidth * 0.75f)
                )
                content()
            }
        }
    }
}

@Composable
fun TitleBackground(
    modifier: Modifier = Modifier,
    title: String,
    onBack: () -> Unit = {},
    onDropdown: () -> Unit = {},
    isDropdownTitle: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val timeSource = DefaultTimeSource("HH:mm")
    val timeText = timeSource.currentTime
    CirclesBackground(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 11.dp, vertical = 6.dp)
                    .fillMaxWidth()
                    .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                        if (isDropdownTitle) onDropdown() else onBack()
                    }

            ) {
                if (isDropdownTitle) {
                    Text(
                        text = buildAnnotatedString {
                            append(title)
                            appendInlineContent(id = "dropdownIcon")
                        },
                        style = MaterialTheme.typography.h2,
                        inlineContent = mapOf(
                            "dropdownIcon" to InlineTextContent(
                                placeholder = Placeholder(
                                    width = MaterialTheme.typography.h2.fontSize,
                                    height = MaterialTheme.typography.h2.fontSize,
                                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.fillMaxSize()
                                )
                            })
                    )
                } else {
                    Text(
                        text = buildAnnotatedString {
                            appendInlineContent(id = "backIcon")
                            append(title)
                        },
                        style = MaterialTheme.typography.h2,
                        inlineContent = mapOf(
                            "backIcon" to InlineTextContent(
                                placeholder = Placeholder(
                                    width = MaterialTheme.typography.h2.fontSize,
                                    height = MaterialTheme.typography.h2.fontSize,
                                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBackIos,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.fillMaxSize()
                                )
                            })
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(text = timeText, style = MaterialTheme.typography.h2)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clipToBounds()
            ) {
                content()
            }
        }
    }
}