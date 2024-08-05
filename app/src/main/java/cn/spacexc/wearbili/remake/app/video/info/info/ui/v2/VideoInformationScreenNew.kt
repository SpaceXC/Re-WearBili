package cn.spacexc.wearbili.remake.app.video.info.info.ui.v2

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationScreenState
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationViewModel
import cn.spacexc.wearbili.remake.app.video.info.info.ui.v2.action.VideoActionsScreen
import cn.spacexc.wearbili.remake.common.ui.LoadableBox
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateFloatAsState

/**
 * Created by XC-Qan on 2023/4/12.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Activity.VideoInformationScreenNew(
    state: VideoInformationScreenState,
    context: Activity,
    videoInformationViewModel: VideoInformationViewModel,
    videoIdType: String,
    videoId: String,
    isDetail: Boolean,
    onRetry: () -> Unit,
    onGoToDetail: () -> Unit
) {
    val pagerState = rememberPagerState {
        2
    }
    LoadableBox(uiState = state.uiState, onRetry = { onRetry() }, modifier = Modifier.graphicsLayer { clip = false }) {
        VerticalPager(state = pagerState, modifier = Modifier.graphicsLayer { clip = false }) { page ->
            when(page) {
                0 -> BasicVideoInformation(viewModel = videoInformationViewModel, isDetail = isDetail, onGoToDetail = onGoToDetail)
                1 -> VideoActionsScreen(
                    state = state,
                    context = this@VideoInformationScreenNew,
                    videoInformationViewModel = videoInformationViewModel,
                    videoIdType = videoIdType,
                    videoId = videoId
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
                //.alpha(1f)
                .offset(y = (-20).dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            repeat(pagerState.pageCount) {
                val indicationAlpha by wearBiliAnimateFloatAsState(targetValue = if (pagerState.currentPage == it) 0.8f else 0.3f)
                Box(
                    modifier = Modifier
                        .alpha(indicationAlpha)
                        .size(5.dp)
                        .background(Color.White, CircleShape)
                )
            }
        }
    }
}