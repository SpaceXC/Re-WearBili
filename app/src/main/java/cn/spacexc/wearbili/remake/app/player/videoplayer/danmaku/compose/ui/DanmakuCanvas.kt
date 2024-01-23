package cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import cn.spacexc.wearbili.common.ifNullOrZero
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.DanmakuCanvasState
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.command.RateExtra
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.command.SubscribeExtra
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.command.VideoExtra
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.command.vote.VoteExtra
import cn.spacexc.wearbili.remake.common.ui.BilibiliBlue
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.DanmakuChip
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import kotlinx.coroutines.delay

/**
 * Created by XC-Qan on 2023/12/10.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun DanmakuCanvas(
    modifier: Modifier = Modifier,
    state: DanmakuCanvasState,
    textMeasurer: TextMeasurer,
    playSpeed: Float = 1f,
    videoAspectRatio: Float,
    displayFrameRate: Boolean = false,
    displayAreaPercent: Float = 1f,
    blockLevel: Int = 0,
    textScale: Float = 1f,
    uploaderAvatarUrl: String = "",
    videoDisplaySurfaceSize: Size,
    isNormalDanmakuVisible: Boolean,
    isAdvanceDanmakuVisible: Boolean,
    isDebug: Boolean = false
) {
    val localDensity = LocalDensity.current
    val localContext = LocalContext.current
    var framePerSecond by remember {
        mutableIntStateOf(0)
    }
    var currentFrameRate by remember {
        mutableIntStateOf(0)
    }
    var boxSize by remember {
        mutableStateOf(DpSize(1.dp, 1.dp))
    }

    LaunchedEffect(key1 = Unit, block = {
        while (true) {
            currentFrameRate = framePerSecond
            framePerSecond = 0
            delay(1000)
        }
    })
    Box(modifier = Modifier.fillMaxSize()) {
        if (isNormalDanmakuVisible) {
            Canvas(modifier = modifier.fillMaxSize()) {
                state.displayingDanmakus.forEach { danmaku ->
                    if (danmaku.y + danmaku.textHeight <= size.height * displayAreaPercent && danmaku.y >= 0 && danmaku.weight >= blockLevel) {
                        val measuredText = textMeasurer.measure(
                            text = danmaku.content,
                            style = TextStyle(
                                fontSize = (danmaku.fontSize * 0.5 * textScale).sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = wearbiliFontFamily,
                            ),
                            layoutDirection = layoutDirection,
                            density = this@Canvas,
                        )
                        if (isDebug) {
                            translate(top = danmaku.y, left = danmaku.x) {
                                val measuredIdText = textMeasurer.measure(
                                    text = danmaku.danmakuId.toString(),
                                    style = TextStyle(
                                        fontSize = 6.sp,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = wearbiliFontFamily
                                    ),
                                    layoutDirection = layoutDirection,
                                    density = this@Canvas,
                                )
                                translate(top = -measuredIdText.size.height.toFloat()) {
                                    drawRect(
                                        size = Size(
                                            measuredIdText.size.width.toFloat(),
                                            measuredIdText.size.height.toFloat()
                                        ), color = Color.Black
                                    )
                                    drawText(
                                        textLayoutResult = measuredIdText,
                                        color = Color.White,
                                    )
                                }
                                drawRect(
                                    size = Size(
                                        measuredText.size.width.toFloat(),
                                        measuredText.size.height.toFloat()
                                    ), color = danmaku.color, style = Stroke(width = 1f)
                                )
                            }
                        }
                        if (danmaku.isLiked) {
                            drawImage(
                                image = ResourcesCompat.getDrawable(
                                    localContext.resources,
                                    R.drawable.icon_danmaku_like,
                                    localContext.theme
                                )!!.toBitmap().asImageBitmap(),
                                dstSize = IntSize(
                                    (measuredText.size.height * 0.75f).toInt(),
                                    (measuredText.size.height * 0.75f).toInt()
                                ),
                                dstOffset = IntOffset(
                                    (danmaku.x - (measuredText.size.height * 0.75f)).toInt(),
                                    danmaku.y.toInt() + 5
                                ),
                            )
                        }

                        if (danmaku.image != null) {
                            val aspectRatio = danmaku.image.width / danmaku.image.height
                            val height = measuredText.size.height
                            val width = height * aspectRatio
                            drawImage(
                                image = danmaku.image,
                                dstOffset = IntOffset(danmaku.x.toInt(), danmaku.y.toInt()),
                                dstSize = IntSize(width, height)
                            )
                        } else {
                            if (danmaku.isGradient) {
                                drawText(
                                    textLayoutResult = measuredText,
                                    topLeft = Offset(danmaku.x, danmaku.y),
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            BilibiliPink,
                                            BilibiliBlue
                                        ), tileMode = TileMode.Mirror
                                    ),
                                    //drawStyle = Stroke(10f)

                                )
                            } else {
                                drawText(
                                    textLayoutResult = measuredText,
                                    topLeft = Offset(danmaku.x, danmaku.y),
                                    color = danmaku.color,
                                    shadow = Shadow(Color.Black, blurRadius = 1.5f)
                                )
                            }
                        }
                    }
                }
                state.updateNormalDanmaku(
                    textMeasurer = textMeasurer,
                    drawScope = this,
                    playSpeed = playSpeed,
                    blockLevel = blockLevel,
                    textScale = textScale
                )
            }
        }

        if (isAdvanceDanmakuVisible) {
            Canvas(modifier = modifier.fillMaxSize()) {
                state.displayingAdvanceDanmakus.forEach { advanceDanmaku ->
                    val measuredText = textMeasurer.measure(
                        text = advanceDanmaku.text,
                        style = TextStyle(
                            fontSize = (advanceDanmaku.textSize * 0.25 * textScale).sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = wearbiliFontFamily
                        ),
                        layoutDirection = layoutDirection,
                        density = this@Canvas,
                    )
                    rotate(
                        degrees = advanceDanmaku.rotateX,
                        pivot = Offset(advanceDanmaku.pivotX, advanceDanmaku.pivotX)
                    ) {
                        translate(
                            left = (advanceDanmaku.currentPosition.x - advanceDanmaku.pivotX)
                                    /** videoDisplaySurfaceSize.width*/
                                    + (size.width - videoDisplaySurfaceSize.width) / 2,
                            top = (advanceDanmaku.currentPosition.y - advanceDanmaku.pivotY)
                                    /** videoDisplaySurfaceSize.height*/
                                    + (size.height - videoDisplaySurfaceSize.height) / 2
                        ) {
                            if (isDebug) {
                                val measuredIdText = textMeasurer.measure(
                                    text = advanceDanmaku.danmakuId.toString(),
                                    style = TextStyle(
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = wearbiliFontFamily
                                    ),
                                    layoutDirection = layoutDirection,
                                    density = this@Canvas,
                                )
                                translate(top = -measuredIdText.size.height.toFloat()) {
                                    drawRect(
                                        size = Size(
                                            measuredIdText.size.width.toFloat(),
                                            measuredIdText.size.height.toFloat()
                                        ), color = Color.White
                                    )
                                    drawText(
                                        textLayoutResult = measuredIdText,
                                        color = Color.Black,
                                    )
                                }
                                drawRect(
                                    size = Size(
                                        measuredText.size.width.toFloat(),
                                        measuredText.size.height.toFloat()
                                    ), color = advanceDanmaku.color, style = Stroke(width = 1f)
                                )
                            }
                            drawText(
                                textLayoutResult = measuredText,
                                alpha = advanceDanmaku.currentAlpha,
                                color = advanceDanmaku.color,
                                shadow = Shadow(Color.Black, blurRadius = 1f)
                            )
                        }
                    }
                }
                state.updateAdvanceDanmaku(this, videoDisplaySurfaceSize)
            }
        }


        if (isNormalDanmakuVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(videoAspectRatio)
                    .align(
                        Alignment.Center
                    )
                    .onSizeChanged {
                        boxSize = with(localDensity) {
                            DpSize(it.width.toDp(), it.height.toDp())
                        }
                    }
            ) {
                state.commandDanmakus.forEach {
                    when (it.extra) {
                        is SubscribeExtra -> {
                            val position =
                                getCommandDanmakuPosition(it.extra.posX2, it.extra.posY2, boxSize)
                            AnimatedVisibility(
                                visible = it.isDisplaying,
                                enter = fadeIn(),
                                exit = fadeOut(),
                                modifier = Modifier.centeredOffset(position)
                            ) {
                                DanmakuChip(
                                    commandDanmakuType = it.type,
                                    uploaderAvatarUrl = uploaderAvatarUrl
                                ) {

                                }
                            }

                        }

                        is VoteExtra -> {
                            val position = getCommandDanmakuPosition(
                                it.extra.posX2,
                                it.extra.posY2,
                                boxSize
                            )
                            AnimatedVisibility(
                                visible = it.isDisplaying,
                                enter = fadeIn(),
                                exit = fadeOut(),
                                modifier = Modifier.centeredOffset(position)
                            ) {
                                DanmakuChip(commandDanmakuType = it.type) {

                                }
                            }

                        }

                        is RateExtra -> {
                            val position = getCommandDanmakuPosition(
                                it.extra.posX2,
                                it.extra.posY2,
                                boxSize
                            )
                            AnimatedVisibility(
                                visible = it.isDisplaying,
                                enter = fadeIn(),
                                exit = fadeOut(),
                                modifier = Modifier.centeredOffset(position)
                            ) {
                                DanmakuChip(commandDanmakuType = it.type) {

                                }
                            }

                        }

                        is VideoExtra -> {
                            val position = getCommandDanmakuPosition(
                                it.extra.posX2,
                                it.extra.posY2,
                                boxSize
                            )
                            AnimatedVisibility(
                                visible = it.isDisplaying,
                                enter = fadeIn(),
                                exit = fadeOut(),
                                modifier = Modifier.centeredOffset(position)
                            ) {
                                DanmakuChip(commandDanmakuType = it.type) {

                                }
                            }

                        }

                    }
                }
            }
        }

        if (displayFrameRate) {
            Text(
                text = "$currentFrameRate fps, ${state.updateTimer() / 1000f}",
                modifier = Modifier.align(
                    Alignment.BottomStart
                ),
                color = Color.White
            )
        }
    }

    framePerSecond++
}

/**
 * 将传入的坐标按比例缩放
 */
private fun getCommandDanmakuPosition(xPercent: Int, yPercent: Int, boxSize: DpSize): DpOffset {
    val x = boxSize.width * (xPercent.ifNullOrZero { 50 }.toFloat() / 100f)
    val y = boxSize.height * (yPercent.ifNullOrZero { 65 }.toFloat() / 100f)

    return DpOffset(x, y)
}

fun Modifier.centeredOffset(offset: DpOffset): Modifier {
    return graphicsLayer {
        translationX = size.width / -2
        translationY = size.height / 2
    }.offset(offset.x, offset.y)
}