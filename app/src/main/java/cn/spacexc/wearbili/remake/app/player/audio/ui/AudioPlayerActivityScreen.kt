package cn.spacexc.wearbili.remake.app.player.audio.ui

import BiliTextIcon
import SegoeTextIcon
import android.content.Context
import android.graphics.drawable.VectorDrawable
import android.media.AudioManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.outlined.OndemandVideo
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material.icons.outlined.Subtitles
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import appendBiliIcon
import biliIconFontFamily
import cn.spacexc.wearbili.common.domain.color.parseColor
import cn.spacexc.wearbili.common.domain.time.secondToTime
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.player.audio.AudioPlayerManager
import cn.spacexc.wearbili.remake.app.player.audio.AudioPlayerService
import cn.spacexc.wearbili.remake.app.player.audio.ui.lyrics.LyricsView
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.IjkVideoPlayerScreen
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PlayerSetting
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PlayerSettingActionItem
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PlayerSettingItem
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PlayerSliderSetting
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.adjustVolume
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.getCurrentVolume
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.getMaxVolume
import cn.spacexc.wearbili.remake.app.settings.LocalConfiguration
import cn.spacexc.wearbili.remake.app.settings.experimantal.EXPERIMENTAL_FLOATING_SUBTITLE
import cn.spacexc.wearbili.remake.app.settings.experimantal.getActivatedExperimentalFunctions
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.ui.ArrowTitleBackgroundWithCustomBackground
import cn.spacexc.wearbili.remake.common.ui.BiliImage
import cn.spacexc.wearbili.remake.common.ui.GradientSlider
import cn.spacexc.wearbili.remake.common.ui.HorizontalPagerIndicator
import cn.spacexc.wearbili.remake.common.ui.clickAlpha
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateDpAsState
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateFloatAsState
import coil.transform.CustomBlurTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import unicodeToString
import kotlin.math.roundToInt

/**
 * Created by XC-Qan on 2023/10/15.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

data class AudioPlayerActivityScreen(
    val videoIdType: String,
    val videoId: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AudioPlayerScreen(
    service: AudioPlayerService,
    navController: NavController
) {
    val pagerState = if (service.viewModel.currentSubtitleLanguage == null) rememberPagerState(
        pageCount = { 2 },
        initialPage = 0
    ) else rememberPagerState(pageCount = { 3 }, initialPage = 1)
    var currentView by remember {
        mutableStateOf(AudioPlayerView.Progress)
    }
    Crossfade(
        targetState = service.viewModel.videoInfo != null,
        label = "audioPlayerCrossfade"
    ) { isVideoInformationLoaded ->
        if (isVideoInformationLoaded) {
            service.viewModel.videoInfo?.data?.let { video ->
                ArrowTitleBackgroundWithCustomBackground(
                    onBack = navController::navigateUp,
                    background = {
                        val alpha by wearBiliAnimateFloatAsState(targetValue = if (pagerState.currentPage == (if (service.viewModel.currentSubtitleLanguage == null) 0 else 1) && currentView == AudioPlayerView.Progress) 0.8f else 0.3f)
                        BiliImage(
                            url = video.pic,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(alpha),
                            transformations = listOf(CustomBlurTransformation(LocalContext.current)),
                            contentScale = ContentScale.FillHeight
                        )

                    }
                ) {
                    Column {
                        //Row {
                        HorizontalPagerIndicator(
                            pagerState = pagerState,
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(end = titleBackgroundHorizontalPadding() + 2.dp),
                            activeColor = Color(255, 255, 255),
                            inactiveColor = Color(
                                255,
                                255,
                                255,
                                128
                            ),
                            spacing = 4.dp,
                            indicatorWidth = 6.dp,
                            indicatorHeight = 6.dp
                        )
                        //}
                        Spacer(modifier = Modifier.height(2.dp))
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.weight(1f)
                        ) { page ->
                            if (service.viewModel.currentSubtitleLanguage == null) {
                                when (page) {
                                    0 -> PlayerPage(
                                        service = service,
                                        currentView = currentView
                                    ) { currentView = it }

                                    1 -> SettingsPage(
                                        service = service,
                                        navController = navController
                                    )
                                }
                            } else {
                                when (page) {
                                    0 -> SubtitlePage(viewModel = service.viewModel)
                                    1 -> PlayerPage(
                                        service = service,
                                        currentView = currentView
                                    ) { currentView = it }

                                    2 -> SettingsPage(
                                        service = service,
                                        navController = navController
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            LoadingScreen()
        }
    }
}

@Composable
fun SubtitlePage(
    viewModel: IjkPlayerAudioPlayerViewModel
) {
    val currentSubtitle by viewModel.currentSubtitle.collectAsState(initial = null)
    val secondarySubtitle by viewModel.secondarySubtitleText.collectAsState(initial = null)
    val currentProgress by viewModel.currentPlayProgress.collectAsState(initial = 0)
    var currentIndex by remember {
        mutableIntStateOf(-1)
    }
    LaunchedEffect(key1 = currentSubtitle, block = {
        val index =
            viewModel.subtitleList[viewModel.currentSubtitleLanguage]?.currentSubtitleIndex ?: -1
        //state.animateScrollToItem(index)
        currentIndex = index
    })
    viewModel.subtitleList[viewModel.currentSubtitleLanguage]?.subtitleList?.let { subtitles ->
        LyricsView(
            subtitles = subtitles,
            currentIndex = currentIndex,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            contentColor = Color.White,
            contentPadding = PaddingValues(horizontal = 3.dp, vertical = 8.dp),
            currentTime = currentProgress,
            secondarySubtitleText = secondarySubtitle
        ) {
            viewModel.player.seekTo(((it.from) * 1000).toLong())
        }
    }
}

enum class AudioPlayerView {
    Progress, Volume, Seek
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlayerPage(
    service: AudioPlayerService,
    currentView: AudioPlayerView,
    onViewChanged: (AudioPlayerView) -> Unit
) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val hasFloatingSubtitle by remember {
        derivedStateOf {
            configuration.getActivatedExperimentalFunctions().contains(
                EXPERIMENTAL_FLOATING_SUBTITLE
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            Crossfade(targetState = currentView, label = "") {
                when (it) {
                    AudioPlayerView.Progress -> ProgressView(service = service)
                    AudioPlayerView.Seek -> SeekView(service = service)
                    AudioPlayerView.Volume -> VolumeView()
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val textMeasurer = rememberTextMeasurer()

            val volumeAlpha by wearBiliAnimateFloatAsState(targetValue = if (currentView == AudioPlayerView.Volume) 1f else 0.7f)
            val seekAlpha by wearBiliAnimateFloatAsState(targetValue = if (currentView == AudioPlayerView.Seek) 1f else 0.7f)
            val subtitleAlpha by wearBiliAnimateFloatAsState(targetValue = if (AudioPlayerManager.isSubtitleOn) 1f else 0.7f)

            Box(modifier = Modifier
                .size(24.dp)
                .alpha(volumeAlpha)
                .clickAlpha {
                    onViewChanged(
                        if (currentView == AudioPlayerView.Volume) {
                            AudioPlayerView.Progress
                        } else {
                            AudioPlayerView.Volume
                        }
                    )
                }
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawBehind {
                    if (currentView == AudioPlayerView.Volume) {
                        drawRoundRect(
                            Color.White,
                            size = Size(size.width, size.height),
                            cornerRadius = CornerRadius(6f)
                        )
                    }
                    translate(
                        (size.width - 18.sp.toPx()) / 2,
                        (size.height - 18.sp.toPx()) / 2
                    ) {
                        drawText(
                            textMeasurer,
                            text = unicodeToString("\\ueb15"),
                            style = TextStyle(
                                fontFamily = biliIconFontFamily,
                                fontSize = 18.sp,
                                color = Color.White
                            ),
                            blendMode = if (currentView == AudioPlayerView.Volume) BlendMode.Xor else DrawScope.DefaultBlendMode
                        )
                    }
                })
            Box(modifier = Modifier
                .size(24.dp)
                .alpha(seekAlpha)
                .clickAlpha {
                    if (!service.viewModel.isReady) return@clickAlpha
                    onViewChanged(
                        if (currentView == AudioPlayerView.Seek) {
                            AudioPlayerView.Progress
                        } else {
                            AudioPlayerView.Seek
                        }
                    )
                }
                .alpha(if (service.viewModel.isReady) 1f else 0.5f)
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawBehind {
                    if (currentView == AudioPlayerView.Seek) {
                        drawRoundRect(
                            Color.White,
                            size = Size(size.width, size.height),
                            cornerRadius = CornerRadius(6f)
                        )
                    }
                    translate(
                        (size.width - 18.sp.toPx()) / 2,
                        (size.height - 18.sp.toPx()) / 2
                    ) {
                        val bitmap = (AppCompatResources.getDrawable(
                            context,
                            R.drawable.icon_audio_seek
                        ) as VectorDrawable).toBitmap()
                        drawImage(
                            bitmap.asImageBitmap(),
                            dstSize = IntSize(18.sp.roundToPx(), 18.sp.roundToPx()),
                            blendMode = if (currentView == AudioPlayerView.Seek) BlendMode.Xor else DrawScope.DefaultBlendMode
                        )
                    }
                })
            if (hasFloatingSubtitle && service.viewModel.currentSubtitleLanguage != null) {
                Box(modifier = Modifier
                    .size(24.dp)
                    .alpha(subtitleAlpha)
                    .clickAlpha {
                        AudioPlayerManager.currentVideo =
                            service.viewModel.videoInfo?.data?.title ?: ""
                        AudioPlayerManager.isSubtitleOn = !AudioPlayerManager.isSubtitleOn
                    }
                    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                    .drawBehind {
                        if (AudioPlayerManager.isSubtitleOn) {
                            drawRoundRect(
                                Color.White,
                                size = Size(size.width, size.height),
                                cornerRadius = CornerRadius(6f)
                            )
                        }
                        translate(
                            (size.width - 18.sp.toPx()) / 2,
                            (size.height - 18.sp.toPx()) / 2
                        ) {
                            drawText(
                                textMeasurer,
                                text = unicodeToString("\\uea7f"),
                                style = TextStyle(
                                    fontFamily = biliIconFontFamily,
                                    fontSize = 18.sp,
                                    color = Color.White
                                ),
                                blendMode = if (AudioPlayerManager.isSubtitleOn) BlendMode.Xor else DrawScope.DefaultBlendMode
                            )
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressView(
    service: AudioPlayerService
) {
    val currentPlayProgress by service.viewModel.currentPlayProgress.collectAsState(initial = 0)
    service.viewModel.let { viewModel ->
        service.viewModel.videoInfo?.data?.let { video ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {
                // region title
                Text(
                    text = video.title,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 13.sp,
                    maxLines = 1,
                    //overflow = TextOverflow.Ellipsis,
                    fontFamily = wearbiliFontFamily,
                    modifier = Modifier.basicMarquee()
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = video.owner.name,
                    fontWeight = FontWeight.Medium,
                    color = Color(255, 255, 255, 128),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = wearbiliFontFamily
                )
                // endregion

                Spacer(modifier = Modifier.height(8.dp))
                // region player controller
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val quickSeekAlpha by wearBiliAnimateFloatAsState(targetValue = if (viewModel.isReady) 1f else 0.5f)
                    BiliTextIcon(icon = "ea04", size = 24.sp, modifier = Modifier
                        .weight(1f)
                        .alpha(quickSeekAlpha)
                        .clickVfx(isEnabled = viewModel.isReady) {
                            //adjustVolume(false, viewModel, service.lifecycleScope)
                            if (viewModel.isReady) {
                                viewModel.player.seekTo(
                                    (currentPlayProgress - 10 * 1000).coerceAtMost(
                                        0
                                    )
                                )
                            }
                        })
                    Box(modifier = Modifier
                        .clickVfx(isEnabled = viewModel.isReady) {
                            if (viewModel.player.isPlaying) viewModel.player.pause() else viewModel.player.start()
                        }
                        .weight(1f)
                        .aspectRatio(1f)
                        .drawWithContent {
                            drawContent()
                            val stroke = Stroke(7f, cap = StrokeCap.Round)
                            val diameterOffset = stroke.width / 2
                            val arcDimen = size.width - 2 * diameterOffset
                            val progress =
                                currentPlayProgress.toFloat() / (viewModel.player.duration + 1).toFloat()
                            drawArc(
                                color = Color.White,
                                startAngle = -90f,
                                sweepAngle = progress * 360f,
                                useCenter = false,
                                topLeft = Offset(diameterOffset, diameterOffset),
                                size = Size(arcDimen, arcDimen),
                                style = stroke
                            )
                        }) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(18, 18, 18, 77))
                                .aspectRatio(1f)

                        ) {
                            if (viewModel.currentStat == PlayerStats.Buffering) {
                                CircularProgressIndicator(
                                    color = parseColor("#FFDBE7"),
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(3.dp)
                                )
                            } else {
                                SegoeTextIcon(
                                    icon = if (viewModel.player.isPlaying) "f8ae" else "f5b0",
                                    modifier = Modifier.align(Alignment.Center),
                                    size = 20.sp
                                )
                            }
                        }
                    }
                    BiliTextIcon(icon = "ea1e", size = 24.sp, modifier = Modifier
                        .weight(1f)
                        .alpha(quickSeekAlpha)
                        .clickVfx(isEnabled = viewModel.isReady) {
                            if (viewModel.isReady) {
                                viewModel.player.seekTo(
                                    (currentPlayProgress + 10 * 1000).coerceAtMost(
                                        viewModel.videoDuration
                                    )
                                )
                            }
                        })
                }
                // endregion


                // region loading and volume message
                Crossfade(
                    targetState = viewModel.isAdjustingVolume,
                    label = ""
                ) { isShowingVolume ->
                    if (isShowingVolume) {
                        Text(
                            text = viewModel.volumeInformation,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontFamily = wearbiliFontFamily,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Crossfade(targetState = viewModel.isReady, label = "") { isReady ->
                            if (!isReady) {
                                Crossfade(
                                    targetState = viewModel.loadingMessage,
                                    label = ""
                                ) { message ->
                                    Text(
                                        text = message.lines().lastOrNull() ?: "",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontFamily = wearbiliFontFamily,
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            } else {
                                Text(
                                    text = "${
                                        currentPlayProgress.div(1000).secondToTime()
                                    }/${
                                        viewModel.player.duration.div(1000)
                                            .secondToTime()
                                    }",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontFamily = wearbiliFontFamily,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
                // endregion
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeekView(
    service: AudioPlayerService,
) {
    val currentPlayProgress by service.viewModel.currentPlayProgress.collectAsState(initial = 0)
    var draggedProgress by remember {
        mutableLongStateOf(0L)
    }
    var isDraggingProgress by remember {
        mutableStateOf(false)
    }
    val slideBarHeight by wearBiliAnimateDpAsState(targetValue = if (isDraggingProgress) 24.dp else 12.dp)
    val textRowPadding by wearBiliAnimateDpAsState(targetValue = if (isDraggingProgress) 0.dp else 4.dp)

    service.viewModel.let { viewModel ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp), verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = textRowPadding)
                    .offset(y = (-2).dp)
            ) {
                Text(
                    text = (if (isDraggingProgress) draggedProgress else currentPlayProgress).div(
                        1000
                    ).secondToTime(),
                    fontFamily = wearbiliFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = viewModel.player.duration.div(1000).secondToTime(),
                    fontFamily = wearbiliFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            GradientSlider(
                value = if (isDraggingProgress) draggedProgress.toFloat() else currentPlayProgress.toFloat(),
                onValueChanged = {
                    isDraggingProgress = true
                    draggedProgress = it.toLong()
                },
                onSlideFinished = {
                    viewModel.player.seekTo(draggedProgress)
                    viewModel.currentStat =
                        PlayerStats.Buffering
                    isDraggingProgress = false
                },
                range = 0f..viewModel.videoDuration.toFloat(),
                brush = Brush.horizontalGradient(listOf(Color.White, Color.White)),
                trackHeight = slideBarHeight,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-2).dp)
            )
        }
    }
}

@Composable
fun VolumeView() {
    val context = LocalContext.current
    var currentVolume by remember {
        mutableFloatStateOf(context.getCurrentVolume().toFloat())
    }
    var isDraggingProgress by remember {
        mutableStateOf(false)
    }
    val slideBarHeight by wearBiliAnimateDpAsState(targetValue = if (isDraggingProgress) 24.dp else 12.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp), verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontSize = 18.sp, baselineShift = BaselineShift(-0.3f))) {
                    appendBiliIcon("eb15")
                }
                append(" ")
                append("${(currentVolume / context.getMaxVolume().toFloat() * 100).toInt()}%")
            },
            fontFamily = wearbiliFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        GradientSlider(
            value = currentVolume,
            onValueChanged = {
                isDraggingProgress = true
                currentVolume = it
                context.adjustVolume(it.roundToInt())
            },
            onSlideFinished = {
                isDraggingProgress = false
            },
            range = 0f..context.getMaxVolume().toFloat(),
            brush = Brush.horizontalGradient(listOf(Color.White, Color.White)),
            trackHeight = slideBarHeight,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-2).dp)
        )
    }
}

private fun adjustVolume(
    context: Context,
    isIncrease: Boolean,
    viewModel: IjkPlayerAudioPlayerViewModel,
    scope: CoroutineScope
) {
    //获取系统的Audio管理者
    val mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    //最大音量
    var maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    //当前音量
    var currentVolume =
        mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    val step = if (maxVolume.div(5) < 1) {
        1
    } else maxVolume.div(5)
    //val targetVolume = currentPercent.times(10).log("1").toInt().log("2").plus(1).toFloat().log("3").div(10).log("4").times(currentVolume).log("5").toInt()
    val targetVolume =
        currentVolume.plus(step.times(if (isIncrease) 1 else -1))
    if (targetVolume > maxVolume) {
        mAudioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            maxVolume,
            0
        )   //flag=0不显示系统指示器
    } else if (targetVolume < 0) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
    } else mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, targetVolume, 0)

    maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    currentVolume =
        mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    val currentPercent = currentVolume.toFloat() / maxVolume.toFloat()

    viewModel.volumeInformation = "当前音量: ${((currentPercent) * 100).toInt()}%"
    scope.launch {
        viewModel.isAdjustingVolume = true
        delay(1500)
        viewModel.isAdjustingVolume = false
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    service: AudioPlayerService,
    navController: NavController
) {
    val context = LocalContext.current
    var playBackSpeed by remember {
        mutableIntStateOf(100)
    }
    var currentVolume by remember {
        mutableFloatStateOf(context.getCurrentVolume().toFloat())
    }
    service.viewModel.let { viewModel ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            //region setting items
            Text(
                text = "播放选项",
                color = Color.White,
                fontFamily = wearbiliFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            PlayerSettingActionItem(
                name = "前往视频",
                description = "观看此音频对应的视频",
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.OndemandVideo,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize()
                    )
                }) {
                viewModel.videoInfo?.data?.let { video ->
                    navController.navigateUp()
                    navController.navigate(
                        IjkVideoPlayerScreen(
                            isCacheVideo = false,
                            videoIdType = VIDEO_TYPE_BVID,
                            videoId = video.bvid,
                            videoCid = video.cid,
                            isBangumi = false,
                        )
                    )
                    service.exitService()
                }
            }

            PlayerSettingActionItem(
                name = "退出",
                description = "停止播放并退出播放器",
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Stop,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            ) {
                service.exitService()
                //finish()
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
                        viewModel.player.setSpeed(
                            i.toFloat().div(100)
                        )
                        playBackSpeed = i
                    }
                }
                for (i in 100..300 step 50) {
                    PlayerSettingItem(
                        text = i.toFloat().div(100).toString() + "x",
                        isSelected = playBackSpeed == i
                    ) {
                        viewModel.player.setSpeed(
                            i.toFloat().div(100)
                        )
                        playBackSpeed = i
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
                    imageVector = Icons.Outlined.Subtitles,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            }, settingName = "双语字幕") {
                PlayerSettingItem(
                    text = "关闭",
                    isSelected = viewModel.secondarySubtitleLanguage == null
                ) {
                    viewModel.secondarySubtitleLanguage =
                        null
                }
                viewModel.subtitleList.forEach {
                    PlayerSettingItem(
                        text = it.value.subtitleLanguage,
                        isSelected = viewModel.secondarySubtitleLanguage == it.key
                    ) {
                        viewModel.secondarySubtitleLanguage =
                            it.key
                    }
                }
            }
            /*PlayerSettingActionItem(
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
                        this@SettingsPage,
                        DlnaDeviceDiscoverActivity::class.java
                    ).apply {
                        putExtra(
                            "url",
                            viewModel.videoCastUrl
                        )
                    })
                finish()
            }*/

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
                displayedSliderValue = "${
                    (currentVolume / context.getMaxVolume().toFloat() * 100).toInt()
                }%",
                sliderValueRange = 0f..context.getMaxVolume().toFloat()
            ) {
                context.adjustVolume(it.roundToInt())
                currentVolume = it
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(70, 70, 70, 100)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "音频播放器正在加载哦...",
            color = Color(255, 255, 255, 128),
            fontFamily = wearbiliFontFamily,
            fontWeight = FontWeight.Medium
        )
    }
}