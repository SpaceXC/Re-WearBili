package cn.spacexc.wearbili.remake.app.videoplayer.defaultplayer

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.view.SurfaceView
import android.view.TextureView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.ScreenRotation
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import cn.spacexc.wearbili.common.domain.time.secondToTime
import cn.spacexc.wearbili.remake.R.drawable
import cn.spacexc.wearbili.remake.app.videoplayer.danmaku.BiliDanmukuParser
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import master.flame.danmaku.controller.DrawHandler
import master.flame.danmaku.danmaku.loader.ILoader
import master.flame.danmaku.danmaku.loader.IllegalDataException
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDisplayer
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.parser.IDataSource
import master.flame.danmaku.ui.widget.DanmakuView
import kotlin.math.roundToInt

val BilibiliPink = Color(254, 103, 154)

/**
 * Created by XC-Qan on 2023/5/20.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

enum class VideoDisplaySurface {
    TEXTURE_VIEW,
    SURFACE_VIEW
}

sealed class VideoPlayerPages(val weight: Int) {
    object Main : VideoPlayerPages(0)
    object Settings : VideoPlayerPages(1)
}

@UnstableApi
@Composable
@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)    //不要删掉这个OptIn!!!!!!!!!!!!!!!!!!灰色的也别删掉!!!!!!!!!!!!
fun Activity.Media3PlayerScreen(
    viewModel: Media3PlayerViewModel,
    displaySurface: VideoDisplaySurface,
    withSubtitleAnimation: Boolean = true,
    context: Context,
    onBack: () -> Unit
) {
    val currentSubtitleText by viewModel.currentSubtitleText.collectAsState(initial = null)
    val currentPlayerPosition by viewModel.currentPlayProgress.collectAsState(initial = 0)
    val localDensity = LocalDensity.current
    var controllerProgressColumnHeight by remember {
        mutableStateOf(0.dp)
    }
    var draggedProgress by remember {
        mutableStateOf(0L)
    }
    var isDraggingProgress by remember {
        mutableStateOf(false)
    }
    var dragSensibility by remember {
        mutableStateOf(60)
    }
    var playBackSpeed by remember {
        mutableStateOf(100)
    }
    var currentVolume by remember {
        mutableStateOf(context.getCurrentVolume())
    }
    val progressDraggableState = rememberDraggableState(onDelta = {
        if (draggedProgress + (it * dragSensibility).toLong() < 0) {
            draggedProgress = 0
        } else if (draggedProgress + (it * dragSensibility).toLong() > viewModel.player.duration) {
            draggedProgress = viewModel.player.duration
        } else {
            draggedProgress += (it * dragSensibility).toLong()
        }
    })
    val progressBarThumbScale by animateFloatAsState(targetValue = if (isDraggingProgress) 1.5f else 1f)
    val subtitleOffset by animateDpAsState(targetValue = if (viewModel.isVideoControllerVisible) controllerProgressColumnHeight - 14.dp else 0.dp)
    val subtitlePadding by animateDpAsState(targetValue = if (viewModel.isVideoControllerVisible) 0.dp else 6.dp)
    var currentPage: VideoPlayerPages by remember {
        mutableStateOf(VideoPlayerPages.Main)
    }
    val backgroundColor by animateColorAsState(
        targetValue = if (currentPage == VideoPlayerPages.Main) Color.Transparent else Color(
            0,
            0,
            0,
            153
        )
    )

    val danmakuInputStream by viewModel.danmakuInputStream.collectAsState()
    var danmakuView: DanmakuView? = null
    var isDanmakuVisible by remember {
        mutableStateOf(true)
    }
    val danmakuButtonColor by animateColorAsState(
        targetValue = if (isDanmakuVisible) BilibiliPink else Color(
            38,
            38,
            38,
            255
        )
    )
    val danmakuAlpha by animateFloatAsState(targetValue = if (isDanmakuVisible) 1f else 0f)

    //For Danmaku
    LaunchedEffect(key1 = danmakuInputStream, block = {
        if (danmakuInputStream != null && danmakuView != null) {
            val danmakuContext: DanmakuContext = DanmakuContext.create()       //弹幕context

            val maxLinesPair = HashMap<Int, Int>()     //弹幕最多行数
            maxLinesPair[BaseDanmaku.TYPE_SCROLL_RL] = 5
            maxLinesPair[BaseDanmaku.TYPE_SCROLL_LR] = 5

            val overlappingEnablePair = HashMap<Int, Boolean>()     //防弹幕重叠
            overlappingEnablePair[BaseDanmaku.TYPE_SCROLL_LR] = true
            overlappingEnablePair[BaseDanmaku.TYPE_SCROLL_RL] = true
            overlappingEnablePair[BaseDanmaku.TYPE_FIX_BOTTOM] = true

            danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 1f) //设置默认样式
                .setDuplicateMergingEnabled(true)      //合并重复弹幕
                .setScrollSpeedFactor(1.2f)     //弹幕速度
                .setScaleTextSize(0.8f)     //文字大小
                //.setCacheStuffer(SpannedCacheStuffer()) // 图文混排使用SpannedCacheStuffer  设置缓存绘制填充器，默认使用{@link SimpleTextCacheStuffer}只支持纯文字显示, 如果需要图文混排请设置{@link SpannedCacheStuffer}如果需要定制其他样式请扩展{@link SimpleTextCacheStuffer}|{@link SpannedCacheStuffer}
                .setMaximumLines(maxLinesPair) //设置最大显示行数
                .preventOverlapping(overlappingEnablePair) //设置防弹幕重叠，null为允许重叠

            val loader: ILoader =
                DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI)       //设置解析b站xml弹幕
            try {
                loader.load(danmakuInputStream)
            } catch (e: IllegalDataException) {
                e.printStackTrace()
                viewModel.appendLoadMessage("弹幕装载失败! ${e.message}")
            }
            val danmukuParser = BiliDanmukuParser()
            val dataSource: IDataSource<*> = loader.dataSource
            danmukuParser.load(dataSource)

            danmakuView!!.setCallback(object : DrawHandler.Callback {
                override fun updateTimer(timer: DanmakuTimer?) {
                    if (playBackSpeed != 100) {
                        timer?.let {
                            timer.add(
                                (timer.lastInterval() * (playBackSpeed.toFloat().div(100)
                                    .minus(1))).toLong()
                            )
                        }
                    }
                }

                override fun danmakuShown(danmaku: BaseDanmaku?) {

                }

                override fun drawingFinished() {

                }

                override fun prepared() {
                    viewModel.appendLoadMessage("弹幕装载成功!")
                }
            })
            danmakuView!!.prepare(danmukuParser, danmakuContext)      //准备弹幕
            danmakuView!!.enableDanmakuDrawingCache(true)
        }
    })
    LaunchedEffect(key1 = viewModel.currentStat, block = {
        when (viewModel.currentStat) {
            PlayerStats.Loading -> {

            }

            PlayerStats.Playing -> {
                danmakuView?.seekTo(currentPlayerPosition)
                danmakuView?.resume()
            }

            PlayerStats.Buffering -> {
                danmakuView?.pause()
            }

            PlayerStats.Paused -> {
                danmakuView?.pause()
            }

            PlayerStats.Finished -> {

            }
        }
    })
    //For Danmaku

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(viewModel.videoPlayerAspectRatio)
                .align(Alignment.Center)
        ) {
            when (displaySurface) {
                VideoDisplaySurface.TEXTURE_VIEW -> {
                    AndroidView(factory = { TextureView(it) }) { textureView ->
                        viewModel.player.setVideoTextureView(textureView)
                    }
                }

                VideoDisplaySurface.SURFACE_VIEW -> {
                    AndroidView(factory = { SurfaceView(it) }) { surfaceView ->
                        viewModel.player.setVideoSurfaceView(surfaceView)
                    }
                }
            }
        }
        AndroidView(
            factory = { DanmakuView(it) },
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 4.dp)
                .alpha(danmakuAlpha)
        ) {
            danmakuView = it
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            AnimatedContent(targetState = currentPage, transitionSpec = {
                if (targetState.weight > initialState.weight) {
                    slideInHorizontally { height -> height } + fadeIn() with
                            slideOutHorizontally { height -> -height } + fadeOut()
                } else {
                    slideInHorizontally { height -> -height } + fadeIn() with
                            slideOutHorizontally { height -> height } + fadeOut()
                }
            }) {
                when (currentPage) {
                    VideoPlayerPages.Main -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            if (viewModel.currentStat == PlayerStats.Loading) {
                                IconText(
                                    text = "关闭",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .alpha(0.8f)
                                        .clickable { onBack() }

                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBackIosNew,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            }
                            viewModel.videoInfo?.let { video ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .draggable(
                                            state = progressDraggableState,
                                            orientation = Orientation.Horizontal,
                                            startDragImmediately = false,
                                            onDragStarted = {
                                                draggedProgress = currentPlayerPosition
                                                viewModel.isVideoControllerVisible = true
                                                isDraggingProgress = true
                                            },
                                            onDragStopped = {
                                                viewModel.player.seekTo(draggedProgress)
                                                danmakuView?.seekTo(draggedProgress)
                                                viewModel.currentStat = PlayerStats.Buffering
                                                isDraggingProgress = false
                                            }
                                        )
                                        .pointerInput(Unit) {
                                            detectTapGestures(onTap = {
                                                viewModel.isVideoControllerVisible =
                                                    !viewModel.isVideoControllerVisible
                                            }, onDoubleTap = {
                                                if (viewModel.player.isPlaying) viewModel.player.pause() else viewModel.player.play()
                                            })
                                        }
                                ) {
                                    var titleRowHeight by remember {
                                        mutableStateOf(0.dp)
                                    }
                                    AnimatedVisibility(
                                        visible = viewModel.isVideoControllerVisible,
                                        enter = slideInVertically() + fadeIn(),
                                        exit = slideOutVertically() + fadeOut()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .background(
                                                    Brush.verticalGradient(
                                                        colors = listOf(
                                                            Color.Black,
                                                            Color.Transparent
                                                        )
                                                    )
                                                )
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp, horizontal = 6.dp)
                                                .onSizeChanged {
                                                    titleRowHeight =
                                                        with(localDensity) { it.height.toDp() }
                                                }
                                                .clickable(
                                                    interactionSource = MutableInteractionSource(),
                                                    indication = null,
                                                    onClick = onBack
                                                ), verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowBackIosNew,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(titleRowHeight * 0.55f)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Column {
                                                Text(
                                                    text = video.data.title,
                                                    color = Color.White,
                                                    fontSize = 13.sp,
                                                    fontFamily = wearbiliFontFamily,
                                                    fontWeight = FontWeight.Medium,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = "${viewModel.onlineCount}人在看",
                                                    color = Color.White,
                                                    fontSize = 11.sp,
                                                    fontFamily = wearbiliFontFamily,
                                                    fontWeight = FontWeight.Normal,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.alpha(0.7f)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .weight(1f)
                                    )
                                    AnimatedVisibility(
                                        visible = viewModel.isVideoControllerVisible,
                                        enter = slideInVertically { it / 2 } + fadeIn(),
                                        exit = slideOutVertically { it / 2 } + fadeOut(),
                                        modifier = Modifier.onSizeChanged {
                                            controllerProgressColumnHeight =
                                                with(localDensity) { it.height.toDp() }
                                        }
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .background(
                                                    Brush.verticalGradient(
                                                        colors = listOf(
                                                            Color.Transparent,
                                                            Color.Black
                                                        )
                                                    )
                                                )
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp, horizontal = 6.dp)
                                        ) {
                                            Text(
                                                text = "${
                                                    (if (isDraggingProgress) draggedProgress else currentPlayerPosition).div(
                                                        1000
                                                    ).secondToTime()
                                                }/${
                                                    viewModel.videoDuration.div(1000).secondToTime()
                                                }",
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontFamily = wearbiliFontFamily,
                                                fontWeight = FontWeight.Medium,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier
                                                    .padding(start = 4.dp)
                                                    .offset(y = 18.dp)
                                            )
                                            androidx.compose.material3.Slider(
                                                value = if (isDraggingProgress) draggedProgress.toFloat() else currentPlayerPosition.toFloat(),
                                                onValueChange = {
                                                    isDraggingProgress = true
                                                    draggedProgress = it.toLong()
                                                },
                                                onValueChangeFinished = {
                                                    viewModel.player.seekTo(draggedProgress)
                                                    danmakuView?.seekTo(draggedProgress)
                                                    viewModel.currentStat = PlayerStats.Buffering
                                                    isDraggingProgress = false
                                                },
                                                valueRange = 0f..viewModel.videoDuration.toFloat(),
                                                colors = SliderDefaults.colors(
                                                    activeTrackColor = BilibiliPink
                                                ),
                                                thumb = {
                                                    SliderDefaults.Thumb(
                                                        interactionSource = MutableInteractionSource(),
                                                        thumbSize = DpSize(12.dp, 12.dp),
                                                        modifier = Modifier
                                                            .offset(
                                                                y = 4.dp,
                                                                x = /*-(4.dp / viewModel.player.duration.toFloat()) * currentPlayerPosition.toFloat() + 2.dp*/ 2.dp
                                                            )
                                                            .scale(progressBarThumbScale),   //别问这串公式怎么得出来的，问就是数学的力量
                                                        /**
                                                         * 上面公式的作用：视频开始时offset = 2.dp
                                                         *              播放到中间时offset = 0.dp
                                                         *              播放结束时offset = -2.dp
                                                         * 可以防止thumb过于靠近屏幕边缘
                                                         * 原始函数解析式：y=-(2n/m)x+n，其中y为offset的值，n是基准offset，m为视频总长，x为当前播放进度
                                                         * 感谢群U帮助我不太聪明的大脑
                                                         * *基准offset=2.dp，为视频开始时的offset
                                                         *
                                                         * (两分钟之后发现根本不需要这个公式，直接2dp的效果就很好...
                                                         */
                                                        colors = SliderDefaults.colors(
                                                            thumbColor = Color.White
                                                        )
                                                    )
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .scale(1.05f)
                                                    .offset(y = (9).dp)
                                            )
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(
                                                        start = 4.dp,
                                                        end = 4.dp,
                                                        bottom = 6.dp
                                                    ),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Image(
                                                    painter = painterResource(id = if (viewModel.player.isPlaying) drawable.img_pause_icon else drawable.img_play_icon),
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        //.clickVfx { if (viewModel.player.isPlaying) viewModel.player.pause() else viewModel.player.play() }
                                                        .clickable { if (viewModel.player.isPlaying) viewModel.player.pause() else viewModel.player.play() }
                                                        .size(18.dp)

                                                )
                                                Spacer(modifier = Modifier.weight(1f))

                                                Box(
                                                    modifier = Modifier
                                                        .background(
                                                            color = danmakuButtonColor,
                                                            shape = CircleShape
                                                        )
                                                        .size(22.dp)
                                                        .clickable {
                                                            isDanmakuVisible = !isDanmakuVisible
                                                        },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        painter = painterResource(id = drawable.icon_danmaku),
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier
                                                            .size(14.dp)
                                                    )
                                                }
                                                Box(
                                                    modifier = Modifier
                                                        .background(
                                                            color = Color(
                                                                38,
                                                                38,
                                                                38,
                                                                255
                                                            ),
                                                            shape = CircleShape
                                                        )
                                                        .size(22.dp)
                                                        .clickable {
                                                            rotateScreen()
                                                        },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Outlined.ScreenRotation,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier
                                                            .size(12.dp)
                                                    )
                                                }

                                                Box(
                                                    modifier = Modifier
                                                        .background(
                                                            color = Color(
                                                                38,
                                                                38,
                                                                38,
                                                                255
                                                            ),
                                                            shape = CircleShape
                                                        )
                                                        .size(22.dp)
                                                        .clickable {
                                                            currentPage = VideoPlayerPages.Settings
                                                        },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Outlined.Settings,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier
                                                            .size(14.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                            if (withSubtitleAnimation) {
                                AnimatedVisibility(
                                    visible = currentSubtitleText != null,
                                    enter = scaleIn() + fadeIn(),
                                    exit = scaleOut() + fadeOut(),
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .offset(y = -subtitleOffset)
                                ) {
                                    Text(
                                        text = currentSubtitleText ?: "", modifier = Modifier
                                            .padding(
                                                bottom = subtitlePadding,
                                                start = 8.dp,
                                                end = 8.dp
                                            )
                                            .background(
                                                color = Color(49, 47, 47, 153),
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .padding(6.dp)
                                            .align(Alignment.BottomCenter)
                                            .animateContentSize(),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontFamily = wearbiliFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else {
                                currentSubtitleText?.let {
                                    Text(
                                        text = currentSubtitleText ?: "", modifier = Modifier
                                            .padding(
                                                bottom = subtitlePadding,
                                                start = 8.dp,
                                                end = 8.dp
                                            )
                                            .background(
                                                color = Color(49, 47, 47, 153),
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .padding(6.dp)
                                            .align(Alignment.BottomCenter)
                                            .offset(y = subtitleOffset),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontFamily = wearbiliFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            if (viewModel.currentStat == PlayerStats.Buffering) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center),
                                    color = BilibiliPink
                                )
                            }
                        }
                    }

                    VideoPlayerPages.Settings -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            IconText(
                                text = "返回",
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier
                                    .alpha(0.8f)
                                    .clickable { currentPage = VideoPlayerPages.Main }

                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBackIosNew,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                            Divider(color = Color(148, 148, 148, 204))
                            Column {
                                Text(
                                    text = "CC字幕",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .alpha(0.9f)
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.horizontalScroll(rememberScrollState())
                                ) {
                                    Text(
                                        text = "关闭", modifier = Modifier
                                            .clickable {
                                                viewModel.currentSubtitleLanguage = null
                                            },
                                        color = if (viewModel.currentSubtitleLanguage == null) BilibiliPink else Color.White,
                                        fontSize = 12.sp,
                                        fontFamily = wearbiliFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center
                                    )
                                    viewModel.subtitleList.forEach {
                                        Text(
                                            text = it.value.subtitleLanguage,
                                            modifier = Modifier
                                                .clickable {
                                                    viewModel.currentSubtitleLanguage =
                                                        it.key
                                                },
                                            color = if (viewModel.currentSubtitleLanguage == it.key) BilibiliPink else Color.White,
                                            fontSize = 12.sp,
                                            fontFamily = wearbiliFontFamily,
                                            fontWeight = FontWeight.Normal,
                                            textAlign = TextAlign.Center,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                            Divider(color = Color(148, 148, 148, 204))
                            Column {
                                Text(
                                    text = "滑动灵敏度",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .alpha(0.9f)
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.horizontalScroll(rememberScrollState())
                                ) {
                                    for (i in 60..180 step 30) {
                                        Text(
                                            text = i.toString(),
                                            modifier = Modifier
                                                .clickable {
                                                    dragSensibility = i
                                                },
                                            color = if (dragSensibility == i) BilibiliPink else Color.White,
                                            fontSize = 12.sp,
                                            fontFamily = wearbiliFontFamily,
                                            fontWeight = FontWeight.Normal,
                                            textAlign = TextAlign.Center,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                            Divider(color = Color(148, 148, 148, 204))
                            Column {
                                Text(
                                    text = "播放速度",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .alpha(0.9f)
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.horizontalScroll(rememberScrollState())
                                ) {
                                    for (i in 25 until 100 step 25) {
                                        Text(
                                            text = i.toFloat().div(100).toString() + "x",
                                            modifier = Modifier
                                                .clickable {
                                                    viewModel.player.setPlaybackSpeed(
                                                        i.toFloat().div(100)
                                                    )
                                                    playBackSpeed = i
                                                },
                                            color = if (playBackSpeed == i) BilibiliPink else Color.White,
                                            fontSize = 12.sp,
                                            fontFamily = wearbiliFontFamily,
                                            fontWeight = FontWeight.Normal,
                                            textAlign = TextAlign.Center,
                                            maxLines = 1
                                        )
                                    }
                                    for (i in 100..300 step 50) {
                                        Text(
                                            text = i.toFloat().div(100).toString() + "x",
                                            modifier = Modifier
                                                .clickable {
                                                    viewModel.player.setPlaybackSpeed(
                                                        i.toFloat().div(100)
                                                    )
                                                    playBackSpeed = i
                                                },
                                            color = if (playBackSpeed == i) BilibiliPink else Color.White,
                                            fontSize = 12.sp,
                                            fontFamily = wearbiliFontFamily,
                                            fontWeight = FontWeight.Normal,
                                            textAlign = TextAlign.Center,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                            Divider(color = Color(148, 148, 148, 204))
                            Column {
                                Row {
                                    Text(
                                        text = "设备音量",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .alpha(0.9f)
                                    )
                                }
                                androidx.compose.material3.Slider(
                                    value = currentVolume.toFloat(),
                                    onValueChange = {
                                        context.adjustVolume(it.roundToInt())
                                        currentVolume = it.roundToInt()
                                    },
                                    valueRange = 0.toFloat()..context.getMaxVolume().toFloat(),
                                    colors = SliderDefaults.colors(
                                        activeTrackColor = BilibiliPink,
                                        thumbColor = Color.White
                                    ),
                                    modifier = Modifier.offset(y = (-6).dp),
                                    thumb = {
                                        SliderDefaults.Thumb(
                                            interactionSource = MutableInteractionSource(),
                                            thumbSize = DpSize(12.dp, 12.dp),
                                            modifier = Modifier
                                                .offset(
                                                    y = 4.dp,
                                                    x = 2.dp
                                                ),

                                            colors = SliderDefaults.colors(
                                                thumbColor = Color.White
                                            )
                                        )
                                    })
                            }
                        }
                    }
                }
            }
        }
        if (viewModel.currentStat == PlayerStats.Loading) {
            Text(
                text = viewModel.loadingMessage,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomStart)
                    .alpha(0.8f),
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = wearbiliFontFamily,
                fontWeight = FontWeight.Normal,
                //textAlign = TextAlign.Center
            )
        }

    }
}

private fun Activity.rotateScreen() {
    when (requestedOrientation) {
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> {
            requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            /*viewModel.videoResolution.value?.let {
                resizeVideo(it.first, it.second)
            }*/
        }

        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> {
            requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            /*viewModel.videoResolution.value?.let {
                resizeVideo(it.second, it.first)
            }*/
        }

        ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED -> {
            requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            /*viewModel.videoResolution.value?.let {
                resizeVideo(it.first, it.second)
            }*/

        }

        else -> {}
    }
}

fun Context.getMaxVolume(): Int {
    val mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
}

fun Context.getCurrentVolume(): Int {
    val mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
}

fun Context.adjustVolume(value: Int) {
    //获取系统的Audio管理者
    val mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value, 0)
}