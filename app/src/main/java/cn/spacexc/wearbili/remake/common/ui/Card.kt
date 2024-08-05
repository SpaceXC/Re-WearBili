package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by XC-Qan on 2023/3/4.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

val CardBorderColor = Color(54, 54, 54, 255)
val CardBorderWidth = 0.4f.dp
val CardBackgroundColor = Color(38, 38, 38, 77)

@Composable
fun Card(
    modifier: Modifier = Modifier,
    isClickEnabled: Boolean = true,
    shape: Shape = RoundedCornerShape(10.dp),
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    borderColor: Color = Color(54, 54, 54, 255),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .padding(/*horizontal = 8.dp, */vertical = 4.dp)
            .clickVfx(
                enabled = isClickEnabled && (onClick != null || onLongClick != null),
                onClick = {
                    onClick?.invoke()
                },
                onLongClick = {
                    onLongClick?.invoke()
                })
            .clip(shape)
            .border(
                width = 0.3f.dp,
                shape = shape,
                brush = Brush.linearGradient(
                    listOf(
                        borderColor,
                        Color.Transparent
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
            .background(color = Color(38, 38, 38, 77))
            .padding(start = 8.dp, end = 8.dp, top = 10.dp, bottom = 10.dp)
            .fillMaxWidth(),
    ) {
        content()
    }
}

@Composable
fun Card(
    modifier: Modifier = Modifier,
    isClickEnabled: Boolean = true,
    shape: Shape = RoundedCornerShape(10.dp),
    onClick: (() -> Unit)? = null,
    isGradient: Boolean = true,
    borderColor: Color = CardBorderColor,
    borderWidth: Dp = CardBorderWidth,
    backgroundColor: Color = CardBackgroundColor,
    innerPaddingValues: PaddingValues = PaddingValues(
        start = 8.dp,
        end = 8.dp,
        top = 10.dp,
        bottom = 10.dp
    ),
    outerPaddingValues: PaddingValues = PaddingValues(vertical = 4.dp),
    fillMaxSize: Boolean = true,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit,
) {
    val secondColor by wearBiliAnimateColorAsState(
        targetValue = if (isGradient) Color.Transparent else borderColor,
    )
    Box(
        modifier = modifier
            .padding(outerPaddingValues)
            .clickVfx(isEnabled = isClickEnabled && onClick != null) {
                onClick?.invoke()
            }
            .clip(shape)
            .border(
                width = borderWidth,
                shape = shape,
                brush = Brush.linearGradient(
                    listOf(
                        borderColor,
                        secondColor
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
            /*.shadow(
                elevation = 10.dp,
                shape = shape,
                //ambientColor = Color.White,
                //spotColor = Color.White
                *//*ambientColor = borderColor,
                spotColor = borderColor*//*
            )*/
            //.advancedShadow()
            .background(color = backgroundColor)
            .padding(innerPaddingValues)
            .then(if (fillMaxSize) Modifier.fillMaxWidth() else Modifier),
        contentAlignment = contentAlignment
    ) {
        content()
    }
}

@Composable
fun Card(
    modifier: Modifier = Modifier,
    isClickEnabled: Boolean = true,
    shape: Shape = RoundedCornerShape(10.dp),
    onClick: (() -> Unit)? = null,
    innerPaddingValues: PaddingValues = PaddingValues(
        start = 8.dp,
        end = 8.dp,
        top = 10.dp,
        bottom = 10.dp
    ),
    isHighlighted: Boolean,
    fillMaxSize: Boolean = true,
    contentAlignment: Alignment = Alignment.TopStart,
    outerPaddingValues: PaddingValues = PaddingValues(vertical = 4.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    val secondColor by wearBiliAnimateColorAsState(
        targetValue = if (isHighlighted) BilibiliPink else Color.Transparent,
    )
    val cardBorderColor by wearBiliAnimateColorAsState(
        targetValue = if (isHighlighted) BilibiliPink else CardBorderColor,
        animationSpec = tween()
    )
    val cardBackgroundColor by wearBiliAnimateColorAsState(
        targetValue = if (isHighlighted) Color(
            231,
            86,
            136,
            26
        ) else CardBackgroundColor,
        animationSpec = tween()

    )
    val width by wearBiliAnimateDpAsState(
        targetValue = if (isHighlighted) 2.dp else CardBorderWidth,
    )

    Box(
        modifier = modifier
            .padding(outerPaddingValues)
            .clickVfx(isEnabled = isClickEnabled && onClick != null) {
                onClick?.invoke()
            }
            .clip(shape)
            .border(
                width = width,
                shape = shape,
                brush = Brush.linearGradient(
                    listOf(
                        cardBorderColor,
                        secondColor
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
            .background(color = cardBackgroundColor)
            .padding(innerPaddingValues)
            .then(if (fillMaxSize) Modifier.fillMaxWidth() else Modifier),
        contentAlignment = contentAlignment
    ) {
        content()
    }
}