package cn.spacexc.wearbili.remake.app.player.livestream.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import android.view.SurfaceView
import android.view.TextureView
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.ScreenRotation
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.viewinterop.AndroidView
import cn.spacexc.wearbili.common.domain.log.TAG
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PlayerQuickActionButton
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.VideoDisplaySurface
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.VideoPlayerPages
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.adjustVolume
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.getCurrentVolume
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.getMaxVolume
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.WearBiliAnimatedVisibility
import cn.spacexc.wearbili.remake.common.ui.isRound
import cn.spacexc.wearbili.remake.common.ui.rememberMutableInteractionSource
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily

@Composable
fun Activity.LiveStreamScreen(
    viewModel: LiveStreamViewModel,
    displaySurface: VideoDisplaySurface = VideoDisplaySurface.SURFACE_VIEW,
) {
    val localDensity = LocalDensity.current
    val roundScreenControllerAlpha by animateIntAsState(
        targetValue = if (isRound() && viewModel.isControllerVisible) 127/*255/2*/ else 0,
        label = ""
    )
    var currentPage: VideoPlayerPages by remember {
        mutableStateOf(VideoPlayerPages.Main)
    }

    var screenSize by remember {
        mutableStateOf(Size(1f, 1f))
    }
    var controllerTitleColumnHeight by remember {
        mutableStateOf(0.dp)
    }
    var controllerProgressColumnHeight by remember {
        mutableStateOf(0.dp)
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

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .onSizeChanged {
            screenSize = it.toSize()
        }
    ) {
        //region player surface
        Box(
            modifier = Modifier
                .aspectRatio(viewModel.aspectRatio)
                .align(Alignment.Center)
        ) {
            when (displaySurface) {
                VideoDisplaySurface.TEXTURE_VIEW -> {
                    AndroidView(factory = { TextureView(it) }) { textureView ->
                        //viewModel.httpPlayer.setDisplay(textureView.display.)
                        //viewModel.cachePlayer.setVideoTextureView(textureView)
                    }
                }

                VideoDisplaySurface.SURFACE_VIEW -> {
                    AndroidView(factory = { SurfaceView(it) }) { surfaceView ->
                        viewModel.httpPlayer.setDisplay(surfaceView.holder)
                    }
                }
            }
        }
        //endregion

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0, 0, 0, roundScreenControllerAlpha))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                            viewModel.isControllerVisible =
                                !viewModel.isControllerVisible
                        }, onDoubleTap = {
                            if (viewModel.httpPlayer.isPlaying) viewModel.httpPlayer.pause() else viewModel.httpPlayer.start()
                        })
                    }
            ) {
                var titleRowHeight by remember {
                    mutableStateOf(0.dp)
                }
                WearBiliAnimatedVisibility(
                    visible = viewModel.isControllerVisible,
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
                                onClick = ::finish
                            ),
                        verticalAlignment = Alignment.CenterVertically
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
                                text = "-",
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
                }
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )
                WearBiliAnimatedVisibility(
                    visible = viewModel.isControllerVisible,
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
                                    painter = painterResource(id = if (viewModel.httpPlayer.isPlaying) R.drawable.img_pause_icon else R.drawable.img_play_icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        //.clickVfx { if (viewModel.player.isPlaying) viewModel.player.pause() else viewModel.player.play() }
                                        .clickable {
                                            if (viewModel.httpPlayer.isPlaying) viewModel.httpPlayer.pause() else viewModel.httpPlayer.start()
                                        }
                                        .size(18.dp)

                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }

                            //region player quick actions


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

            if (isRound()) {
                WearBiliAnimatedVisibility(
                    visible = viewModel.isControllerVisible,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = if (viewModel.httpPlayer.isPlaying) R.drawable.img_pause_icon else R.drawable.img_play_icon),
                            contentDescription = null,
                            modifier = Modifier
                                //.clickVfx { if (viewModel.player.isPlaying) viewModel.player.pause() else viewModel.player.play() }
                                .clickable {
                                    if (viewModel.httpPlayer.isPlaying) viewModel.httpPlayer.pause() else viewModel.httpPlayer.start()
                                }
                                .size(18.dp)

                        )
                    }
                }
            }
        }
    }
}

fun Activity.rotateScreen() {
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