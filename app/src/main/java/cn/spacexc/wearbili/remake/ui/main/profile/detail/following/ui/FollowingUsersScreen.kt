package cn.spacexc.wearbili.remake.ui.main.profile.detail.following.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.wear.compose.navigation.composable
import cn.spacexc.wearbili.remake.common.toUIState
import cn.spacexc.wearbili.remake.common.ui.LoadableBox
import cn.spacexc.wearbili.remake.common.ui.LoadingTip
import cn.spacexc.wearbili.remake.common.ui.TinyUserCard
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.toLoadingState

/**
 * Created by XC-Qan on 2023/6/23.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val FollowingUsersScreenRoute = "following_users_screen"

fun NavGraphBuilder.followingUsers(
    onBack: () -> Unit
) {
    composable(FollowingUsersScreenRoute) {
        FollowingUsersScreen(onBack = onBack)
    }
}

fun NavController.navigateToFollowingUsers() = navigate(FollowingUsersScreenRoute)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FollowingUsersScreen(
    viewModel: FollowingUsersViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val followingTags by viewModel.followedUserTags.collectAsState()
    val pagerState = rememberPagerState(pageCount = { followingTags.size })
    val followingUsersByTags by viewModel.followedUsers.collectAsState(initial = emptyList())

    TitleBackground(
        title = if (followingTags.isEmpty()) "" else followingTags[pagerState.currentPage].name,
        uiState = viewModel.uiState,
        onBack = onBack
    ) {
        HorizontalPager(
            //pageCount = followingTags.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(
                        36,
                        36,
                        36,
                        199
                    ),
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
                .border(
                    width = 0.05.dp,
                    color = Color(112, 112, 112, 179),
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
        ) { page ->
            val items = followingUsersByTags[page].collectAsLazyPagingItems()
            LoadableBox(uiState = items.loadState.refresh.toUIState()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(items.itemCount) {
                        val user = items[it]
                        user?.let {
                            TinyUserCard(avatar = user.face, username = user.uname)
                        }
                    }
                    item {
                        LoadingTip(loadingState = items.loadState.append.toLoadingState())
                    }
                }
            }
        }
    }
}