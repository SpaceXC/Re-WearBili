package cn.spacexc.wearbili.remake.app.video.info.info.ui.v2.basic

import BiliTextIcon
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.IjkVideoPlayerScreen
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.IjkVideoPlayerViewModel
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PlayerStats
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationViewModel
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.ui.BiliImage
import cn.spacexc.wearbili.remake.common.ui.OutlinedRoundButton
import cn.spacexc.wearbili.remake.common.ui.rememberMutableInteractionSource
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SimpleVideoInformation(
    animatedContentScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    globalSharedTransitionScope: SharedTransitionScope,
    globalAnimatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController,
    viewModel: VideoInformationViewModel,
    videoPlayerViewModel: IjkVideoPlayerViewModel,
    onGoToDetail: (DpSize) -> Unit
) {
    var infoButtonSize by remember {
        mutableStateOf(DpSize(0.dp, 0.dp))
    }
    val localDensity = LocalDensity.current
    viewModel.state.videoData?.let { video ->
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = with(globalSharedTransitionScope) {
                    Modifier
                        .sharedElement(
                            rememberSharedContentState(key = "videoCover"),
                            globalAnimatedVisibilityScope
                        )
                        .weight(1f)
                        .padding(vertical = 10.dp)
                        .aspectRatio(16f / 9f)
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                }
            ) {
                if (videoPlayerViewModel.currentStat != PlayerStats.Playing) {
                    BiliImage(
                        url = video.view.pic,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    AndroidView(factory = { SurfaceView(it) }) { surfaceView ->
                        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
                            override fun surfaceCreated(holder: SurfaceHolder) {
                                videoPlayerViewModel.httpPlayer.setDisplay(holder)
                            }

                            override fun surfaceChanged(
                                holder: SurfaceHolder,
                                p0: Int,
                                p1: Int,
                                p2: Int
                            ) {
                                //videoPlayerViewModel.httpPlayer.setDisplay(holder)
                            }

                            override fun surfaceDestroyed(p0: SurfaceHolder) {}
                        })

                    }
                }
            }
            Text(
                text = video.view.title,
                style = TextStyle(
                    fontFamily = wearbiliFontFamily,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = with(sharedTransitionScope) {
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = titleBackgroundHorizontalPadding())
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "title"),
                            animatedVisibilityScope = animatedContentScope
                        )
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                minLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedRoundButton(
                    modifier = Modifier.weight(1f),
                    buttonModifier = Modifier.aspectRatio(1f),
                    interactionSource = rememberMutableInteractionSource(), icon = {
                        Row(
                            modifier = Modifier.align(Alignment.Center),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BiliTextIcon(icon = "EAB4", size = 18.sp)
                        }
                    },
                    text = "",
                    onClick = {

                    }
                )
                OutlinedRoundButton(
                    modifier = Modifier.weight(1f),
                    buttonModifier = Modifier.aspectRatio(1f),
                    interactionSource = rememberMutableInteractionSource(), icon = {
                        Row(
                            modifier = Modifier.align(Alignment.Center),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BiliTextIcon(icon = "EAF7", size = 18.sp)
                        }
                    },
                    text = "",
                    onClick = {
                        navController.navigate(
                            IjkVideoPlayerScreen(
                                isCacheVideo = false,
                                videoIdType = VIDEO_TYPE_BVID,
                                videoId = video.view.bvid,
                                videoCid = video.view.cid,
                                isBangumi = false
                            )
                        )
                    }
                )
                OutlinedRoundButton(
                    modifier = with(sharedTransitionScope) {
                        Modifier
                            .sharedElement(
                                rememberSharedContentState(key = "infoButton"),
                                animatedContentScope
                            )
                            .weight(1f)
                            .onSizeChanged {
                                infoButtonSize = with(localDensity) {
                                    it
                                        .toSize()
                                        .toDpSize()
                                }
                            }
                    },
                    buttonModifier = Modifier.aspectRatio(1f),
                    interactionSource = rememberMutableInteractionSource(), icon = {
                        Row(
                            modifier = Modifier.align(Alignment.Center),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BiliTextIcon(icon = "EAC0", size = 18.sp)
                        }
                    },
                    text = "",
                    onClick = {
                        onGoToDetail(infoButtonSize)
                    }
                )
            }
        }
    }
}