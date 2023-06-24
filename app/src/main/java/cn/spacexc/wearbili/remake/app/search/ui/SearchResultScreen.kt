package cn.spacexc.wearbili.remake.app.search.ui

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
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cn.spacexc.wearbili.common.domain.video.toShortChinese
import cn.spacexc.wearbili.remake.app.search.domain.paging.SearchObject
import cn.spacexc.wearbili.remake.app.search.domain.remote.result.user.SearchedUser
import cn.spacexc.wearbili.remake.app.search.domain.remote.result.video.SearchedVideo
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.ui.LargeUserCard
import cn.spacexc.wearbili.remake.common.ui.LoadingState
import cn.spacexc.wearbili.remake.common.ui.LoadingTip
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.VideoCard
import cn.spacexc.wearbili.remake.common.ui.toOfficialVerify
import kotlinx.coroutines.flow.Flow

/**
 * Created by XC-Qan on 2023/5/3.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchResultScreen(
    onBack: () -> Unit,
    flow: Flow<PagingData<SearchObject>>
) {
    val searchResult = flow.collectAsLazyPagingItems()
    val pullToRefreshState = rememberPullRefreshState(
        refreshing = searchResult.loadState.refresh is LoadState.Loading,
        onRefresh = searchResult::refresh,
        refreshThreshold = 60.dp
    )
    TitleBackground(
        title = "搜索结果", onBack = onBack, uiState = when (searchResult.loadState.refresh) {
            is LoadState.Error -> UIState.Failed
            is LoadState.Loading -> UIState.Loading
            else -> UIState.Success
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(state = pullToRefreshState)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
            ) {
                items(searchResult) {
                    it?.let { searchObject ->
                        when (searchObject.item) {
                            is SearchedVideo -> {
                                val video = searchObject.item
                                VideoCard(
                                    videoName = video.title.replace("<em class=\"keyword\">", "")
                                        .replace("</em>", ""),
                                    uploader = video.author,
                                    views = video.play.toShortChinese(),
                                    coverUrl = "https:" + video.pic,
                                    videoIdType = VIDEO_TYPE_BVID,
                                    videoId = video.bvid
                                )
                            }

                            is SearchedUser -> {
                                val user = searchObject.item
                                LargeUserCard(
                                    avatar = "https:" + user.upic,
                                    username = user.uname,
                                    mid = user.mid,
                                    officialVerify = user.officialVerify.type.toOfficialVerify(),
                                    userInfo = (if (user.officialVerify.desc.isEmpty()) "" else user.officialVerify.desc + "\n") + user.usign
                                )
                            }
                        }
                    }
                }
                item {
                    LoadingTip(
                        loadingState = when (searchResult.loadState.refresh) {
                            is LoadState.Loading -> LoadingState.Loading
                            is LoadState.Error -> LoadingState.Failed
                            else -> LoadingState.NoMore
                        },
                        onRetry = searchResult::retry
                    )
                }
            }
            PullRefreshIndicator(
                refreshing = searchResult.loadState.refresh is LoadState.Loading,
                state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}