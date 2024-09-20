package cn.spacexc.wearbili.remake.app.bangumi.info.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.palette.graphics.Palette
import cn.spacexc.wearbili.remake.app.settings.ProvideConfiguration
import cn.spacexc.wearbili.remake.app.video.info.comment.ui.CommentScreen
import cn.spacexc.wearbili.remake.app.video.info.comment.ui.CommentViewModel
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateColorAsState
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateFloatAsState

/**
 * Created by XC-Qan on 2023/7/19.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@kotlinx.serialization.Serializable
data class BangumiScreen(
    val bangumiIdType: String,
    val bangumiId: Long
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.BangumiScreen(
    bangumiInfoViewModel: BangumiViewModel = hiltViewModel(),
    bangumiCommentViewModel: CommentViewModel = hiltViewModel(),
    bangumiIdType: String,
    bangumiId: Long,
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    ProvideConfiguration {
        val pagerState = rememberPagerState(pageCount = { 2 })
        var currentColor by remember {
            mutableStateOf(BilibiliPink)
        }
        val color by wearBiliAnimateColorAsState(
            targetValue = currentColor,
            animationSpec = tween(durationMillis = 1000)
        )
        val ambientAlpha by wearBiliAnimateFloatAsState(
            targetValue = if (currentColor == BilibiliPink) 0.6f else 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        LaunchedEffect(key1 = Unit) {
            if (bangumiId != 0L) {
                if (bangumiIdType == BANGUMI_ID_TYPE_EPID) {
                    bangumiInfoViewModel.currentSelectedEpid = bangumiId
                }
                if (bangumiInfoViewModel.uiState != UIState.Success)
                    bangumiInfoViewModel.getBangumiInfo(bangumiIdType, bangumiId)
            }
        }
        LaunchedEffect(key1 = bangumiInfoViewModel.imageBitmap, block = {
            bangumiInfoViewModel.imageBitmap?.let { bitmap ->
                val palette = Palette.from(bitmap).generate()
                currentColor = Color(palette.getLightMutedColor(BilibiliPink.value.toInt()))
            }
        })
        TitleBackground(
            navController = navController,
            title = when (pagerState.currentPage) {
                0 -> "剧集详情"
                1 -> "单集评论"
                else -> ""
            },
            themeColor = color,
            ambientAlpha = ambientAlpha,
            onBack = navController::navigateUp,
            onRetry = {}
        ) {
            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> BangumiInfoScreen(
                        viewModel = bangumiInfoViewModel,
                        bangumiIdType = bangumiIdType,
                        bangumiId = bangumiId,
                        navController = navController
                    )

                    1 -> CommentScreen(
                        viewModel = bangumiCommentViewModel,
                        commentsData = bangumiCommentViewModel.commentListFlow(bangumiInfoViewModel.getCurrentSelectedEpisode()?.aid?.toString())
                            ?.collectAsLazyPagingItems(),
                        oid = bangumiInfoViewModel.getCurrentSelectedEpisode()?.aid ?: 0,
                        uploaderMid = 0L,
                        navController = navController,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }
            }
        }
    }
}