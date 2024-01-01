package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily

/**
 * Created by XC-Qan on 2024/1/1.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun Chip(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    text: String,
    textColor: Color = Color.White,
    fontSize: TextUnit = 11.spx,
    backgroundColor: Color
) {
    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(30))
            .padding(start = 8.dp, end = 10.dp, top = 4.dp, bottom = 4.dp)
    ) {
        IconText(
            text = text,
            icon = icon,
            color = textColor,
            fontSize = fontSize,
            fontFamily = wearbiliFontFamily,
            spacingWidth = 0.sp
        )
    }
}