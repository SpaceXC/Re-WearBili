package cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.os.Build
import android.util.Log
import android.view.SurfaceView
import android.view.TextureView
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.BrightnessLow
import androidx.compose.material.icons.outlined.Cast
import androidx.compose.material.icons.outlined.FitScreen
import androidx.compose.material.icons.outlined.ScreenRotation
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Subtitles
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.style.TextAlign.Companion.Start
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.viewinterop.AndroidView
import cn.spacexc.wearbili.common.domain.log.TAG
import cn.spacexc.wearbili.common.domain.time.secondToTime
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.R.drawable
import cn.spacexc.wearbili.remake.app.player.cast.discover.DeviceDiscoverActivity
import cn.spacexc.wearbili.remake.app.player.cast.discover.PARAM_DLNA_VIDEO_NAME
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.rememberDanmakuCanvasState
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.ui.DanmakuCanvas
import cn.spacexc.wearbili.remake.app.settings.LocalConfiguration
import cn.spacexc.wearbili.remake.app.settings.SettingsManager
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.GradientSlider
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.WearBiliAnimatedContent
import cn.spacexc.wearbili.remake.common.ui.WearBiliAnimatedVisibility
import cn.spacexc.wearbili.remake.common.ui.clickAlpha
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.isRound
import cn.spacexc.wearbili.remake.common.ui.rememberMutableInteractionSource
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateColorAsState
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateDpAsState
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateFloatAsState
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimatedContentSize
import cn.spacexc.wearbili.remake.proto.settings.copy
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import net.engawapg.lib.zoomable.rememberZoomState
import kotlin.math.roundToInt

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
    data object Main : VideoPlayerPages(0)
    data object Settings : VideoPlayerPages(1)
    data object DanmakuSettings : VideoPlayerPages(2)
}

enum class VideoPlayerSurfaceRatio(val ratioName: String) {
    FitIn("适应"),
    FillMax("填充"),
    SixteenByNine("16:9"),
    NineBySixteen("9:16"),
    FourByThree("4:3"),
    ThreeByFour("3:4")
}


/*@UnstableApi*/
@Composable
@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class, ExperimentalTextApi::class, ExperimentalFoundationApi::class
)    //不要删掉这个OptIn!!!!!!!!!!!!!!!!!!灰色的也别删掉!!!!!!!!!!!!
//如果不小心删掉：ExperimentalAnimationApi::class
fun Activity.Media3PlayerScreen(
    viewModel: Media3VideoPlayerViewModel,
    displaySurface: VideoDisplaySurface,
    isCacheVideo: Boolean,
    context: Context,
    isBangumi: Boolean,
    onBack: () -> Unit
) {
    //region variables
    val configuration = LocalConfiguration.current
    val localDensity = LocalDensity.current
    val zoomState = rememberZoomState()
    var screenSize by remember {
        mutableStateOf(Size(1f, 1f))
    }
    var playerSurfaceSize by remember {
        mutableStateOf(Size(1f, 1f))
    }
    val currentSubtitleText by viewModel.currentSubtitleText.collectAsState(initial = null)
    val currentPlayerPosition by viewModel.currentPlayProgress.collectAsState(initial = 0)
    var currentPlayerSurfaceRatio: VideoPlayerSurfaceRatio by remember {
        mutableStateOf(VideoPlayerSurfaceRatio.FitIn)
    }
    var controllerProgressColumnHeight by remember {
        mutableStateOf(0.dp)
    }
    var controllerTitleColumnHeight by remember {
        mutableStateOf(0.dp)
    }
    var draggedProgress by remember {
        mutableLongStateOf(0L)
    }
    var isDraggingProgress by remember {
        mutableStateOf(false)
    }
    var dragSensibility by remember {
        mutableFloatStateOf(60f)
    }
    var playBackSpeed by remember {
        mutableIntStateOf(100)
    }
    var currentVolume by remember {
        mutableFloatStateOf(getCurrentVolume().toFloat())
    }
    var dragVolume by remember {
        mutableFloatStateOf(0f)
    }
    var isAdjustingVolume by remember {
        mutableStateOf(false)
    }
    //val animatedCurrentVolume by wearBiliAnimateFloatAsState(targetValue = dragVolume)
    val volumeDraggableState = rememberDraggableState(onDelta = {
        Log.d(TAG, "adjustVolume: $it")
        val dragValue = -it //下正上负
        val validScreenHeight = screenSize.height * 0.9f
        val offset = dragValue / validScreenHeight * 20 //一通乱试试出来的
        if (dragVolume + offset < 0) {
            dragVolume = 0f
        } else if (dragVolume + offset > getMaxVolume()) {
            dragVolume = getMaxVolume().toFloat()
        } else {
            dragVolume += offset//.toInt()
        }
        adjustVolume(dragVolume.toInt())
        currentVolume = dragVolume
    })
    val progressDraggableState = rememberDraggableState(onDelta = {
        val duration =
            if (isCacheVideo) viewModel.cachePlayer.duration else viewModel.httpPlayer.duration
        if (draggedProgress + (it * dragSensibility).toLong() < 0) {
            draggedProgress = 0
        } else if (draggedProgress + (it * dragSensibility).toLong() > duration) {
            draggedProgress = duration
        } else {
            draggedProgress += (it * dragSensibility).toLong()
        }
    })
    val progressBarThumbScale by wearBiliAnimateFloatAsState(
        targetValue = if (isDraggingProgress) 1.5f else 1f
    )
    val roundScreenControllerAlpha by animateIntAsState(
        targetValue = if (isRound() && viewModel.isVideoControllerVisible) 127/*255/2*/ else 0,
        label = ""
    )
    val subtitleOffset by wearBiliAnimateDpAsState(
        targetValue = if (viewModel.isVideoControllerVisible) controllerProgressColumnHeight - 14.dp else if (viewModel.videoChapters.isNotEmpty()) 18.dp else 6.dp,
    )  //18:视频章节字幕条高度  6:普通进度条
    val dragIndicatorOffset by wearBiliAnimateDpAsState(
        targetValue = if (viewModel.isVideoControllerVisible) controllerTitleColumnHeight else 8.dp,
    )
    val subtitlePadding by wearBiliAnimateDpAsState(
        targetValue = if (viewModel.isVideoControllerVisible) 0.dp else 6.dp,
    )
    var currentPage: VideoPlayerPages by remember {
        mutableStateOf(VideoPlayerPages.Main)
    }
    val backgroundColor by wearBiliAnimateColorAsState(
        targetValue = if (currentPage == VideoPlayerPages.Main) Color.Transparent else Color(
            0,
            0,
            0,
            204
        )
    )

    val danmakuCanvasState =
        rememberDanmakuCanvasState { if (isCacheVideo) viewModel.cachePlayer.currentPosition else viewModel.httpPlayer.currentPosition }
    val textMeasurer = rememberTextMeasurer()
    var isNormalDanmakuVisible by remember {
        mutableStateOf(configuration.danmakuPlayerSettings.isNormalDanmakuEnabled)
    }
    var isAdvanceDanmakuVisible by remember {
        mutableStateOf(configuration.danmakuPlayerSettings.isAdvanceDanmakuEnabled)
    }
    val danmakuButtonColor by wearBiliAnimateColorAsState(
        targetValue = if (isNormalDanmakuVisible) BilibiliPink else Color(
            38,
            38,
            38,
            255
        )
    )
    val advanceDanmakuButtonColor by wearBiliAnimateColorAsState(
        targetValue = if (isAdvanceDanmakuVisible) BilibiliPink else Color(
            38,
            38,
            38,
            255
        )
    )

    var danmakuCanvasAlpha by remember {
        mutableFloatStateOf(configuration.danmakuPlayerSettings.alpha)
    }
    var danmakuCanvasDisplayPercent by remember {
        mutableFloatStateOf(configuration.danmakuPlayerSettings.displayArea)
    }
    var danmakuTextScale by remember {
        mutableFloatStateOf(configuration.danmakuPlayerSettings.fontScale)
    }
    var danmakuBlockLevel by remember {
        mutableIntStateOf(configuration.danmakuPlayerSettings.blockLevel)
    }
    //endregion

    //region For Danmaku
    LaunchedEffect(
        danmakuCanvasAlpha,
        danmakuCanvasDisplayPercent,
        danmakuTextScale,
        danmakuBlockLevel,
        isNormalDanmakuVisible,
        isAdvanceDanmakuVisible
    ) {
        SettingsManager.updateConfiguration {
            copy {
                danmakuPlayerSettings = danmakuPlayerSettings.copy {
                    alpha = danmakuCanvasAlpha
                    displayArea = danmakuCanvasDisplayPercent
                    fontScale = danmakuTextScale
                    blockLevel = blockLevel
                    isNormalDanmakuEnabled = isNormalDanmakuVisible
                    isAdvanceDanmakuEnabled = isAdvanceDanmakuVisible
                }
            }
        }
    }
    LaunchedEffect(key1 = viewModel.danmakuList, block = {
        danmakuCanvasState.setDanmakuList(newDanmakuList = viewModel.danmakuList)
    })
    LaunchedEffect(key1 = viewModel.imageDanmakus, block = {
        danmakuCanvasState.setDanmakuList(imageDanmakus = viewModel.imageDanmakus)
    })
    LaunchedEffect(key1 = viewModel.commandDanmakus, block = {
        danmakuCanvasState.setDanmakuList(commandDanmakus = viewModel.commandDanmakus)
    })
    LaunchedEffect(key1 = viewModel.currentStat, block = {
        when (viewModel.currentStat) {
            PlayerStats.Loading -> {
                danmakuCanvasState.pause()
            }

            PlayerStats.Playing -> {
                danmakuCanvasState.start()
            }

            PlayerStats.Buffering -> {
                danmakuCanvasState.pause()
            }

            PlayerStats.Paused -> {
                danmakuCanvasState.pause()
            }

            PlayerStats.Finished -> {
                danmakuCanvasState.pause()
            }
        }
    })
    //endregion

    LaunchedEffect(key1 = currentPage) {
        zoomState.reset()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .onSizeChanged {
                screenSize = it.toSize()
            }
        /*.zoomable(
            zoomState = zoomState,
            zoomEnabled = currentPage == VideoPlayerPages.Main && viewModel.currentStat != PlayerStats.Loading,
            enableOneFingerZoom = false,
            onDoubleTap = {}
        )*/
    ) {
        //region player surface
        Box(
            modifier = when (currentPlayerSurfaceRatio) {
                VideoPlayerSurfaceRatio.FitIn -> Modifier.aspectRatio(viewModel.videoPlayerAspectRatio)
                VideoPlayerSurfaceRatio.FillMax -> Modifier.fillMaxSize()
                VideoPlayerSurfaceRatio.FourByThree -> Modifier.aspectRatio(4f / 3f)
                VideoPlayerSurfaceRatio.NineBySixteen -> Modifier.aspectRatio(9f / 16f)
                VideoPlayerSurfaceRatio.SixteenByNine -> Modifier.aspectRatio(16f / 9f)
                VideoPlayerSurfaceRatio.ThreeByFour -> Modifier.aspectRatio(3f / 4f)
            }
                .animateContentSize()
                .align(Alignment.Center)
                .onSizeChanged {
                    playerSurfaceSize = it.toSize()
                }
        ) {
            when (displaySurface) {
                VideoDisplaySurface.TEXTURE_VIEW -> {
                    AndroidView(factory = { TextureView(it) }) { textureView ->
                        //viewModel.httpPlayer.setDisplay(textureView.surfaceTexture.h)
                        //viewModel.cachePlayer.setVideoTextureView(textureView)
                    }
                }

                VideoDisplaySurface.SURFACE_VIEW -> {
                    AndroidView(factory = { SurfaceView(it) }) { surfaceView ->
                        /*surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
                            override fun surfaceCreated(holder: SurfaceHolder) {
                                viewModel.httpPlayer.setDisplay(holder)
                            }

                            override fun surfaceChanged(
                                holder: SurfaceHolder,
                                p0: Int,
                                p1: Int,
                                p2: Int
                            ) {
                                viewModel.httpPlayer.setDisplay(holder)
                            }
                            override fun surfaceDestroyed(p0: SurfaceHolder) {}
                        })*/
                        viewModel.httpPlayer.setDisplay(surfaceView.holder)
                        viewModel.cachePlayer.setDisplay(surfaceView.holder)
                    }
                }
            }
        }
        //endregion

        //region danmaku surface
        DanmakuCanvas(
            state = danmakuCanvasState,
            textMeasurer = textMeasurer,
            playSpeed = playBackSpeed / 100f,
            videoAspectRatio = viewModel.videoPlayerAspectRatio,
            displayAreaPercent = danmakuCanvasDisplayPercent,
            blockLevel = danmakuBlockLevel,
            modifier = Modifier.alpha(danmakuCanvasAlpha),
            textScale = danmakuTextScale,
            uploaderAvatarUrl = viewModel.videoInfo?.data?.owner?.face ?: "",
            isAdvanceDanmakuVisible = isAdvanceDanmakuVisible,
            isNormalDanmakuVisible = isNormalDanmakuVisible,
            videoDisplaySurfaceSize = playerSurfaceSize,
            isDebug = false,
            displayFrameRate = false
        )

        //endregion
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            WearBiliAnimatedContent(targetState = currentPage, transitionSpec = {
                if (targetState.weight > initialState.weight) {
                    slideInHorizontally { height -> height } + fadeIn() togetherWith
                            slideOutHorizontally { height -> -height } + fadeOut()
                } else {
                    slideInHorizontally { height -> -height } + fadeIn() togetherWith
                            slideOutHorizontally { height -> height } + fadeOut()
                }
            }, label = "") { currentVideoPage ->
                when (currentVideoPage) {
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
                                        .fillMaxWidth(),
                                    textAlign = if (isRound()) Center else Start
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBackIosNew,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            }

                            if (viewModel.isReady) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0, 0, 0, roundScreenControllerAlpha))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .draggable(
                                                state = progressDraggableState,
                                                orientation = Orientation.Horizontal,
                                                startDragImmediately = false,
                                                onDragStarted = {
                                                    draggedProgress = currentPlayerPosition
                                                    //viewModel.isVideoControllerVisible = true
                                                    isDraggingProgress = true
                                                },
                                                onDragStopped = {
                                                    if (isCacheVideo) {
                                                        viewModel.cachePlayer.seekTo(draggedProgress)
                                                    } else {
                                                        viewModel.httpPlayer.seekTo(draggedProgress)
                                                    }

                                                    danmakuCanvasState.pause()
                                                    danmakuCanvasState.seekTo(
                                                        time = draggedProgress,
                                                        textMeasurer = textMeasurer,
                                                        displayWidth = playerSurfaceSize.width.toInt(),
                                                        textScale = danmakuTextScale,
                                                    )
                                                    viewModel.currentStat = PlayerStats.Buffering
                                                    isDraggingProgress = false
                                                }
                                            )
                                            .draggable(
                                                state = volumeDraggableState,
                                                orientation = Orientation.Vertical,
                                                startDragImmediately = false,
                                                onDragStarted = {
                                                    dragVolume = getCurrentVolume().toFloat()
                                                    isAdjustingVolume = true
                                                },
                                                onDragStopped = {
                                                    isAdjustingVolume = false
                                                }
                                            )
                                            .pointerInput(Unit) {
                                                detectTapGestures(onTap = {
                                                    viewModel.isVideoControllerVisible =
                                                        !viewModel.isVideoControllerVisible
                                                }, onDoubleTap = {
                                                    if (isCacheVideo) {
                                                        if (viewModel.cachePlayer.isPlaying) viewModel.cachePlayer.pause() else viewModel.cachePlayer.start()
                                                    } else {
                                                        if (viewModel.httpPlayer.isPlaying) viewModel.httpPlayer.pause() else viewModel.httpPlayer.start()
                                                    }
                                                })
                                            }
                                    ) {
                                        var titleRowHeight by remember {
                                            mutableStateOf(0.dp)
                                        }
                                        WearBiliAnimatedVisibility(
                                            visible = viewModel.isVideoControllerVisible,
                                            enter = slideInVertically() + fadeIn(),
                                            exit = slideOutVertically() + fadeOut(),
                                            modifier = Modifier.onSizeChanged {
                                                controllerTitleColumnHeight =
                                                    with(localDensity) { it.height.toDp() }
                                            }
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
                                                        interactionSource = rememberMutableInteractionSource(),
                                                        indication = null,
                                                        onClick = onBack
                                                    ),
                                                verticalAlignment = CenterVertically
                                            ) {
                                                if (!isRound()) {
                                                    Icon(
                                                        imageVector = Icons.Default.ArrowBackIosNew,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(titleRowHeight * 0.55f)
                                                    )
                                                    Spacer(modifier = Modifier.width(2.dp))
                                                }
                                                Column(
                                                    horizontalAlignment = if (isRound()) Alignment.CenterHorizontally else Alignment.Start
                                                ) {
                                                    if (isRound()) {
                                                        Spacer(modifier = Modifier.height(10.dp))
                                                    }
                                                    Text(
                                                        text = (if (isCacheVideo) viewModel.cacheVideoInfo?.videoName else viewModel.videoInfo?.data?.title)
                                                            ?: "",
                                                        color = Color.White,
                                                        fontSize = 13.sp,
                                                        fontFamily = wearbiliFontFamily,
                                                        fontWeight = FontWeight.Medium,
                                                        maxLines = 1,
                                                        //overflow = TextOverflow.Ellipsis,
                                                        modifier = Modifier
                                                            .basicMarquee()
                                                            .padding(horizontal = if (isRound()) TitleBackgroundHorizontalPadding() else 0.dp)
                                                    )
                                                    Text(
                                                        text = if (isCacheVideo) "来自缓存的视频" else "${viewModel.onlineCount}人在看",
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
                                        WearBiliAnimatedVisibility(
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
                                                        viewModel.videoDuration.div(1000)
                                                            .secondToTime()
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
                                                        .fillMaxWidth(),
                                                    textAlign = if (isRound()) Center else Start
                                                )
                                                androidx.compose.material3.Slider(
                                                    value = if (isDraggingProgress) draggedProgress.toFloat() else currentPlayerPosition.toFloat(),
                                                    onValueChange = {
                                                        isDraggingProgress = true
                                                        draggedProgress = it.toLong()
                                                    },
                                                    onValueChangeFinished = {
                                                        if (isCacheVideo) {
                                                            viewModel.cachePlayer.seekTo(
                                                                draggedProgress
                                                            )
                                                        } else {
                                                            viewModel.httpPlayer.seekTo(
                                                                draggedProgress
                                                            )
                                                        }
                                                        danmakuCanvasState.pause()
                                                        danmakuCanvasState.seekTo(
                                                            time = draggedProgress,
                                                            textMeasurer = textMeasurer,
                                                            displayWidth = playerSurfaceSize.width.toInt(),
                                                            textScale = danmakuTextScale,
                                                        )
                                                        viewModel.currentStat =
                                                            PlayerStats.Buffering
                                                        isDraggingProgress = false
                                                    },
                                                    valueRange = 0f..viewModel.videoDuration.toFloat(),
                                                    colors = SliderDefaults.colors(
                                                        activeTrackColor = BilibiliPink
                                                    ),
                                                    thumb = {
                                                        /*SliderDefaults.Thumb(
                                                            interactionSource = rememberMutableInteractionSource(),
                                                            thumbSize = DpSize(12.dp, 12.dp),
                                                            modifier = Modifier
                                                                .offset(
                                                                    y = 4.dp,
                                                                    x = *//*-(4.dp / viewModel.player.duration.toFloat()) * currentPlayerPosition.toFloat() + 2.dp*//* 2.dp
                                                            )
                                                            .scale(progressBarThumbScale),   //别问这串公式怎么得出来的，问就是数学的力量
                                                        */
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
                                                        /*
                                                            colors = SliderDefaults.colors(
                                                                thumbColor = Color.White
                                                            )
                                                        )*/
                                                        Image(
                                                            painter = painterResource(id = drawable.img_progress_bar_thumb_little_tv),
                                                            contentDescription = null,
                                                            modifier = Modifier
                                                                .offset(
                                                                    y = 4.dp,
                                                                    x = 3.dp
                                                                )
                                                                .size(12.dp)
                                                                .scale(progressBarThumbScale)
                                                        )
                                                    },
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .scale(1.05f)
                                                        .offset(y = (9).dp)
                                                        .padding(horizontal = if (isRound()) TitleBackgroundHorizontalPadding() else 0.dp)
                                                )
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            start = 4.dp,
                                                            end = 4.dp,
                                                            bottom = 6.dp
                                                        ),
                                                    verticalAlignment = CenterVertically,
                                                    horizontalArrangement = if (isRound()) Arrangement.Center else Arrangement.Start
                                                ) {
                                                    if (!isRound()) {
                                                        Image(
                                                            painter = painterResource(id = if (if (isCacheVideo) viewModel.cachePlayer.isPlaying else viewModel.httpPlayer.isPlaying) drawable.img_pause_icon else drawable.img_play_icon),
                                                            contentDescription = null,
                                                            modifier = Modifier
                                                                //.clickVfx { if (viewModel.player.isPlaying) viewModel.player.pause() else viewModel.player.play() }
                                                                .clickable {
                                                                    if (isCacheVideo) {
                                                                        if (viewModel.cachePlayer.isPlaying) viewModel.cachePlayer.pause() else viewModel.cachePlayer.start()
                                                                    } else {
                                                                        if (viewModel.httpPlayer.isPlaying) viewModel.httpPlayer.pause() else viewModel.httpPlayer.start()
                                                                    }
                                                                }
                                                                .size(18.dp)

                                                        )
                                                        Spacer(modifier = Modifier.weight(1f))
                                                        /**
                                                         * MARK: 方屏：播放/暂停
                                                         */
                                                        /**
                                                         * MARK: 方屏：播放/暂停
                                                         */
                                                        /**
                                                         * MARK: 方屏：播放/暂停
                                                         */
                                                        /**
                                                         * MARK: 方屏：播放/暂停
                                                         */
                                                    }

                                                    PlayerQuickActionButton(
                                                        onClick = {
                                                            isAdvanceDanmakuVisible =
                                                                !isAdvanceDanmakuVisible
                                                        },
                                                        color = advanceDanmakuButtonColor
                                                    ) {
                                                        Icon(
                                                            painter = painterResource(id = drawable.icon_advance_danmaku),
                                                            contentDescription = null,
                                                            tint = Color.White,
                                                            modifier = Modifier
                                                                .size(14.dp)
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    //region player quick actions
                                                    PlayerQuickActionButton(
                                                        onClick = {
                                                            isNormalDanmakuVisible =
                                                                !isNormalDanmakuVisible
                                                        },
                                                        color = danmakuButtonColor
                                                    ) {
                                                        Icon(
                                                            painter = painterResource(id = drawable.icon_danmaku),
                                                            contentDescription = null,
                                                            tint = Color.White,
                                                            modifier = Modifier
                                                                .size(14.dp)
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(4.dp))


                                                    PlayerQuickActionButton(
                                                        onClick = ::rotateScreen,
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Outlined.ScreenRotation,
                                                            contentDescription = null,
                                                            tint = Color.White,
                                                            modifier = Modifier
                                                                .size(12.dp)
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(4.dp))

                                                    PlayerQuickActionButton(
                                                        onClick = {
                                                            currentPage = VideoPlayerPages.Settings
                                                        },
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Outlined.Settings,
                                                            contentDescription = null,
                                                            tint = Color.White,
                                                            modifier = Modifier
                                                                .size(12.dp)
                                                        )
                                                    }

                                                    //endregion
                                                }
                                                if (isRound()) {
                                                    Spacer(modifier = Modifier.height(10.dp))
                                                }
                                            }
                                        }

                                    }

                                    WearBiliAnimatedVisibility(
                                        visible = !viewModel.isVideoControllerVisible,
                                        enter = slideInVertically { it / 2 } + fadeIn(),
                                        exit = slideOutVertically { it / 2 } + fadeOut(),
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .fillMaxWidth()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .height(18.dp)
                                                .align(Alignment.BottomCenter)
                                        ) {
                                            LinearProgressIndicator(
                                                progress = (if (isDraggingProgress) draggedProgress.toFloat() else currentPlayerPosition.toFloat()) / viewModel.videoDuration.toFloat(),
                                                color = BilibiliPink.copy(0.5f),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(if (viewModel.videoChapters.isNotEmpty()) 18.dp else 6.dp)
                                                    .align(Alignment.BottomCenter),
                                                backgroundColor = BilibiliPink.copy(0.1f)
                                            )
                                            //视频章节
                                            if (viewModel.videoChapters.isNotEmpty()) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(18.dp)
                                                        .align(Alignment.BottomCenter)
                                                ) {
                                                    viewModel.videoChapters.forEachIndexed { index, chapter ->
                                                        val isAtCurrentChapter =
                                                            currentPlayerPosition in chapter.third * 1000..(chapter.third + chapter.first) * 1000
                                                        Text(
                                                            text = chapter.second,
                                                            color = Color.White,
                                                            modifier = Modifier
                                                                .align(CenterVertically)
                                                                .then(
                                                                    if (isAtCurrentChapter) Modifier.basicMarquee(
                                                                        animationMode = MarqueeAnimationMode.Immediately
                                                                    ) else Modifier
                                                                )
                                                                .alpha(0.7f)
                                                                .weight(chapter.first.toFloat())
                                                                .fillMaxWidth()
                                                                .clickable(
                                                                    interactionSource = rememberMutableInteractionSource(),
                                                                    indication = null
                                                                ) {
                                                                    if (isCacheVideo) {
                                                                        viewModel.cachePlayer.seekTo(
                                                                            chapter.third * 1000L
                                                                        )
                                                                    } else {
                                                                        viewModel.httpPlayer.seekTo(
                                                                            chapter.third * 1000L
                                                                        )
                                                                    }
                                                                    danmakuCanvasState.pause()
                                                                    danmakuCanvasState.seekTo(
                                                                        time = chapter.third * 1000L,
                                                                        textMeasurer = textMeasurer,
                                                                        displayWidth = playerSurfaceSize.width.toInt(),
                                                                        textScale = danmakuTextScale,
                                                                    )
                                                                    viewModel.currentStat =
                                                                        PlayerStats.Buffering
                                                                },
                                                            fontSize = 7.spx,
                                                            textAlign = Center,
                                                            fontFamily = wearbiliFontFamily,
                                                            maxLines = if (isAtCurrentChapter) 1 else 2,
                                                            overflow = if (isAtCurrentChapter) TextOverflow.Clip else TextOverflow.Ellipsis
                                                        )
                                                        if (index < viewModel.videoChapters.size - 1) {  //最后一个没有分割线
                                                            Box(
                                                                modifier = Modifier
                                                                    .height(10.dp)
                                                                    .width(1.dp)
                                                                    .background(
                                                                        BilibiliPink.copy(alpha = 0.5f)
                                                                    )
                                                                    .align(CenterVertically)
                                                            )   //divider
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (isRound()) {
                                        WearBiliAnimatedVisibility(
                                            visible = viewModel.isVideoControllerVisible,
                                            enter = fadeIn(),
                                            exit = fadeOut(),
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Image(
                                                    painter = painterResource(id = if (if (isCacheVideo) viewModel.cachePlayer.isPlaying else viewModel.httpPlayer.isPlaying) drawable.img_pause_icon else drawable.img_play_icon),
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        //.clickVfx { if (viewModel.player.isPlaying) viewModel.player.pause() else viewModel.player.play() }
                                                        .clickable {
                                                            if (isCacheVideo) {
                                                                if (viewModel.cachePlayer.isPlaying) viewModel.cachePlayer.pause() else viewModel.cachePlayer.start()
                                                            } else {
                                                                if (viewModel.httpPlayer.isPlaying) viewModel.httpPlayer.pause() else viewModel.httpPlayer.start()
                                                            }
                                                        }
                                                        .size(18.dp)

                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            //region dragging progress indicator
                            WearBiliAnimatedVisibility(
                                visible = isDraggingProgress,
                                enter = scaleIn() + fadeIn() + slideInVertically(),
                                exit = scaleOut() + fadeOut() + slideOutVertically(),
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .offset {
                                        IntOffset(x = 0, y = dragIndicatorOffset.roundToPx())
                                    }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(360.dp))
                                        .background(
                                            Color(18, 18, 18)
                                        )
                                        .padding(vertical = 6.dp, horizontal = 24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val currentChapter =
                                        viewModel.videoChapters.find { draggedProgress in it.third * 1000..(it.third + it.first) * 1000 }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "${
                                                (if (isDraggingProgress) draggedProgress else currentPlayerPosition).div(
                                                    1000
                                                ).secondToTime()
                                            }/${
                                                viewModel.videoDuration.div(1000).secondToTime()
                                            }", modifier = Modifier
                                                .wearBiliAnimatedContentSize(),
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontFamily = wearbiliFontFamily,
                                            fontWeight = FontWeight.Medium,
                                            textAlign = Center
                                        )
                                        currentChapter?.let {
                                            Text(
                                                text = currentChapter.second, modifier = Modifier
                                                    .wearBiliAnimatedContentSize()
                                                    .alpha(0.7f),
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontFamily = wearbiliFontFamily,
                                                fontWeight = FontWeight.Medium,
                                                textAlign = Center
                                            )
                                        }
                                    }
                                }

                            }
                            WearBiliAnimatedVisibility(
                                visible = isAdjustingVolume,
                                enter = scaleIn() + fadeIn() + slideInVertically(),
                                exit = scaleOut() + fadeOut() + slideOutVertically(),
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .offset(y = dragIndicatorOffset)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(360.dp))
                                        .background(
                                            Color(18, 18, 18)
                                        )
                                        .padding(vertical = 6.dp, horizontal = 24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        IconText(
                                            text = "${(dragVolume / getMaxVolume().toFloat() * 100).toInt()}%",
                                            modifier = Modifier
                                                .wearBiliAnimatedContentSize(),
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontFamily = wearbiliFontFamily,
                                            fontWeight = FontWeight.Medium,
                                            textAlign = Center,
                                            icon = {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                                    contentDescription = null, tint = Color.White
                                                )
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Box {
                                            LinearProgressIndicator(
                                                progress = 1f,
                                                modifier = Modifier
                                                    .width(80.dp)
                                                    .clip(CircleShape),
                                                color = Color(
                                                    199,
                                                    199,
                                                    199,
                                                    77
                                                )
                                            )
                                            LinearProgressIndicator(
                                                progress = (dragVolume / getMaxVolume()),
                                                modifier = Modifier
                                                    .width(80.dp)
                                                    .clip(CircleShape),
                                                color = BilibiliPink,
                                                backgroundColor = Color.Transparent
                                            )
                                        }
                                    }
                                }

                            }

                            //endregion

                            //region subtitle
                            WearBiliAnimatedVisibility(
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
                                            start = TitleBackgroundHorizontalPadding() - 3.dp,
                                            end = TitleBackgroundHorizontalPadding() - 3.dp
                                        )
                                        .background(
                                            color = Color(49, 47, 47, 153),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .padding(6.dp)
                                        .align(Alignment.BottomCenter)
                                        .wearBiliAnimatedContentSize(),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontFamily = wearbiliFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = Center
                                )
                            }

                            //endregion

                            //region loading indicator
                            if (viewModel.currentStat == PlayerStats.Buffering) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .clickVfx(onLongClick = {
                                            this@Media3PlayerScreen.finish()
                                        }),
                                    color = BilibiliPink
                                )
                            }
                            if (viewModel.currentStat == PlayerStats.Loading) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(drawable.img_loading_little_tv)
                                        .crossfade(true).build(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .fillMaxWidth(0.5f),
                                    imageLoader = ImageLoader(LocalContext.current).newBuilder()
                                        .components {
                                            if (Build.VERSION.SDK_INT >= 28) {
                                                add(ImageDecoderDecoder.Factory())
                                            } else {
                                                add(GifDecoder.Factory())
                                            }
                                        }.build()
                                )
                            }
                            //endregion
                        }
                    }

                    VideoPlayerPages.Settings -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(TitleBackgroundHorizontalPadding()),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            //region setting items
                            IconText(
                                text = if (isRound()) "播放选项" else "",
                                color = Color.White,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .clickable { currentPage = VideoPlayerPages.Main }
                                    .fillMaxWidth()
                                    .offset(if (isRound()) (-7).dp else 0.dp),
                                textAlign = if (isRound()) Center else Start,
                                fontWeight = FontWeight.Bold
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBackIosNew,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                            if (!isRound()) {
                                androidx.compose.material3.Text(
                                    text = "播放选项",
                                    color = Color.White,
                                    fontFamily = wearbiliFontFamily,
                                    fontSize = 20.spx,
                                    fontWeight = FontWeight.Bold,
                                )
                            }

                            PlayerSettingActionItem(
                                name = "弹幕选项",
                                description = "调整弹幕显示设置",
                                icon = {
                                    Icon(
                                        painter = painterResource(id = drawable.icon_danmaku),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }) {
                                currentPage = VideoPlayerPages.DanmakuSettings
                            }

                            PlayerSetting(itemIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Speed,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }, settingName = "播放速度") {
                                for (i in 25 until 100 step 25) {
                                    PlayerSettingItem(
                                        text = i.toFloat().div(100).toString() + "x",
                                        isSelected = playBackSpeed == i
                                    ) {
                                        viewModel.httpPlayer.setSpeed(i.toFloat().div(100))
                                        playBackSpeed = i
                                        //danmakuCanvasState.timer.setSpeed(i / 100f)
                                    }
                                }
                                for (i in 100..300 step 50) {
                                    PlayerSettingItem(
                                        text = i.toFloat().div(100).toString() + "x",
                                        isSelected = playBackSpeed == i
                                    ) {
                                        viewModel.httpPlayer.setSpeed(i.toFloat().div(100))
                                        playBackSpeed = i
                                        //danmakuCanvasState.timer.setSpeed(i / 100f)
                                    }
                                }
                            }

                            PlayerSetting(itemIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Subtitles,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }, settingName = "CC字幕") {
                                PlayerSettingItem(
                                    text = "关闭",
                                    isSelected = viewModel.currentSubtitleLanguage == null
                                ) {
                                    viewModel.currentSubtitleLanguage =
                                        null
                                }
                                viewModel.subtitleList.forEach {
                                    PlayerSettingItem(
                                        text = it.value.subtitleLanguage,
                                        isSelected = viewModel.currentSubtitleLanguage == it.key
                                    ) {
                                        viewModel.currentSubtitleLanguage =
                                            it.key
                                    }
                                }
                            }

                            PlayerSetting(itemIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.FitScreen,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }, settingName = "播放器比例") {
                                VideoPlayerSurfaceRatio.entries.forEach { ratio ->
                                    PlayerSettingItem(
                                        text = ratio.ratioName,
                                        isSelected = currentPlayerSurfaceRatio == ratio
                                    ) {
                                        currentPlayerSurfaceRatio = ratio
                                    }
                                }
                                viewModel.subtitleList.forEach {

                                }
                            }

                            /*PlayerSetting(itemIcon = {
                                Icon(
                                    imageVector = Icons.Default.BrightnessLow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }, settingName = "滑动灵敏度") {
                                for (i in 60..180 step 30) {
                                    PlayerSettingItem(
                                        text = i.toString(),
                                        isSelected = dragSensibility == i
                                    ) {
                                        dragSensibility = i
                                    }
                                }
                            }*/
                            PlayerSliderSetting(
                                itemIcon = {
                                    Icon(
                                        imageVector = Icons.Default.BrightnessLow,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        tint = Color.White
                                    )
                                },
                                settingName = "滑动灵敏度",
                                sliderValue = dragSensibility,
                                displayedSliderValue = dragSensibility.toInt().toString(),
                                sliderValueRange = 60f..180f
                            ) {
                                dragSensibility = it
                            }
                            PlayerSettingActionItem(
                                name = "投屏",
                                description = "投射视频到DLNA设备",
                                icon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Cast,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }) {
                                startActivity(
                                    Intent(
                                        this@Media3PlayerScreen,
                                        DeviceDiscoverActivity::class.java
                                    ).apply {
                                        putExtra(
                                            cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_CID,
                                            viewModel.videoInfo?.data?.cid
                                        )
                                        putExtra(
                                            PARAM_DLNA_VIDEO_NAME,
                                            viewModel.videoInfo?.data?.title
                                        )
                                        putExtra(PARAM_IS_BANGUMI, isBangumi)
                                    })
                                finish()
                            }

                            PlayerSliderSetting(
                                itemIcon = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.VolumeUp,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        tint = Color.White
                                    )
                                },
                                settingName = "设备音量",
                                sliderValue = currentVolume,
                                displayedSliderValue = "${(currentVolume / getMaxVolume().toFloat() * 100).toInt()}%",
                                sliderValueRange = 0f..getMaxVolume().toFloat()
                            ) {
                                context.adjustVolume(it.roundToInt())
                                currentVolume = it
                            }
                            /*Column {
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
                                    value = currentVolume,
                                    onValueChange = {
                                        context.adjustVolume(it.roundToInt())
                                        currentVolume = it
                                    },
                                    valueRange = 0f..getMaxVolume().toFloat(),
                                    colors = SliderDefaults.colors(
                                        activeTrackColor = BilibiliPink,
                                        thumbColor = Color.White
                                    ),
                                    modifier = Modifier.offset(y = (-6).dp),
                                    thumb = {
                                        SliderDefaults.Thumb(
                                            interactionSource = rememberMutableInteractionSource(),
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
                                    }
                                )
                            }*/
                            //endregion
                        }
                    }

                    VideoPlayerPages.DanmakuSettings -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(TitleBackgroundHorizontalPadding()),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            //region setting items
                            IconText(
                                text = if (isRound()) "弹幕选项" else "",
                                color = Color.White,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .clickable { currentPage = VideoPlayerPages.Settings }
                                    .fillMaxWidth()
                                    .offset(if (isRound()) (-7).dp else 0.dp),
                                textAlign = if (isRound()) Center else Start,
                                fontWeight = FontWeight.Bold
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBackIosNew,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                            if (!isRound()) {
                                androidx.compose.material3.Text(
                                    text = "弹幕选项",
                                    color = Color.White,
                                    fontFamily = wearbiliFontFamily,
                                    fontSize = 20.spx,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            PlayerSliderSetting(
                                itemIcon = null,
                                settingName = "不透明度",
                                sliderValue = danmakuCanvasAlpha,
                                displayedSliderValue = "${danmakuCanvasAlpha.times(100).toInt()}%",
                                sliderValueRange = 0f..1f
                            ) {
                                danmakuCanvasAlpha = it
                            }
                            PlayerSliderSetting(
                                itemIcon = null,
                                settingName = "显示区域",
                                sliderValue = danmakuCanvasDisplayPercent,
                                displayedSliderValue = "${
                                    danmakuCanvasDisplayPercent.times(100).toInt()
                                }%",
                                sliderValueRange = 0f..1f
                            ) {
                                danmakuCanvasDisplayPercent = it
                            }
                            PlayerSliderSetting(
                                itemIcon = null,
                                settingName = "字体缩放",
                                sliderValue = danmakuTextScale,
                                displayedSliderValue = "${danmakuTextScale.times(100).toInt()}%",
                                sliderValueRange = 0.5f..2f
                            ) {
                                danmakuTextScale = it
                            }
                            PlayerSliderSetting(
                                itemIcon = null,
                                settingName = "屏蔽等级",
                                sliderValue = danmakuBlockLevel.toFloat(),
                                displayedSliderValue = "等级${danmakuBlockLevel}",
                                sliderValueRange = 0f..10f
                            ) {
                                danmakuBlockLevel = it.roundToInt()
                            }
                            //endregion
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
                    .alpha(0.8f)
                    .fillMaxWidth(),
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = wearbiliFontFamily,
                fontWeight = FontWeight.Normal,
                textAlign = if (isRound()) Center else Start
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

@Composable
fun PlayerQuickActionButton(
    onClick: () -> Unit,
    color: Color = Color(
        38,
        38,
        38,
        255
    ),
    icon: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .background(
                color = color,
                shape = CircleShape
            )
            .size(22.dp)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}

//region region: volume
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
//endregion

//region region: Player settings
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlayerSetting(
    itemIcon: @Composable () -> Unit,
    settingName: String,
    settingItems: @Composable RowScope.() -> Unit
) {
    Card(
        innerPaddingValues = PaddingValues(0.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            IconText(
                text = settingName, fontFamily = wearbiliFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.spx,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp, top = 16.dp, bottom = 12.dp)
            ) {
                itemIcon()
            }
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 0.3f.dp,
                        shape = RectangleShape,
                        brush = Brush.linearGradient(
                            listOf(
                                Color(84, 84, 84, 255),
                                Color.Transparent
                            )
                        )
                    )
                    .background(color = Color(38, 38, 38, 77))
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),

                ) {
                settingItems()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlayerSliderSetting(
    itemIcon: (@Composable () -> Unit)?,
    settingName: String,
    sliderValue: Float,
    displayedSliderValue: String,
    sliderValueRange: ClosedFloatingPointRange<Float>,
    onSliderValueChanged: (Float) -> Unit
) {
    Card(
        innerPaddingValues = PaddingValues(0.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 14.dp, end = 14.dp, top = 16.dp, bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = CenterVertically
            ) {
                IconText(
                    text = settingName, fontFamily = wearbiliFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.spx,
                    color = Color.White,
                    spacingWidth = if (itemIcon == null) 0.sp else 2.spx,
                    icon = itemIcon
                )
                Spacer(modifier = Modifier.width(6.dp))

                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = displayedSliderValue, fontFamily = wearbiliFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.spx,
                    color = Color.White,
                    modifier = Modifier.alpha(0.7f)
                )
            }
            GradientSlider(
                value = sliderValue,
                range = sliderValueRange,
                onValueChanged = onSliderValueChanged
            )
        }
    }
}

@Composable
fun PlayerSettingItem(
    text: String,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    val backgroundColor by wearBiliAnimateColorAsState(
        targetValue = if (isSelected) BilibiliPink else Color(
            41,
            41,
            41,
            255
        )
    )
    val textAlpha by wearBiliAnimateFloatAsState(targetValue = if (isSelected) 1f else 0.5f)
    Box(
        modifier = Modifier
            .padding(bottom = 3.dp, top = 3.dp)
            .clip(RoundedCornerShape(30))
            .background(backgroundColor)
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .clickAlpha { onSelected() },
        //.fillMaxSize()
        //.offset(y = (1).dp)
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 11.spx,
            fontFamily = wearbiliFontFamily,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            textAlign = Center,
            modifier = Modifier.alpha(textAlpha)
        )
    }
}

@Composable
fun PlayerSettingActionItem(
    name: String,
    description: String,
    icon: @Composable () -> Unit,
    action: () -> Unit
) {
    val localDensity = LocalDensity.current
    var textHeight by remember {
        mutableStateOf(0.dp)
    }
    Card(shape = RoundedCornerShape(15.dp), onClick = action) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp), verticalAlignment = CenterVertically
        ) {
            Box(modifier = Modifier.size(textHeight * 0.6f)) {
                icon()
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column(modifier = Modifier.onSizeChanged {
                textHeight = with(localDensity) {
                    it.height.toDp()
                }
            }) {
                androidx.compose.material3.Text(
                    text = name,
                    color = Color.White,
                    fontFamily = wearbiliFontFamily,
                    fontSize = 13.spx,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                androidx.compose.material3.Text(
                    text = description,
                    color = Color.White,
                    fontFamily = wearbiliFontFamily,
                    fontSize = 10.spx,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.alpha(0.7f)
                )
            }
        }
    }
}
//endregion