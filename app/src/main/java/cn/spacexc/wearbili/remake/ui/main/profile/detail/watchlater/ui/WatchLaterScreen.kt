package cn.spacexc.wearbili.remake.ui.main.profile.detail.watchlater.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import cn.spacexc.wearbili.common.domain.time.secondToTime
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.VideoCard
import cn.spacexc.wearbili.remake.ui.video.info.ui.VIDEO_TYPE_BVID

/**
 * Created by XC-Qan on 2023/6/24.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val WatchLaterScreenRoute = "watch_later"

fun NavGraphBuilder.watchLater(onBack: () -> Unit, onJumpToVideo: (String, String) -> Unit) {
    composable(WatchLaterScreenRoute) {
        WatchLaterScreen(onBack = onBack, onJumpToVideo = onJumpToVideo)
    }
}

fun NavController.navigateToWatchLater() = navigate(WatchLaterScreenRoute)

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun WatchLaterScreen(
    viewModel: WatchLaterViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onJumpToVideo: (String, String) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.isRefreshing,
        onRefresh = {
            viewModel.isRefreshing = true
            viewModel.getWatchLaterItems()
        }, refreshThreshold = 40.dp
    )
    TitleBackground(title = "稍后再看", onBack = onBack, uiState = viewModel.uiState) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(state = pullRefreshState)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = TitleBackgroundHorizontalPadding - 4.dp,
                    vertical = 4.dp
                )
            ) {
                viewModel.watchLaterList.forEach { item ->
                    item(key = item.aid) {
                        val dismissState = rememberDismissState()
                        if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                            viewModel.removeFromWatchLater(item.aid)
                        }
                        SwipeToDismiss(
                            state = dismissState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItemPlacement(),
                            directions = setOf(DismissDirection.EndToStart),
                            dismissThresholds = { direction ->
                                FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
                            },
                            background = {}
                        ) {
                            VideoCard(
                                videoName = item.title,
                                uploader = item.owner.name,
                                views = item.duration.secondToTime(),
                                coverUrl = item.pic,
                                videoIdType = VIDEO_TYPE_BVID,
                                videoId = item.bvid,
                                badge = if (item.viewed) "已看完" else null,
                                onJumpToVideo = onJumpToVideo
                            )
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = viewModel.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(
                    Alignment.TopCenter
                )
            )
        }
    }
}