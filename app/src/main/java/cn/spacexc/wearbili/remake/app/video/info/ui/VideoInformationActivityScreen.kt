package cn.spacexc.wearbili.remake.app.video.info.ui

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.paging.PagingData
import androidx.palette.graphics.Palette
import cn.spacexc.wearbili.remake.app.video.info.comment.domain.CommentContentData
import cn.spacexc.wearbili.remake.app.video.info.comment.ui.CommentScreen
import cn.spacexc.wearbili.remake.app.video.info.comment.ui.CommentViewModel
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationScreen
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationScreenState
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationViewModel
import cn.spacexc.wearbili.remake.app.video.info.related.RelatedVideosScreen
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
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
fun VideoInformationActivityScreen(
    context: Activity,
    state: PagerState,
    videoInformationScreenState: VideoInformationScreenState,
    videoInformationViewModel: VideoInformationViewModel,
    commentViewModel: CommentViewModel,
    commentDataFlow: Flow<PagingData<CommentContentData>>?,
    uploaderMid: Long,
    oid: Long,
    onBack: () -> Unit
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
            /*videoInformationViewModel.downloadWebMask(
                videoIdType = VIDEO_TYPE_BVID,
                videoId = data.view.bvid,
                videoCid = data.view.cid
            )*/
        }
    })
    context.TitleBackground(
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
                    state = videoInformationScreenState,
                    context,
                    videoInformationViewModel,
                    currentColor
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

                2 -> RelatedVideosScreen(viewModel = videoInformationViewModel)

            }
        }
    }
}