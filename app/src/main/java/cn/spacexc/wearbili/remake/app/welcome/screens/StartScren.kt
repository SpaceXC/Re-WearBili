package cn.spacexc.wearbili.remake.app.welcome.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.app.welcome.components.CirclePosition
import cn.spacexc.wearbili.remake.app.welcome.components.PinkCircle
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.time.DefaultTimeSource
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily

@Composable
fun StartScreen(
    onToNext: () -> Unit
) {
    val timeSource = DefaultTimeSource("HH:mm")
    val timeText = timeSource.currentTime
    Box(modifier = Modifier.fillMaxSize()) {
        PinkCircle(CirclePosition.TOP_START)
        PinkCircle(CirclePosition.BOTTOM_END)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            Row(modifier = Modifier) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = timeText,
                    fontFamily = wearbiliFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.spx
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically)
            ) {
                Text(
                    text = "欢迎使用",
                    fontSize = 17.spx,
                    fontFamily = wearbiliFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color(
                        255,
                        255,
                        255,
                        128
                    )
                )
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "WearBili",
                        fontSize = 24.spx,
                        fontFamily = wearbiliFontFamily,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    ReBadge()
                }
                Card(
                    outerPaddingValues = PaddingValues(horizontal = 0.dp, vertical = 15.dp),
                    innerPaddingValues = PaddingValues(15.dp),
                    shape = RoundedCornerShape(14.dp),
                    borderWidth = 0.55.dp,
                    onClick = onToNext
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "开始使用", fontFamily = wearbiliFontFamily, fontSize = 13.spx)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReBadge() {
    Text(
        text = "Re",
        modifier = Modifier
            .offset(y = 3.dp)
            .background(
                color = BilibiliPink, shape = RoundedCornerShape(
                    topStart = 7.dp, topEnd = 7.dp, bottomEnd = 7.dp, bottomStart = 2.dp
                )
            )
            .padding(
                start = 6.dp,
                end = 6.dp,
                top = 2.dp,
                bottom = 2.dp
            ),
        fontFamily = wearbiliFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 8.spx
    )
}