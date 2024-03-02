package like.painter

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.lerp
import like.BubbleColor
import like.degToRad
import like.forEach
import like.mapValueFromRangeToRange
import kotlin.math.cos
import kotlin.math.sin

fun DrawScope.drawBubble(
    size: Size,
    currentProgress: Float,
    bubblesCount: Int = 7,
    bubbleColor: BubbleColor = BubbleColor(
        dotPrimaryColor = Color(0xFFFFC107),
        dotSecondaryColor = Color(0xFFFF9800),
        dotThirdColor = Color(0xFFFF5722),
        dotLastColor = Color(0xFFF44336),
    )
) {

    val outerBubblesPositionAngle = 360.0f / bubblesCount
    val centerX = size.width * 0.5f
    val centerY = size.height * 0.5f
    val maxDotSize = size.width * 0.05f
    val maxOuterDotsRadius = size.width * 0.5f - maxDotSize * 2
    val maxInnerDotRadius = 0.8f * maxOuterDotsRadius

    // parse1
    val currentRadius1 = if (currentProgress < 0.3) {
        mapValueFromRangeToRange(
            currentProgress, 0.0f, 0.3f, 0.0f, (maxOuterDotsRadius * 0.8f)
        )
    } else {
        mapValueFromRangeToRange(
            currentProgress, 0.3f, 1.0f, 0.8f * maxOuterDotsRadius, maxOuterDotsRadius
        )
    }
    val currentDotSize1 = if (currentProgress == 0.0f) {
        0.0f
    } else if (currentProgress < 0.7f) {
        maxDotSize
    } else {
        mapValueFromRangeToRange(
            currentProgress, 0.7f, 1.0f, maxDotSize, 0.0f
        )
    }

    // parse2
    val currentRadius2 = if (currentProgress < 0.3) {
        mapValueFromRangeToRange(
            currentProgress, 0.0f, 0.3f, 0.0f, maxInnerDotRadius
        )
    } else {
        maxInnerDotRadius
    }
    val currentDotSize2 = if (currentProgress == 0.0f) {
        0.0f
    } else if (currentProgress < 0.2f) {
        maxDotSize
    } else if (currentProgress < 0.5f) {
        mapValueFromRangeToRange(
            currentProgress, 0.2f, 0.5f, maxDotSize, 0.3f * maxDotSize
        )
    } else {
        mapValueFromRangeToRange(
            currentProgress, 0.5f, 1.0f, maxDotSize * 0.3f, 0.0f
        )
    }

    // parse3
    val progress = currentProgress.coerceIn(0.6f, 1.0f)
    val alpha = mapValueFromRangeToRange(progress, 0.6f, 1.0f, 1.0f, 0.0f)
    val colors = if (currentProgress < 0.5) {
        val innerProgress = mapValueFromRangeToRange(currentProgress, 0.0f, 0.5f, 0.0f, 1.0f)
        BubbleColor(
            dotPrimaryColor = lerp(
                bubbleColor.dotPrimaryColor, bubbleColor.dotSecondaryColor, innerProgress
            ).copy(alpha = alpha),
            dotSecondaryColor = lerp(
                bubbleColor.dotSecondaryColor, bubbleColor.dotThirdColor, innerProgress
            ).copy(alpha = alpha),
            dotThirdColor = lerp(
                bubbleColor.dotThirdColor, bubbleColor.dotLastColor, innerProgress
            ).copy(alpha = alpha),
            dotLastColor = lerp(
                bubbleColor.dotLastColor, bubbleColor.dotPrimaryColor, innerProgress
            ).copy(alpha = alpha),
        )
    } else {
        val innerProgress = mapValueFromRangeToRange(currentProgress, 0.5f, 1.0f, 0.0f, 1.0f)
        BubbleColor(
            dotPrimaryColor = lerp(
                bubbleColor.dotSecondaryColor, bubbleColor.dotThirdColor, innerProgress
            ).copy(alpha = alpha),
            dotSecondaryColor = lerp(
                bubbleColor.dotThirdColor, bubbleColor.dotLastColor, innerProgress
            ).copy(alpha = alpha),
            dotThirdColor = lerp(
                bubbleColor.dotLastColor, bubbleColor.dotPrimaryColor, innerProgress
            ).copy(alpha = alpha),
            dotLastColor = lerp(
                bubbleColor.dotPrimaryColor, bubbleColor.dotSecondaryColor, innerProgress
            ).copy(alpha = alpha),
        )
    }

    // parse4
    drawOuter(
        centerX = centerX,
        centerY = centerY,
        currentRadius1 = currentRadius1,
        currentDotSize1 = currentDotSize1,
        outerBubblesPositionAngle = outerBubblesPositionAngle,
        bubblesCount = bubblesCount,
        bubbleColor = colors
    )

    // parse5
    drawInner(
        centerX = centerX,
        centerY = centerY,
        currentRadius2 = currentRadius2,
        currentDotSize2 = currentDotSize2,
        outerBubblesPositionAngle = outerBubblesPositionAngle,
        bubbleColor = colors,
        bubblesCount = bubblesCount,
    )
}

private fun DrawScope.drawInner(
    centerX: Float,
    centerY: Float,
    currentRadius2: Float,
    currentDotSize2: Float,
    outerBubblesPositionAngle: Float,
    bubblesCount: Int,
    bubbleColor: BubbleColor
) {
    val start = outerBubblesPositionAngle / 4.0f * 3.0f -
            outerBubblesPositionAngle / 2.0f
    bubblesCount.forEach {
        val cX = centerX +
                currentRadius2 *
                cos(degToRad(start + outerBubblesPositionAngle * it))
        val cY = centerY +
                currentRadius2 *
                sin(degToRad(start + outerBubblesPositionAngle * it))
        drawCircle(
            color = bubbleColor.next(),
            radius = currentDotSize2,
            center = Offset(cX.toFloat(), cY.toFloat())
        )
    }
}

private fun DrawScope.drawOuter(
    outerBubblesPositionAngle: Float,
    bubblesCount: Int,
    centerX: Float,
    centerY: Float,
    currentRadius1: Float,
    bubbleColor: BubbleColor,
    currentDotSize1: Float
) {
    val start = outerBubblesPositionAngle / 4.0f * 3.0f
    bubblesCount.forEach {
        val cx =
            centerX + currentRadius1 * cos(degToRad(start + outerBubblesPositionAngle * it)).toFloat()
        val cy =
            centerY + currentRadius1 * sin(degToRad(start + outerBubblesPositionAngle * it)).toFloat()
        drawCircle(
            color = bubbleColor.next(),
            center = Offset(cx, cy),
            radius = currentDotSize1,
        )
    }
}
