package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme

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
    iconModifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    onLongClick: () -> Unit = {},
    tint: Color = Color.White,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier.clickVfx(onClick = onClick, onLongClick = onLongClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = iconModifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .fillMaxHeight()
                .background(Color(36, 36, 36, 100), CircleShape)
                .border(
                    width = 0.1f.dp,
                    color = Color(112, 112, 112, 70),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                tint = tint,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .padding(12.dp)//.aspectRatio(1f, matchHeightConstraintsFirst = true)
            )
        }

        Text(text = text, style = AppTheme.typography.body2)
    }
}