package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import cn.spacexc.wearbili.common.domain.log.logd

/**
 * Created by XC-Qan on 2023/11/19.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun ImageWithZoomAndPan(
    imageUrl: String,
) {
    var scale by remember {
        mutableFloatStateOf(1f)
    }
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }

    val animatedScale by animateFloatAsState(targetValue = scale)
    val animatedOffset by animateOffsetAsState(targetValue = offset)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val state = rememberTransformableState { zoomChange, panChange, _ ->
            scale = (scale * zoomChange).coerceIn(1f, 5f)

            val extraWidth = (scale - 1) * constraints.maxWidth
            val extraHeight = (scale - 1) * constraints.maxHeight

            val maxX = extraWidth / 2
            val maxY = extraHeight / 2

            offset = Offset(
                x = (offset.x + scale * panChange.x).coerceIn(-maxX, maxX),
                y = (offset.y + scale * panChange.y).coerceIn(-maxY, maxY),
            )
        }
        BiliImage(
            url = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = animatedScale
                    scaleY = animatedScale
                    translationX = animatedOffset.x
                    translationY = animatedOffset.y
                }
                .transformable(state)
                .pointerInput(Unit) {
                    detectTapGestures(onDoubleTap = { tapOffset ->
                        if (scale + 2.5f < 5f) {
                            scale += 2.5f
                        } else if (scale >= 5f) {
                            scale = 1f
                        } else {
                            scale = 5f
                        }
                        tapOffset.logd("tapOffset")
                        offset.logd("current offset")

                        val targetOffset = Offset(
                            x = constraints.maxWidth / 2 - tapOffset.x,
                            y = constraints.maxHeight / 2 - tapOffset.y
                        )

                        val extraWidth = (scale - 1) * constraints.maxWidth
                        val extraHeight = (scale - 1) * constraints.maxHeight

                        val maxX = extraWidth / 2
                        val maxY = extraHeight / 2

                        offset = Offset(
                            x = (offset.x + scale * targetOffset.x).coerceIn(-maxX, maxX),
                            y = (offset.y + scale * targetOffset.y).coerceIn(-maxY, maxY),
                        )
                    })
                }
        )
    }
}