package cn.spacexc.wearbili.remake.app.player.videoplayer.mirroring.dlna.controller.ui

import android.app.Activity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.spacexc.wearbili.common.domain.time.secondToTime
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.player.videoplayer.mirroring.dlna.WearbiliCastManager
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.isRound
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import com.android.cast.dlna.dmc.DLNACastManager

/**
 * Created by XC-Qan on 2023/7/18.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Activity.DlnaControllerScreen(
    videoName: String,
    viewModel: DlnaControllerViewModel
) {
    var draggedProgress by remember {
        mutableLongStateOf(0L)
    }
    var isDraggingProgress by remember {
        mutableStateOf(false)
    }
    val progressBarThumbScale by animateFloatAsState(targetValue = if (isDraggingProgress) 1.5f else 1f)
    TitleBackground(title = "投屏") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = videoName,
                fontSize = 12.spx,
                maxLines = 1,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontFamily = wearbiliFontFamily,
                modifier = Modifier.alpha(0.8f)
            )
            Icon(
                imageVector = Icons.Default.Tv,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "已连接",
                fontSize = 12.spx,
                maxLines = 1,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontFamily = wearbiliFontFamily,
                modifier = Modifier.alpha(0.8f)
            )
            Text(
                text = WearbiliCastManager.currentDevice()?.details?.friendlyName ?: "未知设备",
                fontSize = 14.spx,
                maxLines = 1,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontFamily = wearbiliFontFamily
            )

            //{
            Text(
                text = "${
                    (if (isDraggingProgress) draggedProgress else viewModel.currentPosition).div(
                        1000
                    ).secondToTime()
                }/${
                    viewModel.videoDuration.secondToTime()
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
                value = if (isDraggingProgress) draggedProgress.toFloat() else viewModel.currentPosition.toFloat(),
                onValueChange = {
                    isDraggingProgress = true
                    draggedProgress = it.toLong()
                },
                onValueChangeFinished = {
                    DLNACastManager.getInstance().seekTo(draggedProgress)

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
                                y = 4.dp,
                                x = 3.dp
                            )
                            .size(10.dp)
                            .scale(progressBarThumbScale)
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
                horizontalArrangement = if (isRound()) Arrangement.Center else Arrangement.Start
            ) {
                if (!isRound()) {
                    Image(
                        painter = painterResource(id = if (viewModel.isPlaying) R.drawable.img_pause_icon else R.drawable.img_play_icon),
                        contentDescription = null,
                        modifier = Modifier
                            //.clickVfx { if (viewModel.player.isPlaying) viewModel.player.pause() else viewModel.player.play() }
                            .clickable {
                                if (viewModel.isPlaying) DLNACastManager
                                    .getInstance()
                                    .pause() else DLNACastManager
                                    .getInstance()
                                    .play()
                            }
                            .size(18.dp)

                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}