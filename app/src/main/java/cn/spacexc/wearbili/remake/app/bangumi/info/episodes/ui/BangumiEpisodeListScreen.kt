package cn.spacexc.wearbili.remake.app.bangumi.info.episodes.ui

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cn.spacexc.wearbili.remake.app.bangumi.info.episodes.BangumiEpisodesViewModel
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.BANGUMI_ID_TYPE_EPID
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.ui.SmallBangumiCard
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding

@kotlinx.serialization.Serializable
data class BangumiEpisodeListScreen(val bangumiIdType: String, val bangumiId: Long, val index: Int)

@Composable
fun BangumiEpisodeListScreen(
    navController: NavController,
    bangumiIdType: String,
    bangumiId: Long,
    viewModel: BangumiEpisodesViewModel = viewModel(),
    screenIndex: Int
) {
    LaunchedEffect(key1 = Unit) {
        if (viewModel.uiState != UIState.Success)
            viewModel.getBangumiInfo(bangumiIdType, bangumiId)
    }
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
    }, onBack = navController::navigateUp, uiState = viewModel.uiState) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
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
                        navController = navController,
                        onClick = {
                            /*setResult(0, Intent().apply {
                                putExtra(PARAM_BANGUMI_ID, episode.ep_id)
                            })
                            finish()*/
                        }
                    )
                }
            }
        }
    }
}