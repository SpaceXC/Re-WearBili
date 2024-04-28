package cn.spacexc.wearbili.remake.app.video.info.comment.detail.ui

import android.app.Activity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import cn.spacexc.wearbili.common.ifNullOrEmpty
import cn.spacexc.wearbili.remake.app.video.info.comment.domain.CommentContentData
import cn.spacexc.wearbili.remake.app.video.info.comment.domain.EmoteObject
import cn.spacexc.wearbili.remake.app.video.info.comment.domain.Meta
import cn.spacexc.wearbili.remake.app.video.info.comment.ui.CommentCard
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.toUIState
import cn.spacexc.wearbili.remake.common.ui.LoadingState
import cn.spacexc.wearbili.remake.common.ui.LoadingTip
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.isRound
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.toLoadingState
import kotlinx.coroutines.Dispatchers

@Composable
fun Activity.CommentRepliesDetailScreen(
    viewModel: CommentRepliesDetailViewModel,
    uploaderMid: Long
) {
    val lazyListData = viewModel.pager?.flow?.collectAsLazyPagingItems(Dispatchers.IO)
    TitleBackground(
        title = "评论详情",
        onRetry = {
            viewModel.pagingDataSource?.let {
                viewModel.initPaging(it.rootRpid, it.videoAid)
            }
            lazyListData?.refresh()
        },
        onBack = ::finish,
        uiState = lazyListData?.loadState?.refresh?.toUIState() ?: UIState.Loading
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = if (isRound()) titleBackgroundHorizontalPadding() else 4.dp,
                vertical = 4.dp
            ),
            state = rememberLazyListState()
        ) {
            viewModel.pagingDataSource?.rootComment?.let { comment ->
                item {
                    CommentCard(
                        senderName = comment.member.uname,
                        senderAvatar = comment.member.avatar,
                        senderNameColor = comment.member.vip.nicknameColor.ifNullOrEmpty { "#FFFFFF" },
                        senderPendant = comment.member.pendant.imageEnhance,
                        senderOfficialVerify = comment.member.officialVerify.type,
                        senderMid = comment.member.mid.toLongOrNull() ?: 0L,
                        uploaderMid = uploaderMid,
                        senderIpLocation = comment.replyControl.location,
                        sendTimeStamp = comment.ctime.times(1000),
                        commentContent = comment.content.message,
                        commentLikeCount = comment.like,
                        commentRepliesCount = comment.rcount,
                        commentImagesList = buildList {
                            comment.content.pictures?.forEach {
                                add(CommentContentData.Picture(it.imgSrc))
                            }
                        },
                        commentEmoteMap = buildMap {
                            comment.content.emote?.forEach {
                                val emote = it.value
                                put(
                                    it.key, EmoteObject(
                                        emote.attr,
                                        emote.id,
                                        emote.jumpTitle,
                                        Meta(emote.meta.size),
                                        emote.mtime,
                                        emote.packageId,
                                        emote.state,
                                        emote.text,
                                        emote.type,
                                        emote.url
                                    )
                                )
                            }
                        },
                        commentJumpUrlMap = buildMap {
                            comment.content.jumpUrl.forEach {
                                val jumpUrlObject = it.value
                                put(
                                    it.key, CommentContentData.JumpUrlObject(
                                        jumpUrlObject.title,
                                        jumpUrlObject.prefixIcon,
                                        CommentContentData.JumpUrlExtra(jumpUrlObject.extra.isWordSearch)
                                    )
                                )
                            }
                        },
                        commentAttentionedUsersMap = buildMap {
                            for (member in comment.content.members) {
                                put(member.uname, member.mid.toLongOrNull() ?: 2)
                            }
                        },
                        commentReplyControl = "",
                        commentRpid = comment.rpid,
                        isUpLiked = comment.cardLabel?.find { it.textContent == "UP主觉得很赞" } != null,    //先这样咯
                        context = this@CommentRepliesDetailScreen,
                        isClickable = false,
                        oid = 0,
                        noteCvid = 0,
                        isTopComment = false,
                        commentReplies = emptyList()
                    )
                }
            }
            item {
                Text(
                    text = "所有回复",
                    fontFamily = wearbiliFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.padding(6.dp)
                )
            }
            items(lazyListData?.itemCount ?: 0) { index ->
                lazyListData?.get(index)?.let { comment ->
                    CommentCard(
                        senderName = comment.member.uname,
                        senderAvatar = comment.member.avatar,
                        senderNameColor = comment.member.vip.nicknameColor.ifNullOrEmpty { "#FFFFFF" },
                        senderPendant = comment.member.pendant.imageEnhance,
                        senderOfficialVerify = comment.member.officialVerify.type,
                        senderMid = comment.member.mid.toLongOrNull() ?: 0L,
                        uploaderMid = uploaderMid,
                        senderIpLocation = comment.replyControl.location,
                        sendTimeStamp = comment.ctime.times(1000),
                        commentContent = comment.content.message,
                        commentLikeCount = comment.like,
                        commentRepliesCount = comment.rcount,
                        commentImagesList = buildList {
                            comment.content.pictures?.forEach {
                                add(CommentContentData.Picture(it.imgSrc))
                            }
                        },
                        commentEmoteMap = buildMap {
                            comment.content.emote?.forEach {
                                val emote = it.value
                                put(
                                    it.key, EmoteObject(
                                        emote.attr,
                                        emote.id,
                                        emote.jumpTitle,
                                        Meta(emote.meta.size),
                                        emote.mtime,
                                        emote.packageId,
                                        emote.state,
                                        emote.text,
                                        emote.type,
                                        emote.url
                                    )
                                )
                            }
                        },
                        commentJumpUrlMap = buildMap {
                            comment.content.jumpUrl.forEach {
                                val jumpUrlObject = it.value
                                put(
                                    it.key, CommentContentData.JumpUrlObject(
                                        jumpUrlObject.title,
                                        jumpUrlObject.prefixIcon,
                                        CommentContentData.JumpUrlExtra(jumpUrlObject.extra.isWordSearch)
                                    )
                                )
                            }
                        },
                        commentAttentionedUsersMap = buildMap {
                            for (member in comment.content.members) {
                                put(member.uname, member.mid.toLongOrNull() ?: 2)
                            }
                        },
                        commentReplyControl = "",
                        commentRpid = comment.rpid,
                        isUpLiked = comment.cardLabel?.find { it.textContent == "UP主觉得很赞" } != null,    //先这样咯
                        context = this@CommentRepliesDetailScreen,
                        isClickable = false,
                        oid = 0,
                        noteCvid = 0,
                        isTopComment = false,
                        commentReplies = emptyList()
                    )
                }
            }
            item {
                LoadingTip(
                    lazyListData?.loadState?.append?.toLoadingState() ?: LoadingState.NoMore
                ) {
                    lazyListData?.retry()
                }
            }
        }
    }
}