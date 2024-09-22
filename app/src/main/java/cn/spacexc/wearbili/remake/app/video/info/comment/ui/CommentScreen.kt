package cn.spacexc.wearbili.remake.app.video.info.comment.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import cn.spacexc.wearbili.common.ifNullOrEmpty
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.video.info.comment.domain.CommentContentData
import cn.spacexc.wearbili.remake.common.toUIState
import cn.spacexc.wearbili.remake.common.ui.LoadableBox
import cn.spacexc.wearbili.remake.common.ui.LoadingTip
import cn.spacexc.wearbili.remake.common.ui.toLoadingState

/**
 * Created by XC-Qan on 2023/4/28.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@OptIn(ExperimentalMaterialApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CommentScreen(
    viewModel: CommentViewModel,
    commentsData: LazyPagingItems<CommentContentData>?,
    oid: Long,
    uploaderMid: Long,
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    if (commentsData != null) {
        LoadableBox(
            uiState = commentsData.loadState.refresh.toUIState(),
            modifier = Modifier.fillMaxSize(),
            onRetry = commentsData::retry
        ) {
            if (commentsData.itemCount == 0) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_empty_box),
                            contentDescription = "Empty...",
                            modifier = Modifier.fillMaxWidth()
                            //.fillMaxWidth(0.3f)
                            //.aspectRatio(1f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "什么都没有啊")
                    }
                }
            } else {
                val pullToRefreshState = rememberPullRefreshState(
                    refreshing = commentsData.loadState.refresh is LoadState.Loading,
                    onRefresh = commentsData::refresh,
                    refreshThreshold = 60.dp
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pullRefresh(state = pullToRefreshState)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = (0.1).dp,
                                color = Color(54, 54, 54, 255),
                                shape = RoundedCornerShape(
                                    topStart = 8.dp, topEnd = 8.dp
                                )
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStart = 8.dp, topEnd = 8.dp
                                )
                            )
                            .background(Color(38, 38, 38, 77)),
                        contentPadding = PaddingValues(
                            vertical = 4.dp
                        ),
                        state = viewModel.getScrollState(oid.toString()) ?: rememberLazyListState()
                    ) {
                        items(commentsData.itemCount) {
                            commentsData[it]?.let { comment ->
                                Spacer(modifier = Modifier.height(12.dp))
                                CommentCard(
                                    senderName = comment.member?.uname ?: "",
                                    senderAvatar = comment.member?.avatar ?: "",
                                    senderNameColor = comment.member?.vip?.nickname_color.ifNullOrEmpty { "#FFFFFF" },
                                    senderPendant = comment.member?.pendant?.image_enhance
                                        ?: "",
                                    senderOfficialVerify = comment.member?.official_verify?.type
                                        ?: -1,
                                    senderMid = comment.member?.mid ?: 0,
                                    senderIpLocation = comment.reply_control?.location
                                        ?: "",
                                    sendTimeStamp = comment.ctime.times(1000),
                                    commentContent = comment.content?.message ?: "",
                                    commentLikeCount = comment.like,
                                    commentRepliesCount = comment.rcount,
                                    commentReplies = comment.replies ?: emptyList(),
                                    commentEmoteMap = comment.content?.emote
                                        ?: emptyMap(),
                                    commentJumpUrlMap = comment.content?.jump_url
                                        ?: emptyMap(),
                                    commentAttentionedUsersMap = comment.content?.at_name_to_mid
                                        ?: emptyMap(),
                                    commentReplyControl = comment.reply_control?.sub_reply_entry_text
                                        ?: "",
                                    commentImagesList = comment.content?.pictures,
                                    commentRpid = comment.rpid,
                                    uploaderMid = uploaderMid,
                                    isTopComment = comment.is_top,
                                    isUpLiked = comment.up_action.like,
                                    navController = navController,
                                    isClickable = true,
                                    oid = oid,
                                    noteCvid = comment.note_cvid,
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Divider(
                                    modifier = Modifier.padding(horizontal = 2.dp),
                                    color = Color(61, 61, 61, 255)
                                )
                            }
                        }
                        item {
                            LoadingTip(
                                loadingState = commentsData.loadState.append.toLoadingState(),
                                onRetry = commentsData::retry
                            )
                        }
                    }
                    PullRefreshIndicator(
                        refreshing = commentsData.loadState.refresh is LoadState.Loading,
                        state = pullToRefreshState,
                        modifier = Modifier.align(
                            Alignment.TopCenter
                        )
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_empty_box),
                    contentDescription = "Empty...",
                    modifier = Modifier.fillMaxWidth()
                    //.fillMaxWidth(0.3f)
                    //.aspectRatio(1f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "选择一集才能康评论啊啊啊啊")
            }
        }
    }
}