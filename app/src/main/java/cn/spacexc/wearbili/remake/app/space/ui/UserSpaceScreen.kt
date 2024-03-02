package cn.spacexc.wearbili.remake.app.space.ui

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.app.space.ui.dynamic.ui.UserSpaceDynamicScreen
import cn.spacexc.wearbili.remake.app.space.ui.info.UserInformationScreen
import cn.spacexc.wearbili.remake.app.space.ui.videos.ui.UserSpaceVideosScreen
import cn.spacexc.wearbili.remake.common.ui.HorizontalPagerIndicator
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.isRound

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Activity.UserSpaceScreen(
    viewModel: UserSpaceViewModel,
    mid: Long
) {
    val pagerState = rememberPagerState { 3 }

    val videoPagingItems = remember {
        viewModel.getUserVideosPagingItems(mid)
    }
    val videoListState = rememberLazyListState()

    val dynamicPagingItems = remember {
        viewModel.getUserDynamicPagingItems(mid)
    }
    val dynamicListState = rememberLazyListState()
    //val videoListFirstVisibleItem = remember

    val currentTitle by remember {
        derivedStateOf {
            when (pagerState.currentPage) {
                0 -> ""
                1 -> if (videoListState.firstVisibleItemIndex != 0) "投稿" else ""
                2 -> if (dynamicListState.firstVisibleItemIndex != 0) "动态" else ""
                else -> ""
            }
        }
    }

    TitleBackground(
        title = currentTitle,
        onRetry = {
            viewModel.getUserSpace(mid)
        },
        uiState = viewModel.uiState,
        onBack = ::finish,
        themeImageUrl = viewModel.info?.face ?: "",
        networkUtils = viewModel.networkUtils
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> UserInformationScreen(
                        viewModel = viewModel
                    )

                    1 -> UserSpaceVideosScreen(
                        pagingItems = videoPagingItems,
                        viewModel = viewModel,
                        listState = videoListState
                    )

                    2 -> UserSpaceDynamicScreen(
                        pagingItems = dynamicPagingItems,
                        viewModel = viewModel,
                        listState = dynamicListState
                    )
                }
            }
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(if (isRound()) Alignment.TopCenter else Alignment.TopEnd)
                    .padding(end = if (isRound()) 0.dp else TitleBackgroundHorizontalPadding() + 2.dp),
                activeColor = Color(255, 255, 255),
                inactiveColor = Color(
                    255,
                    255,
                    255,
                    128
                ),
                spacing = 4.dp,
                indicatorWidth = 6.dp,
                indicatorHeight = 6.dp
            )
        }
    }
}