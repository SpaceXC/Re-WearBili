package cn.spacexc.wearbili.remake.app.main.ui

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ModeFanOff
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.app.about.ui.AboutActivity
import cn.spacexc.wearbili.remake.app.main.profile.ui.ProfileScreen
import cn.spacexc.wearbili.remake.app.main.profile.ui.ProfileScreenState
import cn.spacexc.wearbili.remake.app.main.recommend.ui.RecommendScreen
import cn.spacexc.wearbili.remake.app.main.recommend.ui.RecommendScreenState
import cn.spacexc.wearbili.remake.common.domain.color.parseColor
import cn.spacexc.wearbili.remake.common.domain.log.logd
import cn.spacexc.wearbili.remake.common.ui.LargeRoundButton
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/4/6.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

data class MenuItem @OptIn(ExperimentalFoundationApi::class) constructor(
    val text: String,
    val icon: ImageVector,
    val onClick: PagerState.(CoroutineScope, Context) -> Unit,
)

@OptIn(ExperimentalFoundationApi::class)
val menuItems = listOf(
    MenuItem(
        text = "主页",
        icon = Icons.Outlined.Home,
        onClick = { scope, _ ->
            scope.launch {
                animateScrollToPage(0)
            }
        }
    ),
    MenuItem(
        text = "动态",
        icon = Icons.Outlined.ModeFanOff,
        onClick = { scope, _ ->
            scope.launch {
                animateScrollToPage(1)
            }
        }
    ),
    MenuItem(
        text = "我的",
        icon = Icons.Outlined.Person,
        onClick = { scope, _ ->
            scope.launch {
                animateScrollToPage(2)
            }
        }
    ),
    MenuItem(
        "设置",
        icon = Icons.Outlined.Settings,
        onClick = { _, context ->

        }
    ),
    MenuItem(
        "关于",
        icon = Icons.Outlined.Info,
        onClick = { _, context ->
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    )
)


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
    var isDropdownMenuShowing by remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    val backgroundColor by animateColorAsState(targetValue = if (isDropdownMenuShowing) parseColor("#141414") else Color.Black)
    LaunchedEffect(key1 = pagerState.targetPage, block = {
        pagerState.targetPage.logd("targetPage")
        pagerState.logd("pagerState")
    })
    Box(modifier = Modifier.fillMaxSize()) {
        TitleBackground(
            title = if (isDropdownMenuShowing) "菜单" else when (pagerState.currentPage) {
                0 -> "推荐"
                1 -> "动态"
                2 -> "我的"
                else -> ""
            },
            isDropdownTitle = true,
            isTitleClipToBounds = isBackgroundTitleClipToBounds && !isDropdownMenuShowing,
            backgroundColor = backgroundColor,
            isDropdown = !isDropdownMenuShowing,
            hasSpaceForTitleBar = true,
            isBackgroundShowing = !isDropdownMenuShowing,
            onDropdown = {
                isDropdownMenuShowing = !isDropdownMenuShowing
            }
        ) {
            AnimatedContent(
                targetState = isDropdownMenuShowing,
                transitionSpec = {
                    val tweenFloat = tween<Float>(
                        durationMillis = 500,
                        easing = CubicBezierEasing(0f, 1f, 0.25f, 1f)
                    )
                    val tweenIntOffset = tween<IntOffset>(
                        durationMillis = 500,
                        easing = CubicBezierEasing(0f, 1f, 0.25f, 1f)
                    )
                    if (!isDropdownMenuShowing) {
                        slideInVertically(animationSpec = tweenIntOffset) { height -> height } + fadeIn(
                            animationSpec = tweenFloat
                        ) with
                                slideOutVertically(animationSpec = tweenIntOffset) { height -> -height } + fadeOut(
                            animationSpec = tweenFloat
                        )
                    } else {
                        slideInVertically(animationSpec = tweenIntOffset) { height -> -height } + fadeIn(
                            animationSpec = tweenFloat
                        ) with
                                slideOutVertically(animationSpec = tweenIntOffset) { height -> height } + fadeOut(
                            animationSpec = tweenFloat
                        )
                    }/*.using(
                        SizeTransform(clip = false)
                    )*/
                }
            ) { isDropdownShowing ->
                if (isDropdownShowing) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(menuItems) { item ->
                            LargeRoundButton(
                                icon = item.icon,
                                text = item.text,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                isDropdownMenuShowing = false
                                item.onClick(pagerState, coroutineScope, context)
                            }
                        }
                    }
                } else {
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
        }
    }
}