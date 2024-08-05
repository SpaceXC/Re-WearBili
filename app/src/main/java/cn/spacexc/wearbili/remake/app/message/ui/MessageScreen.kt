package cn.spacexc.wearbili.remake.app.message.ui

import android.app.Activity
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import appendBiliIcon
import cn.spacexc.bilibilisdk.sdk.message.data.direct.list.Session
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.message.at.ui.AtMessageActivity
import cn.spacexc.wearbili.remake.app.message.direct.history.ui.DirectMessageActivity
import cn.spacexc.wearbili.remake.app.message.direct.history.ui.PARAM_TALKER_MID
import cn.spacexc.wearbili.remake.app.message.direct.history.ui.PARAM_TALKER_NAME
import cn.spacexc.wearbili.remake.app.message.direct.sessions.DirectMessagesListViewModel
import cn.spacexc.wearbili.remake.app.message.direct.sessions.getMessageContentByString
import cn.spacexc.wearbili.remake.app.message.like.ui.LikeMessagesActivity
import cn.spacexc.wearbili.remake.app.message.reply.ui.ReplyMessageActivity
import cn.spacexc.wearbili.remake.app.message.system.SystemMessageActivity
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.LoadingTip
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.UserAvatar
import cn.spacexc.wearbili.remake.common.ui.shimmerPlaceHolder
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.toLoadingState
import cn.spacexc.wearbili.remake.common.ui.toOfficialVerify

@Composable
fun Activity.MessageScreen(
    directMessagesSessionsViewModel: DirectMessagesListViewModel
) {
    val lazyListData = directMessagesSessionsViewModel.flow.collectAsLazyPagingItems()
    TitleBackground(title = "消息中心", onRetry = { /*TODO*/ }, onBack = ::finish) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            item {
                MessageTypeCard(
                    ic0n = R.drawable.icon_message_system,
                    name = "系统通知",
                ) {
                    startActivity(Intent(this@MessageScreen, SystemMessageActivity::class.java))
                }
            }
            item {
                MessageTypeCard(
                    ic0n = R.drawable.icon_message_at_me,
                    name = "@我的",
                ) {
                    startActivity(Intent(this@MessageScreen, AtMessageActivity::class.java))
                }
            }
            item {
                MessageTypeCard(
                    ic0n = R.drawable.icon_message_reply_me,
                    name = "回复我的",
                ) {
                    startActivity(Intent(this@MessageScreen, ReplyMessageActivity::class.java))
                }
            }
            item {
                MessageTypeCard(
                    ic0n = R.drawable.icon_message_liked_me,
                    name = "收到的赞",
                ) {
                    startActivity(Intent(this@MessageScreen, LikeMessagesActivity::class.java))
                }
            }
            items(lazyListData.itemCount) { index ->
                lazyListData[index]?.let {
                    if (it.userCard != null) {
                        DirectMessageSessionCard(
                            session = it
                        )
                    }
                }
            }
            item {
                LoadingTip(lazyListData.loadState.append.toLoadingState(), lazyListData::retry)
            }
        }
    }
}

@Composable
fun MessageTypeCard(
    @DrawableRes ic0n: Int,
    name: String,
    onClick: () -> Unit
) {
    Card(onClick = onClick, isClickEnabled = true, shape = RoundedCornerShape(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = ic0n),
                contentDescription = null,
                modifier = Modifier.scale(0.8f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                fontFamily = wearbiliFontFamily
            )
        }
    }
}

@Composable
fun Activity.DirectMessageSessionCard(
    session: Session
) {
    val localDensity = LocalDensity.current
    val userInfo = session.userCard
    Card(shape = RoundedCornerShape(16.dp), innerPaddingValues = PaddingValues(10.dp), onClick = {
        startActivity(Intent(this, DirectMessageActivity::class.java).apply {
            putExtra(PARAM_TALKER_NAME, userInfo?.name)
            putExtra(PARAM_TALKER_MID, userInfo?.mid?.toLongOrNull())
        })
    }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            var textHeight by remember {
                mutableStateOf(0.dp)
            }
            UserAvatar(
                avatar = userInfo?.face ?: "",
                pendant = userInfo?.pendant?.image,
                size = DpSize.Unspecified,
                modifier = Modifier
                    .size(textHeight + 6.dp),
                officialVerify = userInfo?.official?.type.toOfficialVerify()
            )
            Spacer(modifier = Modifier.width(2.dp))
            Column(modifier = Modifier.onSizeChanged {
                textHeight = with(localDensity) { it.height.toDp() }
            }) {
                Text(
                    text = userInfo?.name ?: "bilibili",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.shimmerPlaceHolder(userInfo == null),
                    fontFamily = wearbiliFontFamily
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = buildAnnotatedString {
                        val content = getMessageContentByString(
                            session.last_msg.msg_type,
                            session.last_msg.content
                        )
                        if (content != null) {
                            append(content)
                        } else {
                            withStyle(SpanStyle(color = BilibiliPink)) {
                                withStyle(SpanStyle(baselineShift = BaselineShift(-0.15f))) {
                                    appendBiliIcon("EB3C")
                                }
                                append("无法预览的消息类型")
                            }
                        }
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    modifier = Modifier.alpha(0.7f),
                    fontFamily = wearbiliFontFamily
                )
            }
        }
    }
}