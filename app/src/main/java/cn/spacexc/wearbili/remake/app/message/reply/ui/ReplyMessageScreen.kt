package cn.spacexc.wearbili.remake.app.message.reply.ui

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import cn.spacexc.bilibilisdk.sdk.message.data.reply.ReplyMessage
import cn.spacexc.wearbili.common.domain.time.toDateStr
import cn.spacexc.wearbili.remake.app.article.ui.ArticleActivity
import cn.spacexc.wearbili.remake.app.article.ui.PARAM_CVID
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.VIDEO_TYPE_AID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID_TYPE
import cn.spacexc.wearbili.remake.app.video.info.ui.VideoInformationActivity
import cn.spacexc.wearbili.remake.common.toUIState
import cn.spacexc.wearbili.remake.common.ui.BiliImage
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.LoadingTip
import cn.spacexc.wearbili.remake.common.ui.SmallUserCard
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.toLoadingState
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun Activity.ReplyMessageScreen(viewModel: ReplyMessageViewModel) {
    val lazyItems = viewModel.flow.collectAsLazyPagingItems()
    TitleBackground(
        title = "@我的",
        onBack = ::finish,
        onRetry = lazyItems::refresh,
        uiState = lazyItems.loadState.refresh.toUIState()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = titleBackgroundHorizontalPadding(), vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(lazyItems.itemCount) {
                lazyItems[it]?.let { item ->
                    ReplyMessageCard(item = item)
                }
            }
            item {
                LoadingTip(lazyItems.loadState.append.toLoadingState(), lazyItems::retry)
            }
        }
    }
}

@Composable
fun Activity.ReplyMessageCard(item: ReplyMessage) {
    Card(isClickEnabled = false, outerPaddingValues = PaddingValues()) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            SmallUserCard(
                avatar = item.user.avatar,
                username = item.user.nickname,
                context = this@ReplyMessageCard,
                mid = item.user.mid,
                userInfo = "在${item.item.business}中回复了我"
            )
            if(item.item.source_content.isNotEmpty()) {
                Text(
                    text = item.item.source_content,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontFamily = wearbiliFontFamily,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium
                )
            }
            item.item.apply {
                Card(
                    onClick = {
                        when (type) {
                            "video" -> {
                                startActivity(Intent(this@ReplyMessageCard, VideoInformationActivity::class.java).apply {
                                    putExtra(PARAM_VIDEO_ID, item.item.subject_id.toString())
                                    putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_AID)
                                })
                            }
                            "article" -> {
                                startActivity(Intent(this@ReplyMessageCard, ArticleActivity::class.java).apply {
                                    putExtra(PARAM_CVID, item.item.source_id)
                                })
                            }
                            "reply" -> {
                                startActivity(Intent(this@ReplyMessageCard, VideoInformationActivity::class.java).apply {
                                    putExtra(PARAM_VIDEO_ID, item.item.subject_id.toString())
                                    putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_AID)
                                })
                            }
                        }
                    },
                    modifier = Modifier.alpha(0.75f),
                    innerPaddingValues = PaddingValues(vertical = 8.dp, horizontal = 6.dp)
                ) {
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
                                        ),
                                    contentScale = ContentScale.FillHeight
                                )
                            }
                        }

                        "dynamic", "reply" -> {
                            if (image.isNotEmpty()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(image)
                                        .crossfade(true).build(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        //.offset(x = (index * 8f).dp, y = (index * 4f).dp)
                                        .clip(
                                            RoundedCornerShape(8.dp)
                                        )
                                )
                            } else if (title.isNotEmpty()) {
                                Text(
                                    text = title,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontFamily = wearbiliFontFamily,
                                    maxLines = 4,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(3.dp)
                                )
                            }
                        }
                    }
                }
            }
            Text(
                text = item.reply_time.times(1000).toDateStr(),
                color = Color.White,
                fontSize = 10.sp,
                fontFamily = wearbiliFontFamily,
                modifier = Modifier.alpha(0.5f)
            )
        }
    }
}