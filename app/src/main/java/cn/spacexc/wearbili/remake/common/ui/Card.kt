package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Created by XC-Qan on 2023/3/4.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun Card(
    modifier: Modifier = Modifier,
    isClickEnabled: Boolean = true,
    shape: Shape = RoundedCornerShape(10.dp),
    onClick: (() -> Unit)? = null,
    borderColor: Color = Color(54, 54, 54, 255),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .padding(/*horizontal = 8.dp, */vertical = 4.dp)
            .clickVfx(isEnabled = isClickEnabled && onClick != null) {
                onClick?.invoke()
            }
            .clip(shape)
            .border(
                width = 0.3f.dp,
                shape = shape,
                brush = Brush.linearGradient(
                    listOf(
                        borderColor,
                        Color.Transparent
                    )
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
    borderColor: Color = Color(54, 54, 54, 255),
    innerPaddingValues: PaddingValues = PaddingValues(
        start = 8.dp,
        end = 8.dp,
        top = 10.dp,
        bottom = 10.dp
    ),
    outerPaddingValues: PaddingValues = PaddingValues(vertical = 4.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .padding(outerPaddingValues)
            .clickVfx(isEnabled = isClickEnabled && onClick != null) {
                onClick?.invoke()
            }
            .clip(shape)
            .border(
                width = 0.4f.dp,
                shape = shape,
                brush = Brush.linearGradient(
                    listOf(
                        borderColor,
                        Color.Transparent
                    )
                )
            )
            .background(color = Color(38, 38, 38, 77))
            .padding(innerPaddingValues)
            .fillMaxWidth(),
    ) {
        content()
    }
}