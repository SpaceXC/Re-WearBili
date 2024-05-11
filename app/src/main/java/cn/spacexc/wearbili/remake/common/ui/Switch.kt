package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Switch(
    isOn: Boolean,
    size: Dp = 20.dp,
    onValueChanged: (Boolean) -> Unit
) {
    val color by animateColorAsState(
        targetValue = if (isOn) BilibiliPink else Color(
            23,
            23,
            23,
            255
        ), label = ""
    )
    val spacerWidth by animateDpAsState(targetValue = if (isOn) size else 0.dp, label = "")
    Row(
        modifier = Modifier
            .width(size * 2)
            .height(size)
            .background(color, CircleShape)
            .clickAlpha { onValueChanged(!isOn) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(4.dp))
        Spacer(modifier = Modifier.width(spacerWidth))
        Box(
            modifier = Modifier
                .background(Color.White, CircleShape)
                .size(size - 8.dp)
                .aspectRatio(1f)
                .fillMaxHeight()
        )
        Spacer(modifier = Modifier.width(4.dp))
    }
}

@Preview
@Composable
private fun SwitchPreview() {
    Switch(isOn = false) {

    }
    Switch(isOn = true) {

    }
}