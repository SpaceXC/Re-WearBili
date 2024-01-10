package cn.spacexc.wearbili.remake.ui.main.profile.detail.favorite.detail.ui

import androidx.compose.foundation.layout.Arrangement
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
import cn.spacexc.wearbili.common.domain.video.toShortChinese
import cn.spacexc.wearbili.remake.common.toUIState
import cn.spacexc.wearbili.remake.common.ui.LoadingTip
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.VideoCard
import cn.spacexc.wearbili.remake.common.ui.toLoadingState
import cn.spacexc.wearbili.remake.ui.video.info.ui.VIDEO_TYPE_BVID

/**
 * Created by XC-Qan on 2023/8/19.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val FavouriteFolderDetailScreenRoute = "favourite_folder_detail"

fun NavGraphBuilder.favouriteFolderDetailScreen(
    onBack: () -> Unit,
    onJumpToVideo: (String, String) -> Unit
) {
    composable("$FavouriteFolderDetailScreenRoute/{folderName}/{folderId}") { backStackEntry ->
        backStackEntry.arguments?.let { args ->
            val viewModel: FavoriteFolderDetailViewModel = hiltViewModel()
            val folderId = args.getLong("folderId")
            val folderName = args.getString("folderName") ?: ""
            FavouriteFolderDetailScreen(
                viewModel = viewModel,
                folderName = folderName,
                folderId = folderId,
                onBack = onBack,
                onJumpToVideo = onJumpToVideo
            )
        }
    }
}

fun NavController.navigateToFavouriteFolder(folderName: String, folderId: Long) {
    navigate("$FavouriteFolderDetailScreenRoute/$folderName/$folderId")
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavouriteFolderDetailScreen(
    viewModel: FavoriteFolderDetailViewModel,
    folderName: String,
    folderId: Long,
    onBack: () -> Unit,
    onJumpToVideo: (String, String) -> Unit
) {
    val dataFlow = viewModel.getPagerFlow(folderId)
    val lazyPagingItem = dataFlow.collectAsLazyPagingItems()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = lazyPagingItem.loadState.refresh is LoadState.Loading,
        onRefresh = lazyPagingItem::refresh,
        refreshThreshold = 40.dp
    )
    TitleBackground(
        title = folderName,
        onBack = onBack,
        uiState = lazyPagingItem.loadState.refresh.toUIState()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(state = pullRefreshState)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(lazyPagingItem.itemCount) {
                    lazyPagingItem[it]?.let { video ->
                        VideoCard(
                            videoName = video.title,
                            uploader = video.upper.name,
                            views = video.cnt_info.play.toShortChinese(),
                            coverUrl = video.cover,
                            videoIdType = VIDEO_TYPE_BVID,
                            videoId = video.bvid,
                            onJumpToVideo = onJumpToVideo
                        )
                    }
                }
                item {
                    LoadingTip(lazyPagingItem.loadState.append.toLoadingState())
                }
            }
            PullRefreshIndicator(
                refreshing = lazyPagingItem.loadState.refresh is LoadState.Loading,
                state = pullRefreshState,
                modifier = Modifier.align(
                    Alignment.TopCenter
                )
            )
        }
    }
}