package cn.spacexc.wearbili.remake.app.main.dynamic.ui

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import cn.spacexc.wearbili.remake.common.toUIState
import cn.spacexc.wearbili.remake.common.ui.LoadableBox
import cn.spacexc.wearbili.remake.common.ui.lazyRotateInput
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding

/**
 * Created by XC-Qan on 2023/4/27.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun DynamicScreen(
    viewModel: DynamicViewModel,
    context: Activity
) {
    val focusRequester = remember {
        FocusRequester()
    }
    val dynamicListData = viewModel.dynamicFlow.collectAsLazyPagingItems()
    val pullToRefreshState = rememberPullRefreshState(
        refreshing = dynamicListData.loadState.refresh is LoadState.Loading,
        onRefresh = { dynamicListData.refresh() },
        refreshThreshold = 40.dp
    )
    LoadableBox(
        uiState = dynamicListData.loadState.refresh.toUIState(), modifier = Modifier.fillMaxSize(),
        onRetry = dynamicListData::retry
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(state = pullToRefreshState)
        ) {
            LazyColumn(
                state = viewModel.scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .lazyRotateInput(focusRequester, viewModel.scrollState),
                contentPadding = PaddingValues(
                    vertical = 4.dp,
                    horizontal = titleBackgroundHorizontalPadding() - 3.dp
                )
            ) {
                items(dynamicListData.itemCount) {
                    dynamicListData[it]?.let { dynamicItem ->
                        DynamicCard(item = dynamicItem, context = context)
                    }
                }
            }
            LaunchedEffect(key1 = Unit, block = {
                focusRequester.requestFocus()
            })
            PullRefreshIndicator(
                refreshing = dynamicListData.loadState.refresh is LoadState.Loading,
                state = pullToRefreshState,
                modifier = Modifier.align(
                    Alignment.TopCenter
                )
            )
        }
    }
}