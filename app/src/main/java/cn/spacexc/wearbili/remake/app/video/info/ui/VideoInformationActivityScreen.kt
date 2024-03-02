package cn.spacexc.wearbili.remake.app.video.info.ui

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import cn.spacexc.wearbili.remake.app.video.info.comment.domain.CommentContentData
import cn.spacexc.wearbili.remake.app.video.info.comment.ui.CommentScreen
import cn.spacexc.wearbili.remake.app.video.info.comment.ui.CommentViewModel
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationScreen
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationScreenState
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationViewModel
import cn.spacexc.wearbili.remake.app.video.info.related.RelatedVideosScreen
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import kotlinx.coroutines.flow.Flow

/**
 * Created by XC-Qan on 2023/4/12.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@OptIn(ExperimentalFoundationApi::class)
/*@UnstableApi*/
@Composable
fun Activity.VideoInformationActivityScreen(
    context: Activity,
    state: PagerState,
    videoInformationScreenState: VideoInformationScreenState,
    videoInformationViewModel: VideoInformationViewModel,
    commentViewModel: CommentViewModel,
    commentDataFlow: Flow<PagingData<CommentContentData>>?,
    uploaderMid: Long,
    oid: Long,
    videoIdType: String,
    videoId: String,
    onBack: () -> Unit
) {

    context.TitleBackground(
        title = when (state.currentPage) {
            0 -> "详情"
            1 -> "评论"
            2 -> "相关推荐"
            else -> ""
        },
        onBack = onBack,
        themeImageUrl = videoInformationViewModel.state.videoData?.view?.pic?.replace(
            "http://",
            "https://"
        ) ?: "",
        networkUtils = videoInformationViewModel.ktorNetworkUtils
        //backgroundColor = color,
    ) {
        HorizontalPager(state = state) {
            when (it) {
                0 -> VideoInformationScreen(
                    state = videoInformationScreenState,
                    context = context,
                    videoInformationViewModel = videoInformationViewModel,
                    videoIdType = videoIdType, videoId = videoId
                )

                1 -> {
                    CommentScreen(
                        viewModel = commentViewModel,
                        commentDataFlow = commentDataFlow,
                        oid = oid,
                        uploaderMid = uploaderMid,
                        context = context
                    )
                }

                2 -> RelatedVideosScreen(viewModel = videoInformationViewModel)

            }
        }
    }
}