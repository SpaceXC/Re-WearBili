package cn.spacexc.wearbili.remake.ui.main.profile.detail.history.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.wear.compose.navigation.composable
import cn.spacexc.wearbili.common.domain.time.secondToTime
import cn.spacexc.wearbili.remake.common.toUIState
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

const val HistoryScreenRoute = "history_screen"

fun NavGraphBuilder.history(
    onBack: () -> Unit,
    onJumpToVideo: (String, String) -> Unit
) {
    composable(HistoryScreenRoute) {
        HistoryScreen(onBack = onBack, onJumpToVideo = onJumpToVideo)
    }
}

fun NavController.navigateToHistory() = navigate(HistoryScreenRoute)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onJumpToVideo: (String, String) -> Unit
) {
    val lazyListItems = viewModel.historyPagerFlow.collectAsLazyPagingItems()
    val pullToRefreshState = rememberPullRefreshState(
        refreshing = lazyListItems.loadState.refresh is LoadState.Loading,
        onRefresh = { lazyListItems.refresh() },
        refreshThreshold = 40.dp
    )
    TitleBackground(
        title = "历史记录",
        onBack = onBack,
        uiState = lazyListItems.loadState.refresh.toUIState()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(state = pullToRefreshState)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    vertical = 4.dp,
                    horizontal = TitleBackgroundHorizontalPadding - 4.dp
                )
            ) {
                items(lazyListItems.itemCount) {
                    lazyListItems[it]?.let { item ->
                        when (item.history.business) {
                            "archive" -> {
                                VideoCard(
                                    videoName = buildString {
                                        if (item.show_title.isNotEmpty()) {
                                            append(item.show_title)
                                            append(" - ")
                                        }
                                        append(item.title)
                                    },
                                    uploader = item.author_name,
                                    badge = item.badge,
                                    views = buildString {
                                        if (item.progress != -1) {
                                            append("看到")
                                            append(item.progress.secondToTime())
                                            if (item.new_desc.isNotEmpty()) {
                                                append(" | ")
                                                append(item.new_desc)
                                            }
                                        } else {
                                            append("已看完")
                                        }
                                    },
                                    coverUrl = item.cover,
                                    videoId = item.history.bvid,
                                    videoIdType = VIDEO_TYPE_BVID,
                                    onJumpToVideo = onJumpToVideo
                                )
                            }

                            "pgc" -> {

                            }
                        }

                    }
                }
            }
            PullRefreshIndicator(
                refreshing = lazyListItems.loadState.refresh is LoadState.Loading,
                state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}