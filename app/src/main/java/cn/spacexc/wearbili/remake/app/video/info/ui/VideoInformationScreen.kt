package cn.spacexc.wearbili.remake.app.video.info.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.IjkVideoPlayerViewModel
import cn.spacexc.wearbili.remake.app.video.info.comment.ui.CommentScreen
import cn.spacexc.wearbili.remake.app.video.info.comment.ui.CommentViewModel
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoBasicInformationScreen
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationViewModel
import cn.spacexc.wearbili.remake.app.video.info.info.ui.v2.VideoInformationScreenNew
import cn.spacexc.wearbili.remake.app.video.info.related.RelatedVideosScreen
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.isRound

/**
 * Created by XC-Qan on 2023/4/12.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val PARAM_VIDEO_ID_TYPE = "videoIdType"
const val PARAM_VIDEO_ID = "videoId"
const val PARAM_VIDEO_CID = "videoCid"
const val VIDEO_TYPE_AID = "aid"
const val VIDEO_TYPE_BVID = "bvid"

@kotlinx.serialization.Serializable
data class VideoInformationScreen(
    val videoIdType: String,
    val videoId: String,
)

/*@UnstableApi*/
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.VideoInformationScreen(
    ijkVideoPlayerViewModel: IjkVideoPlayerViewModel,
    navController: NavController,
    videoInformationViewModel: VideoInformationViewModel = hiltViewModel(),
    commentViewModel: CommentViewModel = hiltViewModel(),
    videoIdType: String,
    videoId: String,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    LaunchedEffect(key1 = Unit) {
        if (videoInformationViewModel.state.uiState == UIState.Success) return@LaunchedEffect
        videoInformationViewModel.getVideoInfo(videoIdType, videoId, navController) { cid ->
            ijkVideoPlayerViewModel.playVideoFromId(
                videoIdType,
                videoId,
                cid
            )
        }
    }

    val state = rememberPagerState {
        3
    }
    TitleBackground(
        title = when (state.currentPage) {
            0 -> "详情"
            1 -> "评论"
            2 -> "相关推荐"
            else -> ""
        },
        onBack = {
            navController.navigateUp()
        },
        themeImageUrl = videoInformationViewModel.state.videoData?.view?.pic?.replace(
            "http://",
            "https://"
        ) ?: "",
        networkUtils = videoInformationViewModel.ktorNetworkUtils,
        isTitleClipToBounds = false,
        //backgroundColor = color,
    ) {
        HorizontalPager(state = state) {
            when (it) {
                0 -> if (isRound()) {
                    VideoBasicInformationScreen(
                        state = videoInformationViewModel.state,
                        navController = navController,
                        videoInformationViewModel = videoInformationViewModel,
                        videoIdType = videoIdType, videoId = videoId
                    ) {

                    }
                } else {
                    VideoInformationScreenNew(
                        state = videoInformationViewModel.state,
                        navController = navController,
                        videoInformationViewModel = videoInformationViewModel,
                        videoIdType = videoIdType,
                        videoId = videoId,
                        animatedVisibilityScope = animatedVisibilityScope,
                        videoPlayerViewModel = ijkVideoPlayerViewModel,
                        onRetry = {
                            videoInformationViewModel.getVideoInfo(
                                videoIdType,
                                videoId,
                                navController
                            ) { cid ->
                                ijkVideoPlayerViewModel.playVideoFromId(
                                    videoIdType,
                                    videoId,
                                    cid
                                )
                            }
                        }
                    )
                }

                1 -> {
                    CommentScreen(
                        viewModel = commentViewModel,
                        commentsData = commentViewModel.commentListFlow(
                            videoInformationViewModel.state.videoData?.view?.aid?.toString() ?: ""
                        )?.collectAsLazyPagingItems(),
                        oid = videoInformationViewModel.state.videoData?.view?.aid ?: 0,
                        uploaderMid = videoInformationViewModel.state.videoData?.view?.owner?.mid
                            ?: 0,
                        navController = navController,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }

                2 -> RelatedVideosScreen(
                    viewModel = videoInformationViewModel,
                    navController = navController
                )

            }
        }
    }
}