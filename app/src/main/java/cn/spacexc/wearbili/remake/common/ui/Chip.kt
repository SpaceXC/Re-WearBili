package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.COMMAND_RATE
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.COMMAND_SUBSCRIBE
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.COMMAND_VIDEO
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.COMMAND_VOTE
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily

@Composable
fun DanmakuChip(
    commandDanmakuType: String,
    uploaderAvatarUrl: String = "",
    onClick: () -> Unit
) {
    val localDensity = LocalDensity.current
    var textHeight by remember {
        mutableStateOf(0.dp)
    }
    val text = when (commandDanmakuType) {
        COMMAND_RATE -> "评分"
        COMMAND_VIDEO -> "推荐视频"
        COMMAND_VOTE -> "投票"
        COMMAND_SUBSCRIBE -> "关注我"
        else -> "UNKNOWN"
    }

    val icon = when (commandDanmakuType) {
        COMMAND_RATE -> R.drawable.icon_danmaku_rate
        COMMAND_VIDEO -> R.drawable.icon_view_count
        COMMAND_VOTE -> R.drawable.icon_danmaku_vote
        COMMAND_SUBSCRIBE -> R.drawable.icon_uploader
        else -> 0
    }

    Row(
        modifier = Modifier
            .background(
                Color(18, 18, 18, 255),
                CircleShape
            )
            .padding(start = 9.dp, end = 12.5.dp, top = 5.dp, bottom = 5.dp)
            .clickVfx { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(textHeight * 1.6f)
                .background(Color(73, 73, 73, 255), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(textHeight * 1.3f)
                    .background(Color(18, 18, 18, 255), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (uploaderAvatarUrl.isNotEmpty()) {
                    BiliImage(
                        url = uploaderAvatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(textHeight * 0.9f)
                            .offset(y = (-0.5).dp, x = (-0.25).dp)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(textHeight * 0.9f)
                            .offset(y = (-0.5).dp, x = (-0.25).dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = Color.White,
            fontFamily = wearbiliFontFamily,
            fontSize = 10.spx,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.onSizeChanged {
                textHeight = with(localDensity) { it.height.toDp() }
            }
        )
    }
}