package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.common.domain.log.logd

/**
 * Created by XC-Qan on 2023/10/18.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
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
}