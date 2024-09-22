package cn.spacexc.wearbili.remake.app.space.ui.videos.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import cn.spacexc.bilibilisdk.sdk.user.profile.remote.video.app.Item
import cn.spacexc.wearbili.common.domain.video.toShortChinese
import cn.spacexc.wearbili.remake.app.space.ui.UserSpaceViewModel
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.toUIState
import cn.spacexc.wearbili.remake.common.ui.LoadableBox
import cn.spacexc.wearbili.remake.common.ui.LoadingTip
import cn.spacexc.wearbili.remake.common.ui.VideoCardWithNoBorder
import cn.spacexc.wearbili.remake.common.ui.isRound
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.toLoadingState
import kotlinx.coroutines.flow.Flow

@Composable
fun UserSpaceVideosScreen(
    pagingItems: Flow<PagingData<Item>>,
    viewModel: UserSpaceViewModel,
    listState: LazyListState,
    navController: NavController
) {
    val lazyItems = pagingItems.collectAsLazyPagingItems()
    LoadableBox(uiState = lazyItems.loadState.refresh.toUIState(), onRetry = lazyItems::retry) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                vertical = 6.dp,
                horizontal = titleBackgroundHorizontalPadding() - 3.dp
            ),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            state = listState
        ) {
            item {
                viewModel.info?.let { user ->
                    Text(
                        text = "${user.name}的",
                        fontFamily = wearbiliFontFamily,
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier
                            .alpha(0.7f)
                            .fillMaxWidth(),
                        fontWeight = FontWeight.Medium,
                        textAlign = if (isRound()) TextAlign.Center else TextAlign.Start
                    )
                    Text(
                        text = "投稿",
                        color = Color.White,
                        fontFamily = wearbiliFontFamily,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = if (isRound()) TextAlign.Center else TextAlign.Start
                    )
                }
            }
            items(count = lazyItems.itemCount) {
                lazyItems[it]?.let { video ->
                    VideoCardWithNoBorder(
                        videoName = video.title,
                        views = video.play.toShortChinese(),
                        coverUrl = video.cover,
                        danmaku = video.danmaku.toShortChinese(),
                        videoIdType = VIDEO_TYPE_BVID,
                        videoId = video.bvid,
                        navController = navController
                    )
                }
            }
            item {
                LoadingTip(
                    loadingState = lazyItems.loadState.append.toLoadingState(),
                    onRetry = lazyItems::retry
                )
            }
        }
    }
}