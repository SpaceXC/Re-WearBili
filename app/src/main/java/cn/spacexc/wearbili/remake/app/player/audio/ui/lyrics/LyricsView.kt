package cn.spacexc.wearbili.remake.app.player.audio.ui.lyrics

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import cn.spacexc.bilibilisdk.sdk.video.info.remote.subtitle.Subtitle
import cn.spacexc.wearbili.remake.common.ui.rememberMutableInteractionSource
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
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
    modifier: Modifier = Modifier,
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

    var lyricsHeight by remember { mutableStateOf(0) }

    val itemsInfo = remember { mutableMapOf<Int, ItemInfo>() }

    var initialItemsOffsetY by remember { mutableStateOf(0) }

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
        val lines = subtitles
        val currItemInfo = itemsInfo[currentIndex] ?: return -1..-1
        val scrollY = scrollState.value
        var start = -1
        var end = -1
        for (i in lines.indices) {
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
                    content = line.content,
                    contentColor = contentColor,
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    lineHeight = lineHeight,
                    onClick = { onClick(line) },
                    offsetYProvider = { getItemOffsetY(index) },
                    modifier = Modifier.onGloballyPositioned { updateItemInfo(index, it) },
                )
            }
        }
    }
}

@Composable
private fun LyricsViewLine(
    isActive: Boolean,
    content: String,
    contentColor: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    lineHeight: TextUnit,
    onClick: () -> Unit,
    offsetYProvider: () -> Int,
    modifier: Modifier = Modifier,
    activeScale: Float = 1.1f,
    inactiveScale: Float = 1f,
    activeAlpha: Float = 1f,
    inactiveAlpha: Float = 0.35f,
) {
    var scale by remember { mutableStateOf(if (isActive) activeScale else inactiveScale) }
    var alpha by remember { mutableStateOf(if (isActive) activeAlpha else inactiveAlpha) }

    val interactionSource = rememberMutableInteractionSource()
    val indication = rememberRipple(color = contentColor)

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
            }
            .padding(
                start = 8.dp, top = 8.dp, bottom = 8.dp, end = 14.dp
            ),
    ) {
        Text(
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
        )
    }
}