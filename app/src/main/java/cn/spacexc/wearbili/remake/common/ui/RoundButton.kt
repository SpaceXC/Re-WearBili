package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
    text: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickVfx { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            tint = Color.White,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color(41, 41, 41), CircleShape)
                .padding(16.dp)
        )
        Text(text = text, style = AppTheme.typography.body2)
    }
}