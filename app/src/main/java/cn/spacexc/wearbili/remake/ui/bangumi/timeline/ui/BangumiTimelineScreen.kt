package cn.spacexc.wearbili.remake.ui.bangumi.timeline.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import cn.spacexc.bilibilisdk.sdk.bangumi.info.BANGUMI_ID_TYPE_EPID
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.SmallBangumiCard
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.animateScrollAndCentralizeItem
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.ui.bangumi.info.ui.BANGUMI_ID_TYPE_SSID
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

const val BangumiTimelineScreenRoute = "bangumi_timeline_screen"

fun NavGraphBuilder.bangumiTimeline(
    onBack: () -> Unit,
    onJumpToBangumiDetail: (String, Long) -> Unit
) {
    composable(BangumiTimelineScreenRoute) {
        BangumiTimelineScreen(onBack = onBack, onJumpToBangumiDetail = onJumpToBangumiDetail)
    }
}

fun NavController.navigateToBangumiTimeline() = navigate(BangumiTimelineScreenRoute)

@Composable
fun BangumiTimelineScreen(
    viewModel: BangumiTimelineViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onJumpToBangumiDetail: (String, Long) -> Unit,
) {
    val localDensity = LocalDensity.current
    val calendarRowState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit, block = {
        coroutineScope.launch {
            calendarRowState.animateScrollToItem(6) //因为在请求数据时设置了before=after=6（前后6天），所以这里想要居中第一个元素就可以直接这样子
            //FIXME known-bug: 初始化状态无法居中
        }
    })
    TitleBackground(title = "新番时间表", onBack = onBack, uiState = viewModel.uiState) {
        if (viewModel.timelineData.isNotEmpty() && viewModel.currentDate.isNotEmpty()) {
            val currentList =
                viewModel.timelineData.toList().find { it.first.first == viewModel.currentDate }
            currentList?.let { episodes ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    item {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            state = calendarRowState
                        ) {
                            viewModel.timelineData.forEachIndexed { index, date ->
                                val day = date.first
                                item {
                                    val color by animateColorAsState(
                                        targetValue = if (viewModel.currentDate == day.first) BilibiliPink else Color.Transparent,
                                        animationSpec = tween(durationMillis = 400), label = ""
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
                                        Text(
                                            text = CHINESE_NUMBERS[day.second] ?: "空",
                                            style = AppTheme.typography.h2,
                                            modifier = Modifier
                                                .onSizeChanged {
                                                    textSize =
                                                        with(localDensity) { it.height.toDp() }
                                                }
                                                .width(textSize)
                                                .clip(CircleShape)
                                                .background(color)
                                                .aspectRatio(1f)
                                                .padding(6.dp)
                                                .apply {
                                                    if (index == 6/*同上*/) border(
                                                        width = 0.3.dp,
                                                        color = BilibiliPink
                                                    )
                                                },
                                            textAlign = TextAlign.Center
                                        )
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
                        item {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = entry.key,
                                        style = AppTheme.typography.h3,
                                        color = Color.White
                                    )
                                }
                                for (episode in entry.value) {
                                    val updated =
                                        episode.delay_reason.isEmpty() && System.currentTimeMillis() > episode.pub_ts
                                    SmallBangumiCard(
                                        title = episode.title,
                                        cover = episode.square_cover,
                                        epName = episode.delay_reason.ifEmpty { episode.pub_index },
                                        bangumiId = if (updated) episode.episode_id else episode.season_id,
                                        bangumiIdType = if (updated) BANGUMI_ID_TYPE_EPID else BANGUMI_ID_TYPE_SSID,
                                        modifier = Modifier.alpha(if (episode.delay_reason.isEmpty()) 1f else 0.6f),
                                        onJumpToBangumiDetail = onJumpToBangumiDetail
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}