package cn.spacexc.wearbili.remake.common.ui

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.isAudioServiceUp
import cn.spacexc.wearbili.remake.app.player.audio.AudioPlayerActivity
import cn.spacexc.wearbili.remake.app.settings.SettingsManager
import cn.spacexc.wearbili.remake.common.ToastUtils.toastContent
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.theme.WearBiliTheme
import cn.spacexc.wearbili.remake.common.ui.theme.time.DefaultTimeSource
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import kotlinx.coroutines.delay

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
@OptIn(ExperimentalAnimationApi::class) //DON'T DELETE THIS!!!!!!!!!!!!!!!!(DELETING CAUSES BUILD TIME EXCEPTION "This is an experimental animation API.")
fun Activity.CirclesBackground(
    modifier: Modifier = Modifier,
    uiState: UIState = UIState.Success,
    isShowing: Boolean = true,
    backgroundColor: Color = Color.Black,
    themeColor: Color = BilibiliPink,
    ambientAlpha: Float = 0.6f,
    onRetry: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    val localDensity = LocalDensity.current
    var boxWidth by remember {
        mutableStateOf(0.dp)
    }   //需要获取父容器宽度来计算两个圆圈的宽度, 不直接设置fraction参数是因为大小不太对
    val isDarkTheme by SettingsManager.isDarkTheme.collectAsState(initial = false)
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.7f at 500
            },
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    //TODO 研究一下换成fraction参数
    LaunchedEffect(key1 = toastContent, block = {
        if (toastContent.isNotEmpty()) {
            delay(2000)
            toastContent = ""
        }
    })
    WearBiliTheme {
        if (isDarkTheme) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            ) {
                LoadableBox(
                    uiState = uiState,
                    content = content,
                    onLongClick = { this@CirclesBackground.finish() },
                    onRetry = onRetry
                )
            }
        } else {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .onGloballyPositioned {
                        boxWidth = with(localDensity) { it.size.width.toDp() }
                    }) {
                WearBiliAnimatedVisibility(
                    visible = isShowing,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.img_half_circle_white),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .alpha(if (uiState == UIState.Loading) ambientAlpha * alpha else ambientAlpha),
                            colorFilter = ColorFilter.tint(themeColor),
                            //contentScale = ContentScale.FillWidth

                        )
                    }

                }
                LoadableBox(
                    uiState = uiState,
                    content = content,
                    onLongClick = { this@CirclesBackground.finish() },
                    onRetry = onRetry
                )
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = toastContent.isNotEmpty(),
                enter = scaleIn() + fadeIn() + slideInVertically { it / 2 },
                exit = scaleOut() + fadeOut() + slideOutVertically { it / 2 },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = toastContent, modifier = Modifier
                        .padding(
                            bottom = 24.dp,
                            start = 8.dp,
                            end = 8.dp
                        )
                        .background(
                            color = Color(0, 0, 0, 153),
                            shape = CircleShape
                        )
                        .padding(8.dp)
                        .align(Alignment.BottomCenter)
                        .wearBiliAnimatedContentSize(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = wearbiliFontFamily,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}

@Composable
fun LoadableBox(
    modifier: Modifier = Modifier,
    uiState: UIState,
    onLongClick: () -> Unit = {},
    onRetry: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Crossfade(targetState = uiState, label = "AppBackgroundLoadableBox") {
            when (it) {
                UIState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickVfx(onLongClick = onLongClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_loading_2233),
                                contentDescription = "Loading...",
                                modifier = Modifier.fillMaxWidth()
                                //.fillMaxWidth(0.3f)
                                //.aspectRatio(1f)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "玩命加载中")
                        }
                    }
                }

                UIState.Success -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        content()
                    }
                }

                UIState.Failed -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickVfx(onLongClick = onLongClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .clickVfx(onLongClick = onLongClick, onClick = onRetry)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_loading_2233_error),
                                contentDescription = "Load Failed",
                                modifier = Modifier.fillMaxWidth()
                                //.fillMaxWidth(0.3f)
                                //.aspectRatio(1f)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "加载失败啦")
                        }
                    }
                }
            }
        }
    }
}

val TitleBackgroundHorizontalPadding = 12.dp

@Composable
fun Activity.TitleBackground(
    modifier: Modifier = Modifier,
    title: String,
    isTitleClipToBounds: Boolean = true,
    onBack: () -> Unit = {},
    onDropdown: () -> Unit = {},
    isTitleClickable: Boolean = true,
    isDropdownTitle: Boolean = false,
    isBackgroundShowing: Boolean = true,
    backgroundColor: Color = Color.Black,
    isDropdown: Boolean = true,
    uiState: UIState = UIState.Success,
    themeColor: Color = BilibiliPink,
    ambientAlpha: Float = 0.6f,
    onRetry: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    val timeSource = DefaultTimeSource("HH:mm")
    val timeText = timeSource.currentTime
    CirclesBackground(
        modifier = modifier,
        uiState = uiState,
        isShowing = isBackgroundShowing,
        backgroundColor = backgroundColor,
        themeColor = themeColor,
        ambientAlpha = ambientAlpha,
        onRetry = onRetry
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .padding(
                        horizontal = TitleBackgroundHorizontalPadding,
                        vertical = if (isAudioServiceUp) 6.5.dp else 8.dp
                    )
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = rememberMutableInteractionSource(),
                        indication = null
                    ) {
                        if (isTitleClickable) {
                            if (isDropdownTitle) onDropdown() else onBack()
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (isRound()) Arrangement.Center else Arrangement.Start
            ) {

                if (isDropdownTitle) {
                    Text(
                        text = buildAnnotatedString {
                            append(title)
                            appendInlineContent(id = "dropdownIcon")
                        },
                        style = AppTheme.typography.h2,
                        inlineContent = mapOf(
                            "dropdownIcon" to InlineTextContent(
                                placeholder = Placeholder(
                                    width = AppTheme.typography.h2.fontSize,
                                    height = AppTheme.typography.h2.fontSize,
                                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                                )
                            ) {
                                Icon(
                                    imageVector = if (isDropdown) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }),
                        textAlign = if (isRound()) TextAlign.Center else null,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Text(
                        text = buildAnnotatedString {
                            appendInlineContent(id = "backIcon")
                            append(title)
                        },
                        style = AppTheme.typography.h2,
                        inlineContent = mapOf(
                            "backIcon" to InlineTextContent(
                                placeholder = Placeholder(
                                    width = AppTheme.typography.h2.fontSize,
                                    height = AppTheme.typography.h2.fontSize,
                                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBackIos,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }),
                        textAlign = if (isRound()) TextAlign.Center else null,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight()
                    )
                }

                if (!isRound()) {
                    //Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = buildAnnotatedString {
                            if (isAudioServiceUp) {
                                appendInlineContent("playIcon")
                                append(' ')
                            }
                            append(timeText)
                        },
                        style = AppTheme.typography.h2,
                        modifier = Modifier
                            .clickVfx(isEnabled = isAudioServiceUp) {
                                startActivity(
                                    Intent(
                                        this@TitleBackground,
                                        AudioPlayerActivity::class.java
                                    ).apply {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    }
                                )
                            }
                            .then(
                                if (isAudioServiceUp) {
                                    Modifier
                                        .clip(CircleShape)
                                        .background(BilibiliPink)
                                        .padding(vertical = 1.5.dp, horizontal = 8.dp)
                                } else {
                                    Modifier
                                }
                            ),
                        inlineContent = mapOf(
                            "playIcon" to InlineTextContent(
                                placeholder = Placeholder(
                                    width = AppTheme.typography.h2.fontSize.times(0.8f),
                                    height = AppTheme.typography.h2.fontSize.times(0.8f),
                                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                                )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.img_play_icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .offset(),
                                    tint = Color.White
                                )
                            }
                        )
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    //.weight(1f)
                    .apply {
                        if (isTitleClipToBounds) clipToBounds()
                    }
            ) {
                content()
            }
        }
    }
}

@Composable
fun Wrapper(content: @Composable () -> Unit) {
    Box {
        content()
    }
}

@Composable
fun Activity.ArrowTitleBackgroundWithCustomBackground(
    onBack: () -> Unit = ::finish,
    background: @Composable BoxScope.() -> Unit,
    title: String = "",
    content: @Composable BoxScope.() -> Unit
) {
    val timeSource = DefaultTimeSource("HH:mm")
    val timeText = timeSource.currentTime

    LaunchedEffect(key1 = toastContent, block = {
        if (toastContent.isNotEmpty()) {
            delay(2000)
            toastContent = ""
        }
    })
    WearBiliTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            background()
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .padding(
                            start = TitleBackgroundHorizontalPadding,
                            end = TitleBackgroundHorizontalPadding,
                            top = 8.dp,
                            bottom = 4.dp
                        )
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = rememberMutableInteractionSource(),
                            indication = null
                        ) {
                            onBack()
                        },
                    verticalAlignment = if (isRound()) Alignment.CenterVertically else Alignment.Top
                ) {
                    Text(
                        text = buildAnnotatedString {
                            appendInlineContent(id = "backIcon")
                            append(title)
                        },
                        style = AppTheme.typography.h2,
                        inlineContent = mapOf(
                            "backIcon" to InlineTextContent(
                                placeholder = Placeholder(
                                    width = AppTheme.typography.h2.fontSize,
                                    height = AppTheme.typography.h2.fontSize,
                                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBackIos,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }),
                        textAlign = if (isRound()) TextAlign.Center else null,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight()
                    )
                    if (!isRound()) {
                        //Spacer(modifier = Modifier.weight(1f))
                        Text(text = timeText, style = AppTheme.typography.h2)
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                    //.weight(1f)
                    /*.apply {
                        if (isTitleClipToBounds) clipToBounds()
                    }*/
                ) {
                    content()
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = toastContent.isNotEmpty(),
                enter = scaleIn() + fadeIn() + slideInVertically { it / 2 },
                exit = scaleOut() + fadeOut() + slideOutVertically { it / 2 },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = toastContent, modifier = Modifier
                        .padding(
                            bottom = 24.dp,
                            start = 8.dp,
                            end = 8.dp
                        )
                        .background(
                            color = Color(0, 0, 0, 153),
                            shape = CircleShape
                        )
                        .padding(8.dp)
                        .align(Alignment.BottomCenter)
                        .wearBiliAnimatedContentSize(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = wearbiliFontFamily,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}