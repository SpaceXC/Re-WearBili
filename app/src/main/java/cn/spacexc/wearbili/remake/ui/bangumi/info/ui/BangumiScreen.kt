package cn.spacexc.wearbili.remake.ui.bangumi.info.ui

//import androidx.wear.compose.navigation.composable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.navigation.NavGraphBuilder
import androidx.palette.graphics.Palette
import androidx.wear.compose.navigation.composable
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.ui.video.info.comment.ui.CommentScreen
import cn.spacexc.wearbili.remake.ui.video.info.comment.ui.CommentViewModel

/**
 * Created by XC-Qan on 2023/7/19.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val BangumiScreenRoute = "bangumi_screen"

fun NavGraphBuilder.bangumiScreen(
    onBack: () -> Unit,
    onJumpToVideo: (String, String) -> Unit,
    onJumpToSearch: (String) -> Unit,
    onJumpToImage: (List<String>, Int) -> Unit
) {
    composable("$BangumiScreenRoute/{bangumiIdType}/{bangumiId}") {
        val bangumiInfoViewModel: BangumiViewModel = hiltViewModel()
        val bangumiCommentViewModel: CommentViewModel = hiltViewModel()
        val bangumiId = it.arguments?.getLong("bangumiId") ?: 0L
        val bangumiIdType = it.arguments?.getString("bangumiIdType") ?: BANGUMI_ID_TYPE_EPID
        LaunchedEffect(key1 = Unit, block = {
            if (bangumiId != 0L) {
                if (bangumiIdType == BANGUMI_ID_TYPE_EPID) {
                    bangumiInfoViewModel.currentSelectedEpid = bangumiId
                }
                bangumiInfoViewModel.getBangumiInfo(bangumiIdType, bangumiId)
            }
        })

        BangumiScreen(
            bangumiInfoViewModel = bangumiInfoViewModel,
            bangumiCommentViewModel = bangumiCommentViewModel,
            onBack = onBack,
            onJumpToVideo = onJumpToVideo,
            onJumpToSearch = onJumpToSearch,
            onJumpToImage = onJumpToImage
        )
    }
}

fun NavController.navigateToBangumiDetail(bangumiIdType: String, bangumiId: Long) =
    navigate("$BangumiScreenRoute/$bangumiIdType/$bangumiId")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BangumiScreen(
    bangumiInfoViewModel: BangumiViewModel,
    bangumiCommentViewModel: CommentViewModel,
    onBack: () -> Unit,
    onJumpToVideo: (String, String) -> Unit,
    onJumpToSearch: (String) -> Unit,
    onJumpToImage: (List<String>, Int) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    var currentColor by remember {
        mutableStateOf(BilibiliPink)
    }
    val color by animateColorAsState(
        targetValue = currentColor,
        animationSpec = tween(durationMillis = 1000), label = ""
    )
    val ambientAlpha by animateFloatAsState(
        targetValue = if (currentColor == BilibiliPink) 0.6f else 1f,
        animationSpec = tween(durationMillis = 1000), label = ""
    )
    LaunchedEffect(key1 = bangumiInfoViewModel.imageBitmap, block = {
        bangumiInfoViewModel.imageBitmap?.let { bitmap ->
            val palette = Palette.from(bitmap).generate()
            currentColor = Color(palette.getLightMutedColor(BilibiliPink.value.toInt()))
        }
    })
    TitleBackground(
        title = when (pagerState.currentPage) {
            0 -> "剧集详情"
            1 -> "单集评论"
            else -> ""
        },
        themeColor = color,
        ambientAlpha = ambientAlpha,
        onBack = onBack
    ) {
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> BangumiInfoScreen(viewModel = bangumiInfoViewModel)
                1 -> CommentScreen(
                    viewModel = bangumiCommentViewModel,
                    commentDataFlow = bangumiCommentViewModel.commentListFlow(bangumiInfoViewModel.getCurrentSelectedEpisode()?.aid?.toString()),
                    oid = bangumiInfoViewModel.getCurrentSelectedEpisode()?.aid ?: 0,
                    uploaderMid = 0L,
                    onJumpToImage = onJumpToImage,
                    onJumpToSearch = onJumpToSearch,
                    onJumpToVideo = onJumpToVideo
                )
            }
        }
    }
}