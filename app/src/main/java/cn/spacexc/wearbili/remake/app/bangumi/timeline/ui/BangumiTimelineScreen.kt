package cn.spacexc.wearbili.remake.app.bangumi.timeline.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cn.spacexc.bilibilisdk.sdk.bangumi.info.BANGUMI_ID_TYPE_EPID
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.BANGUMI_ID_TYPE_SSID
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.SmallBangumiCard
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.animateScrollAndCentralizeItem
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.scrollAndCentralizeItem
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateColorAsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/8/9.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

val CHINESE_NUMBERS = mapOf(
    1 to "一",
    2 to "二",
    3 to "三",
    4 to "四",
    5 to "五",
    6 to "六",
    7 to "日",
)

@kotlinx.serialization.Serializable
object BangumiTimelineScreen

@Composable
fun BangumiTimelineScreen(
    viewModel: BangumiTimelineViewModel = viewModel(),
    navController: NavController
) {
    LaunchedEffect(key1 = Unit) {
        if (viewModel.uiState != UIState.Success)
            viewModel.getBangumiTimeline()
    }
    val localDensity = LocalDensity.current
    val calendarRowState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    TitleBackground(
        navController = navController,
        title = "新番时间表",
        onBack = navController::navigateUp,
        uiState = viewModel.uiState,
        onRetry = viewModel::getBangumiTimeline
    ) {
        if (viewModel.timelineData.isNotEmpty() && viewModel.currentDate.isNotEmpty()) {
            val currentList =
                viewModel.timelineData.toList().find { it.first.first == viewModel.currentDate }
            currentList?.let { episodes ->
                LaunchedEffect(key1 = Unit, block = {
                    for (index in 0..6) {
                        calendarRowState.scrollAndCentralizeItem(index)
                        delay(10)
                    }   //不太优雅。。。但是这个scrollAndCentralizeItem的实现只能计算visible的item所以这么一个一个滑已经是我心目中的最优了
                })
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        vertical = 8.dp,
                        horizontal = titleBackgroundHorizontalPadding()
                    ),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    item(key = "calendar") {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            state = calendarRowState,
                            //contentPadding = PaddingValues(horizontal = 10.dp)
                        ) {
                            viewModel.timelineData.forEachIndexed { index, date ->
                                val day = date.first
                                item {
                                    val color by wearBiliAnimateColorAsState(
                                        targetValue = if (viewModel.currentDate == day.first) BilibiliPink else Color.Transparent,
                                        animationSpec = tween(durationMillis = 400)
                                    )
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.clickVfx {
                                            viewModel.currentDate = day.first
                                            coroutineScope.launch {
                                                calendarRowState.animateScrollAndCentralizeItem(
                                                    index
                                                )
                                            }
                                        }) {
                                        var textSize by remember {
                                            mutableStateOf(0.dp)
                                        }
                                        Text(text = day.first, style = AppTheme.typography.h3)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Box(
                                            modifier = Modifier
                                                .background(color, CircleShape)
                                                .size(textSize + 12.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = CHINESE_NUMBERS[day.second] ?: "空",
                                                style = AppTheme.typography.h2,
                                                modifier = Modifier
                                                    .onSizeChanged {
                                                        textSize =
                                                            with(localDensity) { it.height.toDp() }
                                                    }
                                                    .apply {
                                                        if (index == 6 /*同上*/) border(
                                                            width = 0.3.dp,
                                                            color = BilibiliPink
                                                        )
                                                    }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth(0.2f)
                        )
                    }
                    episodes.second.entries.forEach { entry ->
                        item(key = entry.key) {
                            Text(
                                text = entry.key,
                                style = AppTheme.typography.h3,
                                color = Color.White,
                            )
                        }
                        entry.value.forEach { episode ->
                            item(episode.episode_id) {
                                val published = episode.published == 1
                                SmallBangumiCard(
                                    title = episode.title,
                                    cover = episode.square_cover,
                                    epName = episode.delay_reason.ifEmpty { episode.pub_index },
                                    bangumiId = if (published) episode.episode_id else episode.season_id,
                                    bangumiIdType = if (published) BANGUMI_ID_TYPE_EPID else BANGUMI_ID_TYPE_SSID,
                                    navController = navController,
                                    modifier = Modifier
                                        .alpha(if (episode.delay_reason.isEmpty()) 1f else 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}