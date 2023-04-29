package cn.spacexc.wearbili.remake.app.video.info.ui

import android.content.Context
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
@Composable
fun VideoInformationActivityScreen(
    context: Context,
    state: VideoInformationActivityScreenState,
    videoInformationScreenState: VideoInformationScreenState,
    commentViewModel: CommentViewModel,
    commentDataFlow: Flow<PagingData<CommentContentData>>,
    uploaderMid: Long,
    oid: Long,
    onBack: () -> Unit
) {
    TitleBackground(
        title = when (state.pagerState.currentPage) {
            0 -> "详情"
            1 -> "评论"
            else -> ""
        },
        onBack = onBack
    ) {
        HorizontalPager(pageCount = 2) {
            when (it) {
                0 -> VideoInformationScreen(state = videoInformationScreenState, context)
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
            }
        }
    }
}