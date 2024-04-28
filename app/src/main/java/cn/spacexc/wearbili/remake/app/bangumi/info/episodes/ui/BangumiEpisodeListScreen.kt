package cn.spacexc.wearbili.remake.app.bangumi.info.episodes.ui

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.app.bangumi.info.episodes.BangumiEpisodesViewModel
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.BANGUMI_ID_TYPE_EPID
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.PARAM_BANGUMI_ID
import cn.spacexc.wearbili.remake.common.ui.SmallBangumiCard
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding


@Composable
fun Activity.BangumiEpisodeListScreen(
    bangumiIdType: String,
    bangumiId: Long,
    viewModel: BangumiEpisodesViewModel,
    screenIndex: Int
) {
    val pagerState = rememberPagerState {
        viewModel.bangumiInfo.size
    }
    val currentTitle by remember {
        derivedStateOf {
            if (viewModel.bangumiInfo.isEmpty()) "" else viewModel.bangumiInfo[pagerState.currentPage].first
        }
    }
    LaunchedEffect(key1 = Unit) {
        pagerState.scrollToPage(screenIndex)
    }
    TitleBackground(title = currentTitle, onRetry = {
        viewModel.getBangumiInfo(bangumiIdType, bangumiId)
    }, onBack = ::finish, uiState = viewModel.uiState) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            /*viewModel.bangumiInfo[page].second.forEachIndexed { index, episode ->
                LazyColumn(
                    contentPadding = PaddingValues(
                        horizontal = TitleBackgroundHorizontalPadding(),
                        vertical = 8.dp
                    )
                ) {
                    item(key = episode.ep_id) {
                        SmallBangumiCard(
                            title = episode.long_title.ifEmpty { episode.title },
                            cover = episode.cover,
                            epName = "EP${index.plus(1)}",
                            bangumiId = episode.ep_id,
                            bangumiIdType = BANGUMI_ID_TYPE_EPID,
                            onClick = {
                                setResult(0, Intent().apply {
                                    putExtra(PARAM_BANGUMI_ID, episode.ep_id)
                                })
                                finish()
                            }
                        )
                    }
                }
            }*/
            val (_, episodes) = viewModel.bangumiInfo[page]
            LazyColumn(
                contentPadding = PaddingValues(
                    horizontal = titleBackgroundHorizontalPadding(),
                    vertical = 8.dp
                ), modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(episodes, key = { _, episode -> episode.ep_id }) { index, episode ->
                    SmallBangumiCard(
                        title = episode.long_title.ifEmpty { episode.title },
                        cover = episode.cover,
                        epName = "EP${index.plus(1)}",
                        bangumiId = episode.ep_id,
                        bangumiIdType = BANGUMI_ID_TYPE_EPID,
                        onClick = {
                            setResult(0, Intent().apply {
                                putExtra(PARAM_BANGUMI_ID, episode.ep_id)
                            })
                            finish()
                        }
                    )
                }
            }
        }
    }
}