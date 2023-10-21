package cn.spacexc.wearbili.remake.app.bangumi.info.ui

import android.app.Activity
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
import androidx.palette.graphics.Palette
import cn.spacexc.wearbili.remake.app.video.info.comment.ui.CommentScreen
import cn.spacexc.wearbili.remake.app.video.info.comment.ui.CommentViewModel
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.TitleBackground

/**
 * Created by XC-Qan on 2023/7/19.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
        /*@UnstableApi*/
fun Activity.BangumiScreen(
    bangumiInfoViewModel: BangumiViewModel,
    bangumiCommentViewModel: CommentViewModel
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    var currentColor by remember {
        mutableStateOf(BilibiliPink)
    }
    val color by animateColorAsState(
        targetValue = currentColor,
        animationSpec = tween(durationMillis = 1000)
    )
    val ambientAlpha by animateFloatAsState(
        targetValue = if (currentColor == BilibiliPink) 0.6f else 1f,
        animationSpec = tween(durationMillis = 1000)
    )
    LaunchedEffect(key1 = bangumiInfoViewModel.imageBitmap, block = {
        bangumiInfoViewModel.imageBitmap?.let { bitmap ->
            val palette = Palette.from(bitmap).generate()
            val newColor = palette.getDarkVibrantColor(BilibiliPink.value.toInt())
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
        onBack = ::finish
    ) {
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> BangumiInfoScreen(viewModel = bangumiInfoViewModel)
                1 -> CommentScreen(
                    viewModel = bangumiCommentViewModel,
                    commentDataFlow = bangumiCommentViewModel.commentListFlow(bangumiInfoViewModel.getCurrentSelectedEpisode()?.aid?.toString()),
                    oid = bangumiInfoViewModel.getCurrentSelectedEpisode()?.aid ?: 0,
                    uploaderMid = 0L,
                    context = this@BangumiScreen
                )
            }
        }
    }
}