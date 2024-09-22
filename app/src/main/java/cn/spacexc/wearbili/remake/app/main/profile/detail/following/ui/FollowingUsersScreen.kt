package cn.spacexc.wearbili.remake.app.main.profile.detail.following.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import cn.spacexc.bilibilisdk.sdk.user.follow.following.remote.user.Data
import cn.spacexc.wearbili.remake.app.space.ui.UserSpaceScreen
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.LoadingTip
import cn.spacexc.wearbili.remake.common.ui.TinyUserCard
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.WearBiliAnimatedVisibility
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.toLoadingState
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/6/23.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@kotlinx.serialization.Serializable
object FollowingUsersScreen

@Composable
fun FollowingUsersScreen(
    viewModel: FollowingUsersViewModel = viewModel(),
    navController: NavController,
    lazyPagingItems: List<LazyPagingItems<Data>>
) {
    val localDensity = LocalDensity.current

    LaunchedEffect(key1 = Unit) {
        if (viewModel.uiState != UIState.Success)
            viewModel.getFollowedUserTags()
    }

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { viewModel.followedUserTags.size })

    var tagRowHeight by remember {
        mutableStateOf(0.dp)
    }
    var isTagRowShowing by remember {
        mutableStateOf(true)
    }

    viewModel.lazyColumnStates.forEach { (index, lazyListState) ->
        LaunchedEffect(key1 = lazyListState.firstVisibleItemIndex, key2 = pagerState.currentPage) {
            if (pagerState.currentPage == index) {
                isTagRowShowing = lazyListState.firstVisibleItemIndex == 0
            }
        }
    }

    TitleBackground(
        navController = navController,
        title = if (viewModel.followedUserTags.isEmpty()) "" else viewModel.followedUserTags[pagerState.currentPage].name,
        uiState = viewModel.uiState,
        onBack = navController::navigateUp,
        onRetry = viewModel::getFollowedUserTags,
        isTitleClipToBounds = true
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
            ) { page ->
                val items = lazyPagingItems[page]
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = 8.dp + tagRowHeight,
                        bottom = 8.dp,
                        start = titleBackgroundHorizontalPadding() - 3.dp,
                        end = titleBackgroundHorizontalPadding() - 3.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    state = viewModel.lazyColumnStates[page]!!
                ) {
                    items(items.itemCount) {
                        val user = items[it]
                        user?.let {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickVfx {
                                        navController.navigate(UserSpaceScreen(user.mid))
                                    }
                                    .background(Color(18, 18, 18), RoundedCornerShape(10.dp))
                                    .padding()
                            ) {
                                TinyUserCard(
                                    avatar = user.face,
                                    username = user.uname,
                                    navController = null,
                                    mid = user.mid
                                )
                            }
                        }
                    }
                    item {
                        LoadingTip(
                            loadingState = items.loadState.append.toLoadingState(),
                            onRetry = items::retry
                        )
                    }
                }
            }
            WearBiliAnimatedVisibility(
                visible = isTagRowShowing,
                enter = slideInVertically { -it } + fadeIn(),
                exit = slideOutVertically { -it } + fadeOut()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { tagRowHeight = with(localDensity) { it.height.toDp() } },
                    contentPadding = PaddingValues(
                        horizontal = titleBackgroundHorizontalPadding() - 3.dp,
                        vertical = 2.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    viewModel.followedUserTags.forEachIndexed { index, tag ->
                        item {
                            Card(
                                isHighlighted = pagerState.currentPage == index,
                                outerPaddingValues = PaddingValues(),
                                innerPaddingValues = PaddingValues(
                                    vertical = 8.dp,
                                    horizontal = 12.dp
                                ),
                                shape = CircleShape,
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }) {
                                Text(
                                    text = tag.name,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = wearbiliFontFamily
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}