package cn.spacexc.wearbili.remake.app.message.direct.history.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import appendBiliIcon
import cn.spacexc.bilibilisdk.sdk.message.data.direct.history.Message
import cn.spacexc.wearbili.common.domain.time.toDateStr
import cn.spacexc.wearbili.remake.app.message.direct.sessions.domain.content.eighteen.ContentCard18
import cn.spacexc.wearbili.remake.app.message.direct.sessions.domain.content.image.ImageContent
import cn.spacexc.wearbili.remake.app.message.direct.sessions.domain.content.plain.PlainContent
import cn.spacexc.wearbili.remake.app.message.direct.sessions.getMessageContentObjectByString
import cn.spacexc.wearbili.remake.common.ui.BiliImage
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding

@Composable
fun Activity.DirectMessageScreen(
    talkerName: String,
    talkerMid: Long,
    viewModel: DirectMessageViewModel
) {
    TitleBackground(
        title = talkerName,
        onRetry = {
            viewModel.getMessages(talkerMid)
        },
        onBack = ::finish,
        uiState = viewModel.uiState
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = titleBackgroundHorizontalPadding(),
                vertical = 8.dp
            ),
            reverseLayout = true,
            //verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            viewModel.messages.forEachIndexed { index, message ->
                item(key = message.msg_seqno) {
                    val isTalker = talkerMid == message.sender_uid
                    val lastMessage = viewModel.messages.getOrNull(index + 1)
                    val nextMessage = viewModel.messages.getOrNull(index - 1)
                    val lastMessageSendTime = lastMessage?.timestamp
                    val shouldDisplayTime =
                        lastMessageSendTime == null || (message.timestamp - lastMessageSendTime > 2 * 60 * 1000)
                    val shouldJoinNext = nextMessage != null && nextMessage.sender_uid == message.sender_uid && (nextMessage.timestamp - message.timestamp < 2 * 60 * 1000)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (shouldDisplayTime) {
                            Card(
                                shape = CircleShape,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .alpha(0.7f),
                                fillMaxSize = false,
                                innerPaddingValues = PaddingValues(
                                    horizontal = 8.dp,
                                    vertical = 2.dp
                                ),
                                outerPaddingValues = PaddingValues()
                            ) {
                                Text(
                                    text = message.timestamp.times(1000)
                                        .toDateStr("yyyy年MM月dd日 HH:mm"),
                                    fontFamily = wearbiliFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        MessageCard(
                            isTalker = isTalker,
                            message = message,
                            shouldJoinNext = shouldJoinNext
                        )

                        if(shouldJoinNext) {
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                        else {
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ColumnScope.MessageCard(isTalker: Boolean, message: Message, shouldJoinNext: Boolean) {
    val color = if (isTalker) Color(20, 20, 20, 255) else BilibiliPink
    val shape = if (isTalker)
        RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = if(shouldJoinNext) 10.dp else 2.dp, bottomEnd = 10.dp)
    else RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 10.dp, bottomEnd = if(shouldJoinNext) 10.dp else 2.dp)
    val alignment = if (isTalker) Alignment.Start else Alignment.End
    when (val content = getMessageContentObjectByString(message.msg_type, message.content)) {
        is PlainContent -> {
            Box(
                modifier = Modifier
                    .background(color, shape)
                    .align(alignment)
            ) {
                Text(
                    text = content.content,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = wearbiliFontFamily,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        is ImageContent -> {
            val aspectRatio = content.width.toFloat() / content.height.toFloat()
            BiliImage(
                url = content.url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if(isTalker) {
                            Modifier.padding(end = 12.dp)
                        }
                        else {
                            Modifier.padding(start = 12.dp)
                        }
                    )
                    .aspectRatio(aspectRatio)
                    .clip(shape)
                    .align(alignment)
            )
        }

        is ContentCard18.ContentCard18Content -> {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .alpha(0.7f),
                fillMaxSize = false,
                innerPaddingValues = PaddingValues(
                    horizontal = 8.dp,
                    vertical = 2.dp
                ),
                outerPaddingValues = PaddingValues()
            ) {
                Text(
                    text = content.text,
                    fontFamily = wearbiliFontFamily,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }

        else -> Box(
            modifier = Modifier
                .background(color, shape)
                .align(alignment)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = if (isTalker) BilibiliPink else Color(
                                20,
                                20,
                                20,
                                255
                            )
                        )
                    ) {
                        withStyle(SpanStyle(baselineShift = BaselineShift(-0.15f))) {
                            appendBiliIcon("EB3C")
                        }
                        append("不支持的消息类型")
                    }
                },
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = wearbiliFontFamily,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}