package cn.spacexc.wearbili.remake.app.main.profile.ui

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.domain.color.parseColor
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.LoadableBox
import cn.spacexc.wearbili.remake.common.ui.OfficialVerify
import cn.spacexc.wearbili.remake.common.ui.UserAvatar
import cn.spacexc.wearbili.remake.common.ui.WearBiliAnimatedVisibility
import cn.spacexc.wearbili.remake.common.ui.spx

/**
 * Created by XC-Qan on 2023/4/9.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */
//WIP 个人页
data class ProfileScreenState(
    val username: String,
    val avatar: String,
    val pendant: String?,
    val level: Int,
    val fans: Long,
    val coins: Double,
    val followed: Long,
    val uiState: UIState,
    val scrollState: ScrollState,
    val nicknameColor: String
)

@Composable
fun ProfileScreen(
    state: ProfileScreenState,
    isAvatarBackgroundVisible: Boolean = false
) {
    val localDensity = LocalDensity.current
    var avatarHeight by remember {
        mutableStateOf(0.dp)
    }
    var screenWidth by remember {
        mutableStateOf(0.dp)
    }
    var avatarHeightPx by remember {
        mutableStateOf(0)
    }
    val avatarBackgroundVisibility =
        state.scrollState.value < avatarHeightPx.times(0.6f) && isAvatarBackgroundVisible
    LoadableBox(
        uiState = state.uiState,
        modifier = Modifier.onSizeChanged {
            screenWidth = with(localDensity) { it.width.toDp() }
        }) {
        Box(modifier = Modifier.fillMaxSize()) {
            WearBiliAnimatedVisibility(
                visible = avatarBackgroundVisibility,
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                Box(
                    modifier = Modifier
                        .size(screenWidth)
                        .scale(2f)
                        .offset(y = (avatarHeight + screenWidth).times(-.45f))
                        .background(color = BilibiliPink, shape = CircleShape)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state.scrollState)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UserAvatar(
                        avatar = state.avatar,
                        pendant = state.pendant,
                        size = DpSize.Unspecified,
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .onSizeChanged {
                                avatarHeight = with(localDensity) {
                                    it.height.toDp()
                                }
                                avatarHeightPx = it.height
                            },
                        officialVerify = OfficialVerify.PERSONAL
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val levelCardResourceId = when (state.level) {
                            0 -> cn.spacexc.wearbili.remake.R.drawable.icon_lv0_card
                            1 -> cn.spacexc.wearbili.remake.R.drawable.icon_lv1_card
                            2 -> cn.spacexc.wearbili.remake.R.drawable.icon_lv2_card
                            3 -> cn.spacexc.wearbili.remake.R.drawable.icon_lv3_card
                            4 -> cn.spacexc.wearbili.remake.R.drawable.icon_lv4_card
                            5 -> cn.spacexc.wearbili.remake.R.drawable.icon_lv5_card
                            6 -> cn.spacexc.wearbili.remake.R.drawable.icon_lv6_card
                            7 -> cn.spacexc.wearbili.remake.R.drawable.icon_lv6_plus_card
                            else -> 0
                        }
                        val inlineTextContent = mapOf(
                            "levelCard" to InlineTextContent(
                                Placeholder(
                                    width = MaterialTheme.typography.h2.fontSize * 2,
                                    height = MaterialTheme.typography.h2.fontSize,
                                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                                )
                            ) {
                                if (levelCardResourceId != 0) {
                                    Image(
                                        painter = painterResource(id = levelCardResourceId),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .scale(0.95f),
                                        contentScale = ContentScale.Inside
                                    )
                                }
                            }
                        )
                        Text(
                            text = buildAnnotatedString {
                                append(state.username)
                                append(" ")
                                if (levelCardResourceId != 0) {
                                    appendInlineContent("levelCard")
                                }
                            },
                            style = MaterialTheme.typography.h2,
                            color = parseColor(state.nicknameColor.ifEmpty { "#FFFFFF" }),
                            inlineContent = inlineTextContent
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.coins.toString(),
                            fontSize = 12.spx,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "硬币",
                            fontSize = 11.spx,
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.alpha(0.8f)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(parseColor("#902F3134"))
                            .fillMaxHeight(0.5f)
                            .width(0.5.dp)
                        //.height(10.dp)
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.fans.toString(),
                            fontSize = 12.spx,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "粉丝",
                            fontSize = 11.spx,
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.alpha(0.8f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(200.dp))

            }
        }
    }
}