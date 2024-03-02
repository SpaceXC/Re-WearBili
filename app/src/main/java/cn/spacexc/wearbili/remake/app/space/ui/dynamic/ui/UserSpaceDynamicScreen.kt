package cn.spacexc.wearbili.remake.app.space.ui.dynamic.ui

import android.app.Activity
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
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list.DynamicItem
import cn.spacexc.wearbili.remake.app.main.dynamic.ui.DynamicCard
import cn.spacexc.wearbili.remake.app.space.ui.UserSpaceViewModel
import cn.spacexc.wearbili.remake.common.toUIState
import cn.spacexc.wearbili.remake.common.ui.LoadableBox
import cn.spacexc.wearbili.remake.common.ui.LoadingTip
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.isRound
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.toLoadingState
import kotlinx.coroutines.flow.Flow

@Composable
fun Activity.UserSpaceDynamicScreen(
    pagingItems: Flow<PagingData<DynamicItem>>,
    viewModel: UserSpaceViewModel,
    listState: LazyListState
) {
    val lazyItems = pagingItems.collectAsLazyPagingItems()
    LoadableBox(uiState = lazyItems.loadState.refresh.toUIState(), onRetry = lazyItems::retry) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                vertical = 6.dp,
                horizontal = TitleBackgroundHorizontalPadding() - 3.dp
            ),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            state = listState
        ) {
            item {
                viewModel.info?.let { user ->
                    Text(
                        text = "${user.name}的",
                        fontFamily = wearbiliFontFamily,
                        fontSize = 14.spx,
                        color = Color.White,
                        modifier = Modifier
                            .alpha(0.7f)
                            .fillMaxWidth(),
                        fontWeight = FontWeight.Medium,
                        textAlign = if (isRound()) TextAlign.Center else TextAlign.Start
                    )
                    Text(
                        text = "动态",
                        color = Color.White,
                        fontFamily = wearbiliFontFamily,
                        fontSize = 22.spx,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = if (isRound()) TextAlign.Center else TextAlign.Start
                    )
                }
            }
            items(count = lazyItems.itemCount) {
                lazyItems[it]?.let { dynamicItem ->
                    DynamicCard(item = dynamicItem, context = this@UserSpaceDynamicScreen)
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