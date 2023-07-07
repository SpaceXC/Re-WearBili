package cn.spacexc.wearbili.remake.app.video.info.ui

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.media3.common.util.UnstableApi
import androidx.paging.PagingData
import cn.spacexc.wearbili.remake.app.video.info.comment.domain.CommentContentData
import cn.spacexc.wearbili.remake.app.video.info.comment.ui.CommentScreen
import cn.spacexc.wearbili.remake.app.video.info.comment.ui.CommentViewModel
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationScreen
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationScreenState
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationViewModel
import cn.spacexc.wearbili.remake.app.video.info.related.RelatedVideosScreen
import cn.spacexc.wearbili.remake.app.video.info.related.RelatedVideosViewModel
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import kotlinx.coroutines.flow.Flow

/**
 * Created by XC-Qan on 2023/4/12.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

data class VideoInformationActivityScreenState @OptIn(ExperimentalFoundationApi::class) constructor(
    val pagerState: PagerState = PagerState(initialPage = 0),
)

@OptIn(ExperimentalFoundationApi::class)
@UnstableApi
@Composable
fun VideoInformationActivityScreen(
    context: Context,
    state: VideoInformationActivityScreenState,
    videoInformationScreenState: VideoInformationScreenState,
    videoInformationViewModel: VideoInformationViewModel,
    commentViewModel: CommentViewModel,
    commentDataFlow: Flow<PagingData<CommentContentData>>,
    relatedVideosViewModel: RelatedVideosViewModel,
    uploaderMid: Long,
    oid: Long,
    onBack: () -> Unit
) {
    context.TitleBackground(
        title = when (state.pagerState.currentPage) {
            0 -> "详情"
            1 -> "评论"
            2 -> "相关推荐"
            else -> ""
        },
        onBack = onBack
    ) {
        HorizontalPager(pageCount = 3, state = state.pagerState) {
            when (it) {
                0 -> VideoInformationScreen(
                    state = videoInformationScreenState,
                    context,
                    videoInformationViewModel
                )

                1 -> {
                    if (uploaderMid != 0L && oid != 0L) {
                        CommentScreen(
                            viewModel = commentViewModel,
                            commentDataFlow = commentDataFlow,
                            oid = oid,
                            uploaderMid = uploaderMid,
                            context = context
                        )
                    }
                }

                2 -> RelatedVideosScreen(viewModel = relatedVideosViewModel)

            }
        }
    }
}