package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import kotlinx.coroutines.launch
import like.BubbleColor
import like.CircleColor
import like.LikeButton
import like.LikeButtonState

/**
 * Created by XC-Qan on 2023/4/24.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun LargeRoundButton(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    background: Color = Color(41, 41, 41),
    text: String,
    iconColor: Color = Color.White,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickVfx { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            tint = iconColor,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(background, CircleShape)
                .padding(vertical = 14.dp)
        )
        Text(text = text, style = AppTheme.typography.body2)
    }
}

@Composable
fun OutlinedRoundButton(
    modifier: Modifier = Modifier,
    buttonModifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    text: String,
    clickable: Boolean = true,
    onClick: () -> Unit = { },
    onLongClick: () -> Unit = { },
    interactionSource: MutableInteractionSource = rememberMutableInteractionSource(),
    icon: @Composable BoxScope.() -> Unit,
) {
    val alpha by wearBiliAnimateFloatAsState(
        targetValue = if (clickable) 1f else 0.4f
    )
    Column(
        modifier = modifier
            .clickVfx(
                enabled = true,
                onClick = onClick,
                onLongClick = onLongClick,
                interactionSource = interactionSource
            )
            .alpha(alpha),
        /*.height(IntrinsicSize.Max)*//*.wrapContentHeight()*/
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            innerPaddingValues = PaddingValues(3.dp),
            modifier = buttonModifier,
            /*.fillMaxSize()*/
            shape = shape,
            outerPaddingValues = PaddingValues(2.dp),
            isClickEnabled = clickable,
        ) {
            icon()
        }
        if (text.isNotEmpty()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = text,
                fontFamily = wearbiliFontFamily,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

@Composable
fun VfxOutlinedRoundButton(
    likeButtonState: LikeButtonState,
    modifier: Modifier = Modifier,
    buttonModifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    count: Int,
    clickable: Boolean = true,
    onClick: () -> Boolean = { false },
    onLongClick: () -> Unit = { },
    outlineProgress: Float = 0f,
    interactionSource: MutableInteractionSource = rememberMutableInteractionSource(),
    icon: @Composable BoxScope.(Boolean) -> Unit,
) {
    val alpha by wearBiliAnimateFloatAsState(
        targetValue = if (clickable) 1f else 0.4f
    )
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .clickVfx(
                enabled = true,
                onClick = {
                    val hasVfx = onClick()
                    scope.launch {
                        if (hasVfx) {
                            if (likeButtonState.isLiked.value) likeButtonState.unlike() else likeButtonState.like(
                                scope
                            )
                        } else {
                            if (likeButtonState.isLiked.value) likeButtonState.unlike() else likeButtonState.likeWithoutAnimation()
                        }
                    }
                },
                onLongClick = onLongClick,
                interactionSource = interactionSource
            )
            .alpha(alpha),
        /*.height(IntrinsicSize.Max)*//*.wrapContentHeight()*/
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            innerPaddingValues = PaddingValues(3.dp),
            modifier = buttonModifier.drawWithContent {
                drawContent()
                val stroke = Stroke(7f)
                val diameterOffset = stroke.width / 2
                val arcDimen = size.width - 2 * diameterOffset
                drawArc(
                    color = BilibiliPink,
                    startAngle = -90f,
                    sweepAngle = outlineProgress * -360f,
                    useCenter = false,
                    topLeft = Offset(diameterOffset, diameterOffset),
                    size = Size(arcDimen, arcDimen),
                    style = stroke
                )
            },
            /*.fillMaxSize()*/
            shape = shape,
            outerPaddingValues = PaddingValues(2.dp),
            isClickEnabled = clickable,
        ) {
            LikeButton(
                likeButtonState = likeButtonState,
                bubbleColor = BubbleColor(
                    dotPrimaryColor = BilibiliPink,
                    dotSecondaryColor = BilibiliPink,
                    dotThirdColor = BilibiliPink,
                    dotLastColor = BilibiliPink
                ),
                circleColor = CircleColor(BilibiliPink, BilibiliPink),
                likeContent = { isLiked ->
                    icon(isLiked)
                },
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        AnimatedCounter(
            count = count, style = TextStyle(
                fontFamily = wearbiliFontFamily,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                fontSize = 12.sp
            )
        )
    }
}