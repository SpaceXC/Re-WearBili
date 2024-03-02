package like

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import like.painter.drawBubble
import like.painter.drawLikeCircle

@Composable
fun LikeButton(
    modifier: Modifier = Modifier,
    likeButtonState: LikeButtonState = rememberLikeButtonState(),
    size: Dp = 30.dp,
    likeContent: @Composable (isLiked: Boolean) -> Unit = {
        DefaultContent(
            isLike = it,
            size = size,
        )
    },
    circleSize: Dp = size * 0.8,
    bubblesCount: Int = 7,
    bubbleColor: BubbleColor = BubbleColor(
        dotPrimaryColor = Color(0xFFFFC107),
        dotSecondaryColor = Color(0xFFFF9800),
        dotThirdColor = Color(0xFFFF5722),
        dotLastColor = Color(0xFFF44336),
    ),
    circleColor: CircleColor = CircleColor(
        start = Color(0xFFFF5722),
        end = Color(0xFFFFC107),
    ),
    //onTap: (suspend () -> Unit)? = null,
) {

    /*val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }*/

    Box(
        modifier = modifier
            .size(size)
        /*.clickable(
          interactionSource = interactionSource,
          indication = null,
        ) {
          onTap?.let {
            coroutineScope.launch {
              it.invoke()
            }
          } ?: coroutineScope.launch {
            if (likeButtonState.isLiked.value) {
              likeButtonState.unlike()
            } else {
              likeButtonState.like(coroutineScope)
            }
          }
        }*/
    ) {
        Box(
            modifier = Modifier
                .size(
                    likeButtonState.scaleAnimation.value * size
                )
                .padding(
                    start = (size - likeButtonState.scaleAnimation.value * size) / 2,
                    top = (size - likeButtonState.scaleAnimation.value * size) / 2,
                )
        ) {
            likeContent(likeButtonState.isLiked.value)
        }

        Box(
            modifier = Modifier
                .size(circleSize)
                .padding(
                    start = (size - circleSize) / 2,
                    top = (size - circleSize) / 2,
                )
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawLikeCircle(
                    innerCircleRadiusProgress = likeButtonState.innerCircleAnimation.value,
                    outerCircleRadiusProgress = likeButtonState.outerCircleAnimation.value,
                    size = circleSize,
                    circleColor = circleColor
                )
            }
        }
        Canvas(modifier = Modifier.fillMaxSize()) {
            translate(-size.toPx() / 2, -size.toPx() / 2) {
                drawBubble(
                    size = Size(size.toPx() * 2, size.toPx() * 2),
                    currentProgress = likeButtonState.bubblesAnimation.value,
                    bubblesCount = bubblesCount,
                    bubbleColor = bubbleColor,
                )
            }
        }
    }
}
