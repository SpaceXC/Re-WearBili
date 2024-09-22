package cn.spacexc.wearbili.remake.app.player.audio

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.spacexc.bilibilisdk.sdk.video.info.remote.subtitle.Subtitle
import cn.spacexc.wearbili.remake.app.player.audio.ui.lyrics.BlankSuspend
import cn.spacexc.wearbili.remake.app.player.audio.ui.lyrics.LyricsLine
import cn.spacexc.wearbili.remake.app.settings.LocalConfiguration
import cn.spacexc.wearbili.remake.app.settings.experimantal.EXPERIMENTAL_FLOATING_SUBTITLE
import cn.spacexc.wearbili.remake.app.settings.experimantal.getActivatedExperimentalFunctions
import cn.spacexc.wearbili.remake.common.ui.WearBiliAnimatedContent
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimatedContentSize
import kotlinx.coroutines.flow.MutableStateFlow

object AudioPlayerManager {
    private var audioPlayerHeartbeat: AudioPlayerHeartbeat? = null

    var isSubtitleOn by mutableStateOf(false)

    var currentPlayerTask = MutableStateFlow<AudioPlayerTask?>(null)

    private var currentOffset by mutableStateOf(Offset.Zero)

    var currentPlayingSubtitle: Subtitle? by mutableStateOf(null)
    var currentProgress by mutableDoubleStateOf(0.0)
    var currentVideo by mutableStateOf("")

    fun heartbeat(heartbeat: AudioPlayerHeartbeat) {
        audioPlayerHeartbeat = heartbeat
    }

    fun isAudioPlayerOn(): Boolean {
        return (System.currentTimeMillis() - (audioPlayerHeartbeat?.timestamp ?: 0)) < 1000
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun LyricContent() {
        val configuration = LocalConfiguration.current
        val hasFloatingSubtitle by remember {
            derivedStateOf {
                configuration.getActivatedExperimentalFunctions().contains(
                    EXPERIMENTAL_FLOATING_SUBTITLE
                )
            }
        }

        if (hasFloatingSubtitle) {
            Box(modifier = Modifier.fillMaxSize()) {
                val draggableState = rememberDraggable2DState {
                    currentOffset += it
                }
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(currentOffset.x.toInt(), currentOffset.y.toInt())
                        }
                        .draggable2D(draggableState)
                        .padding(titleBackgroundHorizontalPadding() - 4.dp)
                        .wearBiliAnimatedContentSize(animationSpec = tween(400))
                        .border(
                            width = 0.3f.dp,
                            shape = RoundedCornerShape(10.dp),
                            brush = Brush.linearGradient(
                                listOf(
                                    Color(54, 54, 54, 255),
                                    Color.Transparent
                                ),
                                start = Offset.Zero,
                                end = Offset.Infinite
                            )
                        )
                        .background(
                            brush = Brush.horizontalGradient(
                                colorStops = arrayOf(
                                    0f to Color.Black,
                                    .5f to Color.Black,
                                    1f to Color(39, 16, 23)
                                )
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)

                ) {
                    WearBiliAnimatedContent(
                        targetState = currentPlayingSubtitle,
                        label = "",
                        transitionSpec = {
                            slideInVertically { it } + fadeIn() togetherWith slideOutVertically { -it } + fadeOut()
                        }) { currentSubtitle ->
                        if (currentSubtitle == null) {
                            Text(
                                text = currentVideo,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = wearbiliFontFamily,
                            )  //placeholder
                        }
                        currentSubtitle?.let { subtitle ->
                            if (subtitle.content == "[BLANK_SUSPEND]") {
                                BlankSuspend(
                                    timeLength = subtitle.to - subtitle.from,
                                    startTime = subtitle.from,
                                    currentTime = currentProgress,
                                    modifier = Modifier.scale(0.7f)
                                )
                            } else {
                                LyricsLine(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White,
                                    text = subtitle.content,
                                    timeLength = subtitle.to - subtitle.from,
                                    isActive = true,
                                    startTime = subtitle.from,
                                    currentTime = currentProgress,
                                    notation = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    data class AudioPlayerHeartbeat(
        val timestamp: Long
    )
}

data class AudioPlayerTask(
    val isCache: Boolean,
    val videoIdType: String,
    val videoId: String,
    val videoCid: Long,
    val isBangumi: Boolean
)