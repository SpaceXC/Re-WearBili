package cn.spacexc.wearbili.remake.app.video.info.ui

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationScreen
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationScreenState
import cn.spacexc.wearbili.remake.common.ui.TitleBackground

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
                1 -> Text(text = "评论", modifier = Modifier.fillMaxSize())
            }
        }
    }
}