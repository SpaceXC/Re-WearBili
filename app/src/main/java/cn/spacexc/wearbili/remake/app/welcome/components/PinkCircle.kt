package cn.spacexc.wearbili.remake.app.welcome.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

enum class CirclePosition {
    TOP_START,
    BOTTOM_END
}

@Composable
fun PinkCircle(
    position: CirclePosition
) {
    val localDensity = LocalDensity.current
    var circleSize by remember {
        mutableStateOf(DpSize(0.dp, 0.dp))
    }
    Box(
        modifier = Modifier
            .offset(
                x = circleSize.width * (if (position == CirclePosition.TOP_START) -0.4f else 0.5f),
                y = circleSize.height * (if (position == CirclePosition.TOP_START) -0.7f else 0.55f)
            )
            .fillMaxHeight()
            .aspectRatio(1f)
            .onSizeChanged {
                circleSize = with(localDensity) {
                    DpSize(
                        width = it.width.toDp(),
                        height = it.height.toDp()
                    )
                }
            }
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(212, 78, 125),
                        Color.Transparent
                    ),
                    start = if (position == CirclePosition.TOP_START) Offset.Zero else Offset.Infinite,
                    end = if (position == CirclePosition.TOP_START) Offset.Infinite else Offset.Zero,
                ),
                shape = CircleShape
            )

    )
}