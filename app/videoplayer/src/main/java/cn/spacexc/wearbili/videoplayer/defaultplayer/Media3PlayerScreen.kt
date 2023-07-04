package cn.spacexc.wearbili.videoplayer.defaultplayer

import android.view.SurfaceView
import android.view.TextureView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import cn.spacexc.wearbili.videoplayer.theme.wearbiliFontFamily


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

@UnstableApi
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Media3PlayerScreen(
    viewModel: Media3PlayerViewModel,
    displaySurface: VideoDisplaySurface,
    withSubtitleAnimation: Boolean = true,
    onBack: () -> Unit
) {
    val currentSubtitleText by viewModel.currentSubtitleText.collectAsState(initial = null)
    val currentPlayerPosition by viewModel.currentPlayProgress.collectAsState(initial = 0)
    val scope = rememberCoroutineScope()
    val localDensity = LocalDensity.current
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
        viewModel.videoInfo?.let { video ->
            Column(modifier = Modifier.fillMaxSize()) {
                var titleRowHeight by remember {
                    mutableStateOf(0.dp)
                }
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
                            titleRowHeight = with(localDensity) { it.height.toDp() }
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
                        modifier = Modifier.size(titleRowHeight / 2)
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
                            text = "-人在看",
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
                /*Column(
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
                        .onSizeChanged {
                            titleRowHeight = with(localDensity) { it.height.toDp() }
                        }
                ) {
                    Text(
                        text = "${currentPlayerPosition.div(1000).secondToTime()}/${viewModel.videoDuration.div(1000).secondToTime()}",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = wearbiliFontFamily,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }*/
            }
        }
        if (withSubtitleAnimation) {
            AnimatedVisibility(
                visible = currentSubtitleText != null,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(
                    text = currentSubtitleText ?: "", modifier = Modifier
                        .padding(8.dp)
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
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            currentSubtitleText?.let {
                Text(
                    text = currentSubtitleText ?: "", modifier = Modifier
                        .padding(8.dp)
                        .background(
                            color = Color(49, 47, 47, 153),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(6.dp)
                        .align(Alignment.BottomCenter),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = wearbiliFontFamily,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }
        if (!viewModel.loaded) {
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
        /*Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "关闭", modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = Color(49, 47, 47, 153),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(6.dp)
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
                    text = it.value.subtitleLanguage, modifier = Modifier
                        .padding(8.dp)
                        .background(
                            color = Color(49, 47, 47, 153),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(6.dp)
                        .clickable {
                            scope.launch {
                                viewModel.currentSubtitleLanguage = it.key
                            }
                        },
                    color = if (viewModel.currentSubtitleLanguage == it.key) BilibiliPink else Color.White,
                    fontSize = 12.sp,
                    fontFamily = wearbiliFontFamily,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }*/
    }
}