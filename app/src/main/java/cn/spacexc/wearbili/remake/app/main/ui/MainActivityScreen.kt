package cn.spacexc.wearbili.remake.app.main.ui

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cn.spacexc.wearbili.remake.app.main.profile.ui.ProfileScreen
import cn.spacexc.wearbili.remake.app.main.profile.ui.ProfileScreenState
import cn.spacexc.wearbili.remake.app.main.recommend.ui.RecommendScreen
import cn.spacexc.wearbili.remake.app.main.recommend.ui.RecommendScreenState
import cn.spacexc.wearbili.remake.common.ui.TitleBackground

/**
 * Created by XC-Qan on 2023/4/6.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainActivityScreen(
    context: Context,
    pagerState: PagerState,
    recommendScreenState: RecommendScreenState,
    onRecommendRefresh: (isRefresh: Boolean) -> Unit,
    profileScreenState: ProfileScreenState
) {
    val isBackgroundTitleClipToBounds = pagerState.currentPage != 2
    TitleBackground(
        title = when (pagerState.currentPage) {
            0 -> "推荐"
            1 -> "动态"
            2 -> "我的"
            else -> ""
        }, isDropdownTitle = true, isTitleClipToBounds = isBackgroundTitleClipToBounds
    ) {
        HorizontalPager(
            pageCount = 3 /* 推荐页，动态页，个人页 */,
            modifier = Modifier.fillMaxSize(),
            state = pagerState
        ) {
            when (it) {
                0 -> RecommendScreen(
                    state = recommendScreenState,
                    context = context,
                    onFetch = onRecommendRefresh
                )

                1 -> Text(text = "动态", modifier = Modifier.fillMaxSize())
                2 -> ProfileScreen(
                    state = profileScreenState,
                    isAvatarBackgroundVisible = !isBackgroundTitleClipToBounds
                )
            }
        }
    }
}