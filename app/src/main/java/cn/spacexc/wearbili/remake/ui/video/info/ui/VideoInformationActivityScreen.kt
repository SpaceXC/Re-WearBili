package cn.spacexc.wearbili.remake.ui.video.info.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.paging.PagingData
import androidx.palette.graphics.Palette
import androidx.wear.compose.navigation.composable
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.ui.video.info.comment.domain.CommentContentData
import cn.spacexc.wearbili.remake.ui.video.info.comment.ui.CommentScreen
import cn.spacexc.wearbili.remake.ui.video.info.comment.ui.CommentViewModel
import cn.spacexc.wearbili.remake.ui.video.info.info.ui.VideoInformationScreen
import cn.spacexc.wearbili.remake.ui.video.info.info.ui.VideoInformationViewModel
import cn.spacexc.wearbili.remake.ui.video.info.related.RelatedVideosScreen
import kotlinx.coroutines.flow.Flow

/**
 * Created by XC-Qan on 2023/4/12.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val VideoInformationScreen = "video_information_screen"

@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.videoInformation(
    onBack: () -> Unit,
    onJumpToSearch: (String) -> Unit,
    onJumpToVideo: (String, String) -> Unit,
    onJumpToBangumiDetail: (String, Long) -> Unit,
    onJumpToImage: (List<String>, Int) -> Unit,
    onJumpToSeason: (seasonName: String, uploaderName: String, seasonId: Long, uploaderMid: Long, ambientColor: Int) -> Unit
) {
    composable("$VideoInformationScreen/{videoIdType}/{videoId}") {
        val videoInformationViewModel: VideoInformationViewModel = hiltViewModel()
        val commentViewModel: CommentViewModel = hiltViewModel()
        val videoIdType = it.arguments?.getString("videoIdType") ?: ""
        val videoId = it.arguments?.getString("videoId") ?: ""
        val aid = (if (videoId.isNotEmpty()) {
            if (videoIdType == VIDEO_TYPE_AID) videoId.replace(
                "av",
                ""
            ) else cn.spacexc.wearbili.common.domain.video.VideoUtils.bv2av(
                videoId
            ).replace("av", "")
        } else "0")
        val commentDataFlow = remember { commentViewModel.commentListFlow(aid) }
        val pagerState = rememberPagerState { 3 }
        LaunchedEffect(key1 = Unit, block = {
            videoInformationViewModel.getVideoInfo(
                videoIdType = videoIdType,
                videoId = videoId,
                onJumpToBangumiDetail
            )
        })

        VideoInformationScreen(
            state = pagerState,
            videoInformationViewModel = videoInformationViewModel,
            commentViewModel = commentViewModel,
            commentDataFlow = commentDataFlow,
            uploaderMid = videoInformationViewModel.state.videoData?.view?.owner?.mid ?: 0,
            oid = videoInformationViewModel.state.videoData?.view?.aid ?: 0,
            onBack = onBack,
            onJumpToVideo = onJumpToVideo,
            onJumpToBangumiDetail = onJumpToBangumiDetail,
            onJumpToSeason = onJumpToSeason,
            onJumpToImage = onJumpToImage,
            onJumpToSearch = onJumpToSearch
        )
    }
}

fun NavController.navigateToVideoInformationScreen(
    videoIdType: String, videoId: String
) = navigate("$VideoInformationScreen/$videoIdType/$videoId")

@OptIn(ExperimentalFoundationApi::class)
/*@UnstableApi*/
@Composable
fun VideoInformationScreen(
    state: PagerState,
    videoInformationViewModel: VideoInformationViewModel,
    commentViewModel: CommentViewModel,
    commentDataFlow: Flow<PagingData<CommentContentData>>?,
    uploaderMid: Long,
    oid: Long,  //aid
    onBack: () -> Unit,
    onJumpToVideo: (String, String) -> Unit,
    onJumpToBangumiDetail: (String, Long) -> Unit,
    onJumpToSeason: (seasonName: String, uploaderName: String, seasonId: Long, uploaderMid: Long, ambientColor: Int) -> Unit,
    onJumpToImage: (List<String>, Int) -> Unit,
    onJumpToSearch: (String) -> Unit
) {
    var currentColor by remember {
        mutableStateOf(BilibiliPink)
    }
    val color by animateColorAsState(
        targetValue = currentColor,
        animationSpec = tween(durationMillis = 1000), label = ""
    )
    val ambientAlpha by animateFloatAsState(
        targetValue = if (currentColor == BilibiliPink) 0.6f else 1f,
        animationSpec = tween(durationMillis = 1000), label = ""
    )
    LaunchedEffect(key1 = videoInformationViewModel.imageBitmap, block = {
        videoInformationViewModel.imageBitmap?.let { bitmap ->
            val palette = Palette.from(bitmap).generate()
            //if(newColor < Color(0x10000000).value.toInt())
            currentColor = Color(palette.getLightMutedColor(BilibiliPink.value.toInt()))
        }
    })
    LaunchedEffect(key1 = videoInformationViewModel.state, block = {
        videoInformationViewModel.state.videoData?.let { data ->
            videoInformationViewModel.downloadWebMask(
                videoIdType = VIDEO_TYPE_BVID,
                videoId = data.view.bvid,
                videoCid = data.view.cid
            )
        }
    })
    TitleBackground(
        title = when (state.currentPage) {
            0 -> "详情"
            1 -> "评论"
            2 -> "相关推荐"
            else -> ""
        },
        onBack = onBack,
        themeColor = color,
        ambientAlpha = ambientAlpha
        //backgroundColor = color,
    ) {
        HorizontalPager(state = state) {
            when (it) {
                0 -> VideoInformationScreen(
                    state = videoInformationViewModel.state,
                    videoInformationViewModel = videoInformationViewModel,
                    ambientColor = currentColor,
                    onJumpToSeason = onJumpToSeason
                )

                1 -> {
                    if (uploaderMid != 0L && oid != 0L) {
                        CommentScreen(
                            viewModel = commentViewModel,
                            commentDataFlow = commentDataFlow,
                            oid = oid,
                            uploaderMid = uploaderMid,
                            onJumpToVideo = onJumpToVideo,
                            onJumpToSearch = onJumpToSearch,
                            onJumpToImage = onJumpToImage
                        )
                    }
                }

                2 -> RelatedVideosScreen(viewModel = videoInformationViewModel, onJumpToVideo)

            }
        }
    }
}