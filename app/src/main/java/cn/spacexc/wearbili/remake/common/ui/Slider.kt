package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * Created by XC-Qan on 2023/10/18.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

/*@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun GradientSlider(
    value: Float,
    range: ClosedRange<Float>,
    modifier: Modifier = Modifier,
    onValueChanged: (Float) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        val localDensity = LocalDensity.current
        var width by remember {
            mutableIntStateOf(1)
        }
        var currentOffsetX by remember {
            mutableFloatStateOf(0f)
        }
        //expected 372 actual 321
        //expected 308 actual 260
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .clip(CircleShape)
                .background(Color(38, 38, 38, 128))
                .onSizeChanged { size ->
                    width = size.width
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(onDragStart = { offset ->
                        offset.logd("start offset")
                        currentOffsetX = offset.x
                    }) { change, _ ->
                        val targetOffset = change.position.x - 25
                        change.consume()
                        width.logd("width")
                        targetOffset.logd("targetOffset")
                        if (targetOffset in 0f..width.toFloat()) {
                            currentOffsetX = targetOffset
                        }
                    }
                }
        ) {
            val offsetDp = with(localDensity) { currentOffsetX.toDp() }
            Box(
                modifier = Modifier
                    .width(offsetDp + 24.dp)
                    .height(24.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(255, 54, 121, 128),
                                Color(255, 54, 121, 255)
                            )
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .offset(
                        x = offsetDp
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        }
    }
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradientSlider(
    value: Float,
    modifier: Modifier = Modifier,
    range: ClosedFloatingPointRange<Float>,
    brush: Brush = Brush.horizontalGradient(listOf(Color(50, 25, 33), BilibiliPink)),
    trackHeight: Dp = TrackHeight,
    thumbSize: Dp = TrackHeight * 0.4f,
    onValueChanged: (Float) -> Unit
) {
    val thumbInteractionSource = rememberMutableInteractionSource()
    Slider(
        modifier = modifier,
        value = value,
        onValueChange = onValueChanged,
        valueRange = range,
        thumb = {
            Box(
                modifier = Modifier
                    .size(thumbSize)
                    .offset(x = 4.5.dp, y = 5.dp)
                    .background(Color.White, CircleShape)
            )
        },
        track = { state ->
            Track(sliderState = state, brush = brush, height = trackHeight)
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradientSlider(
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    brush: Brush = Brush.horizontalGradient(listOf(Color(50, 25, 33), BilibiliPink)),
    trackHeight: Dp = TrackHeight,
    thumbSize: Dp = TrackHeight * 0.4f,
    onValueChanged: (Float) -> Unit,
    onSlideFinished: () -> Unit
) {
    Slider(
        modifier = modifier,
        value = value,
        onValueChange = onValueChanged,
        valueRange = range,
        thumb = {
            Box(
                modifier = Modifier
                    .size(thumbSize)
                    .offset(x = 4.5.dp, y = 5.dp)
                    .background(Color.White, CircleShape)
            )
        },
        track = { state ->
            Track(sliderState = state, brush = brush, height = trackHeight)
        },
        onValueChangeFinished = {
            onSlideFinished()
        }
    )
}

val TrackHeight = 24.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Track(
    sliderState: SliderState,
    modifier: Modifier = Modifier,
    height: Dp = TrackHeight,
    brush: Brush = Brush.horizontalGradient(listOf(Color(50, 25, 33), BilibiliPink))
) {

    Canvas(
        modifier
            .fillMaxWidth()
            .height(height)
    ) {
        val coercedValueAsFraction = with(sliderState) {
            calcFraction(
                valueRange.start,
                valueRange.endInclusive,
                value.coerceIn(valueRange.start, valueRange.endInclusive)
            )
        }
        drawTrack(
            0f,
            height,
            coercedValueAsFraction,
            brush
        )
    }
}

fun calcFraction(a: Float, b: Float, pos: Float) =
    (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)

fun DrawScope.drawTrack(
    activeRangeStart: Float,
    height: Dp,
    activeRangeEnd: Float,
    brush: Brush = Brush.horizontalGradient(listOf(Color(50, 25, 33), BilibiliPink))
) {
    val isRtl = layoutDirection == LayoutDirection.Rtl
    val sliderLeft = Offset(0f, center.y)
    val sliderRight = Offset(size.width, center.y)
    val sliderStart = if (isRtl) sliderRight else sliderLeft
    val sliderEnd = if (isRtl) sliderLeft else sliderRight
    val trackStrokeWidth = height.toPx()
    drawLine(
        Color(38, 38, 38, 128),
        sliderStart,
        sliderEnd,
        trackStrokeWidth,
        StrokeCap.Round
    )
    val sliderValueEnd = Offset(
        sliderStart.x +
                (sliderEnd.x - sliderStart.x) * activeRangeEnd,
        center.y
    )

    val sliderValueStart = Offset(
        sliderStart.x +
                (sliderEnd.x - sliderStart.x) * activeRangeStart,
        center.y
    )

    drawLine(
        brush,
        sliderValueStart,
        sliderValueEnd,
        trackStrokeWidth,
        StrokeCap.Round
    )
}

@Preview
@Composable
private fun SliderPreview() {
    var sliderValue by remember {
        mutableFloatStateOf(0.5f)
    }
    GradientSlider(value = sliderValue, range = 0f..1f) {
        sliderValue = it
    }
}