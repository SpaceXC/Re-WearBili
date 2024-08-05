package cn.spacexc.wearbili.remake.app.player.audio.ui.lyrics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness1
import androidx.compose.material.ripple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import cn.spacexc.bilibilisdk.sdk.video.info.remote.subtitle.Subtitle
import cn.spacexc.wearbili.remake.app.settings.LocalConfiguration
import cn.spacexc.wearbili.remake.app.settings.experimantal.EXPERIMENTAL_FADE_SUBTITLE
import cn.spacexc.wearbili.remake.app.settings.experimantal.getActivatedExperimentalFunctions
import cn.spacexc.wearbili.remake.common.ui.WearBiliAnimatedVisibility
import cn.spacexc.wearbili.remake.common.ui.rememberMutableInteractionSource
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateFloatAsState
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout


@JvmInline
@Immutable
private value class ItemInfo(val packedValue: Long) {
    val offsetY: Int get() = unpackInt1(packedValue)

    val height: Int get() = unpackInt2(packedValue)
}

private fun ItemInfo(offsetY: Int, height: Int): ItemInfo {
    return ItemInfo(packInts(offsetY, height))
}

fun packInts(val1: Int, val2: Int): Long {
    return val1.toLong().shl(32) or (val2.toLong() and 0xFFFFFFFF)
}

/**
 * Unpacks the first Int value in [packInts] from its returned ULong.
 */
fun unpackInt1(value: Long): Int {
    return value.shr(32).toInt()
}

/**
 * Unpacks the second Int value in [packInts] from its returned ULong.
 */
fun unpackInt2(value: Long): Int {
    return value.and(0xFFFFFFFF).toInt()
}

@Composable
fun LyricsView(
    //state: LyricsViewState,
    subtitles: List<Subtitle>,
    currentIndex: Int,
    currentTime: Long,
    modifier: Modifier = Modifier,
    secondarySubtitleText: String?,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    contentColor: Color,
    fadingEdges: FadingEdges = FadingEdges.None,
    fontSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    lineHeight: TextUnit = 1.2.em,
    onClick: (Subtitle) -> Unit
) {
    val scope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    var lyricsHeight by remember { mutableIntStateOf(0) }

    val itemsInfo = remember { mutableMapOf<Int, ItemInfo>() }

    var initialItemsOffsetY by remember { mutableIntStateOf(0) }

    var currItemsOffsetY by remember { mutableStateOf(0) }

    var animationItemsRange by remember { mutableStateOf(-1..-1) }

    var itemsAnimationJob by remember { mutableStateOf<Job?>(null) }

    fun updateItemInfo(index: Int, itemCoordinates: LayoutCoordinates) {
        itemsInfo[index] = ItemInfo(
            offsetY = itemCoordinates.positionInParent().y.toInt(),
            height = itemCoordinates.size.height,
        )
    }

    fun getAnimationItemsRange(currentIndex: Int): IntRange {
        val currItemInfo = itemsInfo[currentIndex] ?: return -1..-1
        val scrollY = scrollState.value
        var start = -1
        var end = -1
        for (i in subtitles.indices) {
            val itemInfo = itemsInfo[i] ?: continue

            val itemTop = itemInfo.offsetY
            val itemHeight = itemInfo.height
            val itemBottom = itemTop + itemHeight

            if (itemBottom < scrollY) {
                continue
            } else if (start == -1) {
                start = i
            }

            if (itemTop > currItemInfo.offsetY + lyricsHeight) {
                break
            } else {
                end = i
            }
        }
        return start..end
    }

    fun getItemOffsetY(index: Int): Int {
        return if (index in animationItemsRange) {
            val value = currItemsOffsetY
            if (index > currentIndex) {
                // These lines produce the animation delay
                val factor = (1f + (index - currentIndex) * 0.08f)
                val progress = currItemsOffsetY.toFloat() / initialItemsOffsetY
                val finalProgress = (progress * factor).coerceAtMost(1f)
                (initialItemsOffsetY * finalProgress).toInt()
            } else {
                value
            }
        } else {
            0
        }
    }

    fun startItemsAnimation(targetItemIndex: Int) {
        if (targetItemIndex < 0) return
        if (subtitles[targetItemIndex].content == "[BLANK_SUSPEND]") return
        val targetItemTop = itemsInfo[targetItemIndex]?.offsetY ?: return
        itemsAnimationJob?.cancel()
        itemsAnimationJob = scope.launch {
            val targetScrollY = targetItemTop.coerceAtMost(scrollState.maxValue)
            val diff = targetScrollY - scrollState.value
            // 1) Find items to animate
            animationItemsRange = getAnimationItemsRange(targetItemIndex)

            // 2) Scroll the lyrics to the target position
            scrollState.scrollTo(targetScrollY)

            // 3) Apply an offset to items so the lyric looks like it hasn't moved
            Snapshot.withoutReadObservation { initialItemsOffsetY = diff }
            currItemsOffsetY = diff

            // 4) Animate items to the target position
            animate(
                initialValue = diff.toFloat(),
                targetValue = 0f,
                animationSpec = tween(durationMillis = 1000),
            ) { value, _ ->
                currItemsOffsetY = value.toInt()
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        startItemsAnimation(currentIndex)
    }

    LaunchedEffect(scrollState, currentIndex) {
        snapshotFlow { currentIndex }
            .filter { it >= 0 }
            .collect(::startItemsAnimation)
    }

    Box(
        modifier = modifier
            .fadingEdges(edges = fadingEdges)
            .onSizeChanged { lyricsHeight = it.height },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .padding(contentPadding),
        ) {
            for ((index, line) in subtitles.withIndex()) {
                LyricsViewLine(
                    isActive = index == currentIndex,
                    timeLength = line.to - line.from,
                    startTime = line.from,
                    currentTime = currentTime.toDouble() / 1000,
                    content = line.content,
                    contentColor = contentColor,
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    lineHeight = lineHeight,
                    onClick = { onClick(line) },
                    offsetYProvider = { getItemOffsetY(index) },
                    modifier = Modifier.onGloballyPositioned { updateItemInfo(index, it) },
                    secondarySubtitleText = secondarySubtitleText
                )
            }
        }
    }
}

@Composable
private fun LyricsViewLine(
    isActive: Boolean,
    timeLength: Double,
    startTime: Double,
    currentTime: Double,
    content: String,
    contentColor: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    lineHeight: TextUnit,
    onClick: () -> Unit,
    offsetYProvider: () -> Int,
    modifier: Modifier = Modifier,
    activeScale: Float = 1.06f,
    inactiveScale: Float = 1f,
    activeAlpha: Float = 1f,
    inactiveAlpha: Float = 0.5f,
    activeShadow: Float = 1f,
    inactiveShadow: Float = 0f,
    secondarySubtitleText: String?
) {
    var scale by remember { mutableStateOf(if (isActive) activeScale else inactiveScale) }
    val offset by animateFloatAsState(targetValue = if (isActive) 0f else -5f, label = "")
    var alpha by remember { mutableStateOf(if (isActive) activeAlpha else inactiveAlpha) }
    var shadow by remember { mutableStateOf(if (isActive) activeShadow else inactiveShadow) }

    val interactionSource = rememberMutableInteractionSource()

    val indication = ripple(color = contentColor)//rememberRipple(color = contentColor)


    LaunchedEffect(isActive) {
        launch {
            animate(
                initialValue = scale,
                targetValue = if (isActive) activeScale else inactiveScale,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow,
                )
            ) { value, _ ->
                scale = value
            }
        }
        launch {
            // Composable could suddenly go invisible for one frame (or few frame?) when
            // isActive changes to false and the alpha animation starts. Delay may help
            // to reduce these glitches
            repeat(10) { awaitFrame() }
            animate(
                initialValue = alpha,
                targetValue = if (isActive) activeAlpha else inactiveAlpha,
            ) { value, _ ->
                alpha = value
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .offset { IntOffset(0, offsetYProvider()) }
            .clip(MaterialTheme.shapes.medium)
            .indication(interactionSource, indication)
            .pointerInput(interactionSource) {
                detectTapGestures(
                    onPress = {
                        val press = PressInteraction.Press(it)
                        try {
                            // Do not show indications (ripples) if the tap is done in 100ms since
                            // ripple animations will impact the performance of other animations
                            withTimeout(timeMillis = 100) {
                                tryAwaitRelease()
                            }
                        } catch (e: TimeoutCancellationException) {
                            interactionSource.emit(press)
                            tryAwaitRelease()
                        }
                        interactionSource.emit(PressInteraction.Release(press))
                    },
                    onTap = { onClick() },
                )
            },
        /*.padding(
            start = 8.dp, top = 8.dp, bottom = 8.dp, end = 14.dp
        )*/
    ) {

        if (content == "[BLANK_SUSPEND]") {
            AnimatedVisibility(
                visible = isActive,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                BlankSuspend(
                    timeLength = timeLength,
                    startTime = startTime,
                    currentTime = currentTime,
                    modifier = Modifier
                        .alpha(alpha)
                        .graphicsLayer {
                            transformOrigin = TransformOrigin(0f, 0f)
                            scaleX = scale
                            scaleY = scale
                        }
                        .padding(
                            start = 4.dp, top = 8.dp, bottom = 8.dp, end = 14.dp
                        )
                )
            }
        } else {
            LyricsLine(
                fontSize = fontSize,
                fontWeight = fontWeight,
                text = content,
                timeLength = timeLength,
                startTime = startTime,
                currentTime = currentTime,
                color = contentColor,
                //offset = offset,
                isActive = isActive,
                notation = secondarySubtitleText,
                modifier = Modifier
                    .alpha(alpha)
                    .graphicsLayer {
                        transformOrigin = TransformOrigin(0f, 0f)
                        scaleX = scale
                        scaleY = scale
                    }
                    .padding(
                        start = 8.dp, top = 12.dp, bottom = 4.dp, end = 14.dp
                    )
            )
        }
        /*Text(
            text = content,
            modifier = Modifier
                .graphicsLayer {
                    transformOrigin = TransformOrigin(0f, 1f)
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                },
            color = contentColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            lineHeight = lineHeight,
            fontFamily = wearbiliFontFamily
        )*/
    }
}

@Composable
fun BlankSuspend(
    modifier: Modifier = Modifier,
    timeLength: Double,
    startTime: Double,
    currentTime: Double
) {
    val lineLength = 3
    val elapsedTime = currentTime - startTime
    val timePerText = timeLength.toFloat() / lineLength.toFloat()
    val currentLetterIndex = elapsedTime.toFloat() / timePerText

    Row(modifier = modifier) {
        repeat(3) { index ->
            val factor =
                (currentLetterIndex - index.toDouble()).coerceIn(0.0..1.0).toFloat()
            val infiniteTransition = rememberInfiniteTransition(label = "")
            val offset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -10f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1200),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(index * 400)
                ),
                label = ""//tween<Float>(durationMillis = 800, delayMillis = index * 200)
            )
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.5f,
                targetValue = 0.7f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1200),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(index * 400)
                ),
                label = ""//tween<Float>(durationMillis = 800, delayMillis = index * 200)
            )
            Box(modifier = Modifier.graphicsLayer {
                //translationY = offset
                scaleY = scale
                scaleX = scale
            }) {
                Icon(
                    imageVector = Icons.Default.Brightness1,
                    contentDescription = null,
                    modifier = Modifier
                        .graphicsLayer {
                            compositingStrategy = CompositingStrategy.Offscreen
                        }
                        .drawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.horizontalGradient(
                                    factor * 2f - 1f to Color.Black,
                                    factor * 2f to Color.Transparent
                                ),
                                blendMode = BlendMode.DstIn
                            )
                        },
                    tint = Color.White
                )
                Icon(
                    imageVector = Icons.Default.Brightness1,
                    contentDescription = null,
                    modifier = Modifier.alpha(0.2f),
                    tint = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LyricsLine(
    modifier: Modifier = Modifier,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    color: Color,
    text: String,
    timeLength: Double,
    isActive: Boolean,
    startTime: Double,
    currentTime: Double,
    notation: String?
) {
    val configuration = LocalConfiguration.current
    val hasSubtitleFadeAnimation by remember {
        derivedStateOf {
            configuration.getActivatedExperimentalFunctions().contains(
                EXPERIMENTAL_FADE_SUBTITLE
            )
        }
    }


    Column {
        if (hasSubtitleFadeAnimation) {
            val lineLength = text.length
            val elapsedTime = currentTime - startTime
            val timePerText = timeLength / lineLength.toDouble()
            val currentLetterIndex = elapsedTime.toFloat() / timePerText

            FlowRow(modifier = modifier) {
                text.forEachIndexed { index, char ->
                    val factor = (currentLetterIndex - index.toDouble()).coerceIn(0.0..1.0)
                    val offset by animateFloatAsState(
                        targetValue = if (currentLetterIndex < index) 0f else -1.5f,
                        label = "",
                        animationSpec = tween((timePerText * 1000).toInt() * 2, easing = EaseInOutCubic)
                    )

                    BasicText(
                        text = char.toString(),
                        style = TextStyle(
                            fontSize = fontSize,
                            fontWeight = fontWeight,
                            color = color,
                            textMotion = TextMotion.Animated,
                            fontFamily = wearbiliFontFamily
                        ),
                        modifier = Modifier
                            //.alpha(alpha.toFloat())
                            .graphicsLayer {
                                compositingStrategy = CompositingStrategy.Offscreen
                                translationY = offset
                            }
                            .drawWithContent {
                                drawContent()
                                drawRect(
                                    brush = Brush.horizontalGradient(
                                        factor.toFloat() * 2f - 1f to Color.Black,
                                        factor.toFloat() * 2f to Color(0, 0, 0, 50)
                                    ),
                                    blendMode = BlendMode.DstIn
                                )
                            },
                    )

                    /*Text(
                        char.toString(),
                        fontSize = fontSize,
                        fontWeight = fontWeight,
                        modifier = Modifier
                            //.alpha(alpha.toFloat())
                            .graphicsLayer {
                                compositingStrategy = CompositingStrategy.Offscreen
                                translationY = offset
                            }
                            .drawWithContent {
                                drawContent()
                                drawRect(
                                    brush = Brush.horizontalGradient(
                                        factor.toFloat() * 2f - 1f to Color.Black,
                                        factor.toFloat() * 2f to Color(0, 0, 0, 50)
                                    ),
                                    blendMode = BlendMode.DstIn
                                )
                            },
                        fontFamily = wearbiliFontFamily,
                        color = color,
                        style = TextStyle(textMotion = TextMotion.Animated)
                    )*/
                }
            }
        } else {
            val alpha by wearBiliAnimateFloatAsState(targetValue = if (isActive) 1f else 0.3f)
            Text(
                text,
                fontSize = fontSize,
                fontWeight = fontWeight,
                modifier = modifier.alpha(alpha),
                fontFamily = wearbiliFontFamily,
                color = color
            )
        }
        //val alpha by wearBiliAnimateFloatAsState(targetValue = if (isActive) 0.5f else 0.3f)
        WearBiliAnimatedVisibility(
            modifier = Modifier
                .offset(y = (-4).dp)
                .fillMaxWidth(),
            visible = notation != null && isActive,
            enter = fadeIn(tween(delayMillis = 100)) + expandVertically(tween(delayMillis = 100)),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                notation ?: "",
                fontSize = fontSize * 0.7f,
                fontWeight = FontWeight.Medium,
                modifier = modifier.alpha(0.5f),
                fontFamily = wearbiliFontFamily,
                color = color
            )
        }
    }
}