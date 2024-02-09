package cn.spacexc.wearbili.remake.app.welcome.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
fun OffsetBigIcon(
    imageVector: ImageVector,
    offset: Offset
) {
    val localDensity = LocalDensity.current
    var iconSize by remember {
        mutableStateOf(DpSize(0.dp, 0.dp))
    }
    Icon(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .aspectRatio(1f)
            .onSizeChanged {
                iconSize = with(localDensity) {
                    DpSize(it.width.toDp(), it.height.toDp())
                }
            }
            .offset(iconSize.width * offset.x * -1, iconSize.height * offset.y * -1)
            .graphicsLayer(alpha = 0.99f)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawRect(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(212, 78, 125),
                                Color.Transparent
                            ),
                            start = Offset.Zero,
                            end = Offset.Infinite,
                        ), blendMode = BlendMode.SrcAtop
                    )
                }
            },
        imageVector = imageVector,
        contentDescription = null,
    )
}