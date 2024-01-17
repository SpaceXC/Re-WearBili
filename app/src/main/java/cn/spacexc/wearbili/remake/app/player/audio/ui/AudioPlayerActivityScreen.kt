package cn.spacexc.wearbili.remake.app.player.audio.ui

import TextIcon
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OndemandVideo
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material.icons.outlined.Subtitles
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.common.domain.time.secondToTime
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.player.audio.AudioPlayerService
import cn.spacexc.wearbili.remake.app.player.audio.ui.lyrics.LyricsView
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.Media3PlayerActivity
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PlayerSetting
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PlayerSettingActionItem
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PlayerSettingItem
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.adjustVolume
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.getCurrentVolume
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.getMaxVolume
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_CID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID_TYPE
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.ui.ArrowTitleBackgroundWithCustomBackground
import cn.spacexc.wearbili.remake.common.ui.BiliImage
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.rememberMutableInteractionSource
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimatedContentSize
import coil.transform.CustomBlurTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.sign

/**
 * Created by XC-Qan on 2023/10/15.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Activity.AudioPlayerActivityScreen(
    service: AudioPlayerService
) {
    val pagerState = rememberPagerState(pageCount = { 3 }, initialPage = 1)
    Crossfade(
        targetState = service.viewModel.videoInfo != null,
        label = "audioPlayerCrossfade"
    ) { isVideoInformationLoaded ->
        if (isVideoInformationLoaded) {
            service.viewModel.videoInfo?.data?.let { video ->
                ArrowTitleBackgroundWithCustomBackground(
                    background = {
                        val alpha by animateFloatAsState(targetValue = if (pagerState.currentPage == 1) 0.8f else 0.3f)
                        BiliImage(
                            url = video.pic,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(alpha),
                            transformations = listOf(CustomBlurTransformation(this@AudioPlayerActivityScreen)),
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
                                .padding(end = TitleBackgroundHorizontalPadding + 2.dp),
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
                            when (page) {
                                0 -> SubtitlePage(viewModel = service.viewModel)
                                1 -> PlayerPage(service = service)
                                2 -> SettingsPage(service = service)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    pageCount: Int = pagerState.pageCount,
    pageIndexMapping: (Int) -> Int = { it },
    activeColor: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    inactiveColor: Color = activeColor.copy(ContentAlpha.disabled),
    indicatorWidth: Dp = 8.dp,
    indicatorHeight: Dp = indicatorWidth,
    spacing: Dp = indicatorWidth,
    indicatorShape: Shape = CircleShape,
) {

    val indicatorWidthPx = LocalDensity.current.run { indicatorWidth.roundToPx() }
    val spacingPx = LocalDensity.current.run { spacing.roundToPx() }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val indicatorModifier = Modifier
                .size(width = indicatorWidth, height = indicatorHeight)
                .background(color = inactiveColor, shape = indicatorShape)

            repeat(pageCount) {
                Box(indicatorModifier)
            }
        }

        Box(
            Modifier
                .offset {
                    val position = pageIndexMapping(pagerState.currentPage)
                    val offset = pagerState.currentPageOffsetFraction
                    val next = pageIndexMapping(pagerState.currentPage + offset.sign.toInt())
                    val scrollPosition = ((next - position) * offset.absoluteValue + position)
                        .coerceIn(
                            0f,
                            (pageCount - 1)
                                .coerceAtLeast(0)
                                .toFloat()
                        )

                    IntOffset(
                        x = ((spacingPx + indicatorWidthPx) * scrollPosition).toInt(),
                        y = 0
                    )
                }
                .size(width = indicatorWidth, height = indicatorHeight)
                .then(
                    if (pageCount > 0) Modifier.background(
                        color = activeColor,
                        shape = indicatorShape,
                    )
                    else Modifier
                )
        )
    }
}

@Composable
fun SubtitlePage(
    viewModel: Media3AudioPlayerViewModel
) {
    val currentSubtitle by viewModel.currentSubtitle.collectAsState(initial = null)
    var currentIndex by remember {
        mutableIntStateOf(-1)
    }
    LaunchedEffect(key1 = currentSubtitle, block = {
        val index =
            viewModel.subtitleList[viewModel.currentSubtitleLanguage]?.currentSubtitleIndex ?: 0
        //state.animateScrollToItem(index)
        currentIndex = index
    })
    viewModel.subtitleList[viewModel.currentSubtitleLanguage]?.subtitleList?.let { subtitles ->
        LyricsView(
            subtitles = subtitles,
            currentIndex = currentIndex,
            fontWeight = FontWeight.Bold,
            fontSize = 18.spx,
            contentColor = Color.White,
            contentPadding = PaddingValues(horizontal = 3.dp, vertical = 8.dp)
        ) {
            viewModel.player.seekTo((it.from * 1000).toLong())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Activity.PlayerPage(
    service: AudioPlayerService
) {
    val currentPlayProgress by service.viewModel.currentPlayProgress.collectAsState(initial = 0)
    var draggedProgress by remember {
        mutableLongStateOf(0L)
    }
    var isDraggingProgress by remember {
        mutableStateOf(false)
    }
    val currentSubtitleText by service.viewModel.currentSubtitleText.collectAsState(initial = null)
    val progressBarThumbScale by animateFloatAsState(targetValue = if (isDraggingProgress) 1.5f else 1f)
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
                    fontSize = 13.spx,
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
                    fontSize = 12.spx,
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
                    TextIcon(icon = "\\ue993", size = 24.spx, modifier = Modifier
                        .weight(1f)
                        .clickVfx { adjustVolume(false, viewModel, service.lifecycleScope) })
                    Box(modifier = Modifier
                        .clickVfx(isEnabled = viewModel.isReady) {
                            if (viewModel.player.isPlaying) viewModel.player.pause() else viewModel.player.play()
                        }
                        .weight(1f)
                        .clip(CircleShape)
                        .background(BilibiliPink)
                        .aspectRatio(1f)
                        .padding(10.dp)
                    ) {
                        if (viewModel.currentStat == PlayerStats.Buffering) {
                            //CircularProgressIndicator(color = BilibiliPink/*parseColor("#FFDBE7")*/)
                        } else {
                            TextIcon(
                                icon = if (viewModel.player.isPlaying) "\\uf8ae" else "\\uf5b0",
                                modifier = Modifier.align(Alignment.Center),
                                size = 20.spx
                            )
                        }
                    }
                    TextIcon(icon = "\\ue995", size = 24.spx, modifier = Modifier
                        .weight(1f)
                        .clickVfx { adjustVolume(true, viewModel, service.lifecycleScope) })
                }
                // endregion

                // region subtitle
                Box(modifier = Modifier.animateContentSize()) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = currentSubtitleText != null,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        androidx.compose.material.Text(
                            text = currentSubtitleText ?: "", modifier = Modifier
                                .padding(
                                    start = 8.dp,
                                    end = 8.dp,
                                    top = 8.dp
                                )
                                .background(
                                    color = Color(49, 47, 47, 153),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(6.dp)
                                //.align(Alignment.BottomCenter)
                                .wearBiliAnimatedContentSize(),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontFamily = wearbiliFontFamily,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                // endregion

                // region seekbar
                androidx.compose.material3.Slider(
                    value = if (isDraggingProgress) draggedProgress.toFloat() else currentPlayProgress.toFloat(),
                    onValueChange = {
                        isDraggingProgress = true
                        draggedProgress = it.toLong()
                    },
                    onValueChangeFinished = {
                        viewModel.player.seekTo(draggedProgress)
                        viewModel.currentStat =
                            PlayerStats.Buffering
                        isDraggingProgress = false
                    },
                    valueRange = 0f..viewModel.videoDuration.toFloat(),
                    colors = SliderDefaults.colors(
                        activeTrackColor = BilibiliPink
                    ),
                    thumb = {
                        Image(
                            painter = painterResource(id = R.drawable.img_progress_bar_thumb_little_tv),
                            contentDescription = null,
                            modifier = Modifier
                                .offset(
                                    y = 5.dp,
                                    x = 3.dp
                                )
                                .size(10.dp)
                                .scale(progressBarThumbScale)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(1.05f)
                )
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
                            fontSize = 10.spx,
                            fontFamily = wearbiliFontFamily,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Crossfade(targetState = viewModel.isReady, label = "") { isReady ->
                            if (!isReady) {
                                Crossfade(
                                    targetState = viewModel.loadingMessage,
                                    label = ""
                                ) { message ->
                                    Text(
                                        text = message,
                                        color = Color.White,
                                        fontSize = 10.spx,
                                        fontFamily = wearbiliFontFamily,
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Center
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
                                    fontSize = 10.spx,
                                    fontFamily = wearbiliFontFamily,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center
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

private fun Activity.adjustVolume(
    isIncrease: Boolean,
    viewModel: Media3AudioPlayerViewModel,
    scope: CoroutineScope
) {
    //获取系统的Audio管理者
    val mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
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
fun Activity.SettingsPage(
    service: AudioPlayerService
) {
    var playBackSpeed by remember {
        mutableIntStateOf(100)
    }
    var currentVolume by remember {
        mutableIntStateOf(getCurrentVolume())
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
                fontSize = 20.spx,
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
                    startActivity(
                        Intent(this@SettingsPage, Media3PlayerActivity::class.java).apply {
                            putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_BVID)
                            putExtra(PARAM_VIDEO_ID, video.bvid)
                            putExtra(PARAM_VIDEO_CID, video.cid.logd("cid"))
                            startActivity(this)
                        }
                    )
                    service.exitService()
                    finish()
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
                finish()
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
                        viewModel.player.setPlaybackSpeed(
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
                        viewModel.player.setPlaybackSpeed(
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

            Column {
                Row {
                    androidx.compose.material.Text(
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
                        adjustVolume(it.roundToInt())
                        currentVolume = it.roundToInt()
                    },
                    valueRange = 0.toFloat()..getMaxVolume().toFloat(),
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