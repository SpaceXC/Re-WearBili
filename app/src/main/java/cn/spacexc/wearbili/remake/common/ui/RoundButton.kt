package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily

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
    text: String,
    clickable: Boolean = true,
    onClick: () -> Unit = { },
    onLongClick: () -> Unit = { },
    icon: @Composable BoxScope.() -> Unit,
) {
    Column(
        modifier = modifier.clickVfx(
            onClick = onClick,
            onLongClick = onLongClick
        )/*.height(IntrinsicSize.Max)*//*.wrapContentHeight()*/,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /*Box {
            icon()
        }*/
        Card(
            innerPaddingValues = PaddingValues(3.dp),
            modifier = buttonModifier/*.fillMaxSize()*/,
            shape = CircleShape,
            outerPaddingValues = PaddingValues(2.dp),
            isClickEnabled = clickable
        ) {
            icon()
        }
        //Box(modifier = Modifier.height(50.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = text,
            fontFamily = wearbiliFontFamily,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }

}