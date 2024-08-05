package cn.spacexc.wearbili.remake.app.message.like.ui

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import cn.spacexc.bilibilisdk.sdk.message.data.like.LikeMessage
import cn.spacexc.wearbili.common.domain.time.toDateStr
import cn.spacexc.wearbili.remake.app.article.ui.ArticleActivity
import cn.spacexc.wearbili.remake.app.article.ui.PARAM_CVID
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.VIDEO_TYPE_AID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID_TYPE
import cn.spacexc.wearbili.remake.app.video.info.ui.VideoInformationActivity
import cn.spacexc.wearbili.remake.common.ui.BiliImage
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding

@Composable
fun Activity.LikeMessagesScreen(viewModel: LikeMessagesViewModel) {
    val lazyItems = viewModel.flow.collectAsLazyPagingItems()
    TitleBackground(title = "收到的赞", onRetry = lazyItems::refresh, onBack = ::finish) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = titleBackgroundHorizontalPadding(),
                vertical = 6.dp
            ),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(lazyItems.itemCount) {
                lazyItems[it]?.let { item ->
                    LikeMessageCard(item = item)
                }
            }
        }
    }
}

@Composable
fun Activity.LikeMessageCard(item: LikeMessage) {
    val users = if (item.users.size > 1) item.users.subList(0, 2) else item.users.subList(0, 1)
    Card(isClickEnabled = false, outerPaddingValues = PaddingValues()) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                users.asReversed().forEachIndexed { index, item ->
                    BiliImage(
                        url = item.avatar,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .offset(x = (index * 8f).dp, y = (index * 4f).dp)
                            .clip(
                                CircleShape
                            )
                    )
                }
            }
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Black)) {
                        users.forEachIndexed { index, user ->
                            append(user.nickname)
                            if (index < users.size - 1) {
                                append("、")
                            }
                        }
                    }
                    if (item.counts > 1) {
                        append("等总计${item.counts}人")
                    }
                    append("赞了我的${item.item.business}")
                },
                fontFamily = wearbiliFontFamily,
                fontSize = 12.sp,
                color = Color.White,
                modifier = Modifier.alpha(0.95f)
            )
            item.item.apply {
                Card(onClick = {
                    when (type) {
                        "video" -> {
                            startActivity(Intent(this@LikeMessageCard, VideoInformationActivity::class.java).apply {
                                putExtra(PARAM_VIDEO_ID, item.item.item_id.toString())
                                putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_AID)
                            })
                        }
                        "article" -> {
                            startActivity(Intent(this@LikeMessageCard, ArticleActivity::class.java).apply {
                                putExtra(PARAM_CVID, item.item.item_id)
                            })
                        }
                        "reply" -> {
                            startActivity(Intent(this@LikeMessageCard, VideoInformationActivity::class.java).apply {
                                putExtra(PARAM_VIDEO_ID, item.item.item_id.toString())
                                putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_AID)
                            })
                        }
                        /*"dynamic" -> {
                            startActivity(
                                Intent(
                                    this@LikeMessageActivity,
                                    NewDynamicDetailActivity::class.java
                                ).apply {

                                })
                        }*/
                    }

                }, modifier = Modifier.alpha(0.75f)) {
                    when (type) {
                        "video", "article" -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = title,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontFamily = wearbiliFontFamily,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(
                                        1f
                                    )
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                BiliImage(
                                    url = image,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .aspectRatio(1f)

                                        .clip(
                                            RoundedCornerShape(6.dp)
                                        ), contentScale = ContentScale.FillHeight
                                )
                            }
                        }

                        "dynamic", "reply", "danmu" -> {
                            Text(
                                text = title,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontFamily = wearbiliFontFamily,
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        "album" -> {
                            Text(
                                text = desc,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontFamily = wearbiliFontFamily,
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            Text(
                text = item.like_time.times(1000).toDateStr(),
                color = Color.White,
                fontSize = 10.sp,
                fontFamily = wearbiliFontFamily,
                modifier = Modifier.alpha(0.5f)
            )
        }
    }
}