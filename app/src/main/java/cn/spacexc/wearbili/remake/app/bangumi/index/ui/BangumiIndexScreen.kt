package cn.spacexc.wearbili.remake.app.bangumi.index.ui

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.BANGUMI_ID_TYPE_SSID
import cn.spacexc.wearbili.remake.app.bangumi.timeline.ui.BangumiTimelineActivity
import cn.spacexc.wearbili.remake.common.toUIState
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.LargeBangumiCard
import cn.spacexc.wearbili.remake.common.ui.LoadingTip
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.toLoadingState

/**
 * Created by XC-Qan on 2023/8/8.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
        /*@UnstableApi*/
fun Activity.BangumiIndexScreen(
    viewModel: BangumiIndexViewModel
) {
    val lazyItems = viewModel.pager.collectAsLazyPagingItems()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = lazyItems.loadState.refresh is LoadState.Loading,
        onRefresh = lazyItems::refresh,
        refreshThreshold = 40.dp
    )
    TitleBackground(
        title = "追番索引",
        onBack = ::finish,
        uiState = lazyItems.loadState.refresh.toUIState(),
        onRetry = lazyItems::retry
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            PullRefreshIndicator(
                refreshing = lazyItems.loadState.refresh is LoadState.Loading,
                state = pullRefreshState,
                modifier = Modifier.align(
                    Alignment.TopCenter
                )
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item {
                    Column {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            //outerPaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            innerPaddingValues = PaddingValues(12.dp),
                            shape = CircleShape,
                            onClick = {
                                startActivity(
                                    Intent(
                                        this@BangumiIndexScreen,
                                        BangumiTimelineActivity::class.java
                                    )
                                )
                            }
                        ) {
                            IconText(
                                text = "新番时间表",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.align(
                                    Alignment.Center
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CalendarMonth,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth(0.2f)
                                .align(Alignment.CenterHorizontally), color = Color(42, 42, 42)
                        )
                    }
                }
                items(lazyItems.itemCount) {
                    val bangumi = lazyItems[it]
                    bangumi?.let {
                        LargeBangumiCard(
                            title = bangumi.title,
                            cover = bangumi.cover,
                            score = bangumi.score.toFloatOrNull() ?: 0.0f,
                            areas = listOf(bangumi.order),
                            updateInformation = bangumi.index_show,
                            bangumiId = bangumi.season_id,
                            bangumiIdType = BANGUMI_ID_TYPE_SSID,
                            badge = listOf(bangumi.badge),
                            badgeColor = listOf(bangumi.badge_info.bg_color),
                            context = this@BangumiIndexScreen
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
}