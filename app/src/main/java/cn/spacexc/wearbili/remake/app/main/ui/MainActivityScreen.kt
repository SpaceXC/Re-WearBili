package cn.spacexc.wearbili.remake.app.main.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.about.ui.AboutActivity
import cn.spacexc.wearbili.remake.app.bangumi.index.ui.BangumiIndexActivity
import cn.spacexc.wearbili.remake.app.cache.list.CacheListActivity
import cn.spacexc.wearbili.remake.app.main.dynamic.ui.DynamicScreen
import cn.spacexc.wearbili.remake.app.main.dynamic.ui.DynamicViewModel
import cn.spacexc.wearbili.remake.app.main.profile.ui.ProfileScreen
import cn.spacexc.wearbili.remake.app.main.profile.ui.ProfileScreenState
import cn.spacexc.wearbili.remake.app.main.recommend.ui.RecommendScreen
import cn.spacexc.wearbili.remake.app.main.recommend.ui.RecommendViewModel
import cn.spacexc.wearbili.remake.app.search.ui.SearchActivity
import cn.spacexc.wearbili.remake.app.settings.LocalConfiguration
import cn.spacexc.wearbili.remake.app.settings.ui.SettingsActivity
import cn.spacexc.wearbili.remake.app.splash.ui.SplashScreenActivity
import cn.spacexc.wearbili.remake.common.ui.OutlinedRoundButton
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.WearBiliAnimatedContent
import cn.spacexc.wearbili.remake.common.ui.isRound
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/4/6.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

data class MenuItem(
    val text: String,
    val icon: ImageVector? = null,
    @DrawableRes val iconResId: Int = 0,
    val customIcon: Boolean = false,
    val onClick: suspend PagerState.(Context) -> Unit,
)

val menuItems = listOf(
    MenuItem(
        text = "主页",
        icon = Icons.Outlined.Home,
        onClick = { _ ->
            scrollToPage(0)

        }
    ),
    MenuItem(
        text = "动态",
        customIcon = true,
        iconResId = R.drawable.icon_dynamic,
        onClick = { _ ->
            scrollToPage(1)
        }
    ),
    MenuItem(
        text = "我的",
        icon = Icons.Outlined.Person,
        onClick = { _ ->
            scrollToPage(2)
        }
    ),
    MenuItem(
        "搜索",
        icon = Icons.Outlined.Search,
        onClick = { context ->
            context.startActivity(Intent(context, SearchActivity::class.java))
        }
    ),
    MenuItem(
        "番剧",
        icon = Icons.Outlined.Movie,
        onClick = { context ->
            context.startActivity(Intent(context, BangumiIndexActivity::class.java))
        }
    ),
    MenuItem(
        "缓存",
        icon = Icons.Outlined.FileDownload,
        onClick = { context ->
            context.startActivity(Intent(context, CacheListActivity::class.java))
        }
    ),
    MenuItem(
        "设置",
        icon = Icons.Outlined.Settings,
        onClick = { context ->
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }
    ),
    MenuItem(
        "关于",
        icon = Icons.Outlined.Info,
        onClick = { context ->
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    )
)


@Composable
fun MainActivityScreen(
    context: Activity,
    recommendViewModel: RecommendViewModel,
    dynamicViewModel: DynamicViewModel,
    profileScreenState: ProfileScreenState,
    updateInfo: SplashScreenActivity.AppUpdatesResult?,
    onProfileRetry: () -> Unit
) {
    val pagerState = rememberPagerState {
        3
    }
    var isDropdownMenuShowing by remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = pagerState.targetPage, block = {
        pagerState.targetPage.logd("targetPage")
        pagerState.logd("pagerState")
    })
    Box(modifier = Modifier.fillMaxSize()) {
        context.TitleBackground(
            title = if (isDropdownMenuShowing) "菜单" else when (pagerState.currentPage) {
                0 -> "推荐"
                1 -> "动态"
                2 -> "我的"
                else -> ""
            },
            isDropdownTitle = true,
            //isTitleClipToBounds = isBackgroundTitleClipToBounds && !isDropdownMenuShowing,
            isDropdown = !isDropdownMenuShowing,
            isBackgroundShowing = !isDropdownMenuShowing,
            onDropdown = {
                isDropdownMenuShowing = !isDropdownMenuShowing
            },
            onRetry = {}
        ) {
            val recommendSource = LocalConfiguration.current.recommendSource
            LaunchedEffect(key1 = recommendSource, block = {
                recommendViewModel.getRecommendVideos(true, recommendSource)
            })
            WearBiliAnimatedContent(
                targetState = isDropdownMenuShowing,
                transitionSpec = {
                    val tweenFloat = tween<Float>(
                        durationMillis = 400,
                        //easing = CubicBezierEasing(0f, 1f, 0.25f, 1f)
                    )
                    val tweenIntOffset = tween<IntOffset>(
                        durationMillis = 400,
                        //easing = CubicBezierEasing(0f, 1f, 0.25f, 1f)
                    )
                    if (!isDropdownMenuShowing) {
                        (slideInVertically(animationSpec = tweenIntOffset) { height -> height } + fadeIn(
                            animationSpec = tweenFloat
                        )).togetherWith(slideOutVertically(animationSpec = tweenIntOffset) { height -> -height } + fadeOut(
                            animationSpec = tweenFloat
                        ))
                    } else {
                        (slideInVertically(animationSpec = tweenIntOffset) { height -> -height } + fadeIn(
                            animationSpec = tweenFloat
                        )).togetherWith(slideOutVertically(animationSpec = tweenIntOffset) { height -> height } + fadeOut(
                            animationSpec = tweenFloat
                        ))
                    }
                }, label = "mainActivityMenu"
            ) { isDropdownShowing ->
                if (isDropdownShowing) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = if (isRound()) PaddingValues(24.dp) else PaddingValues(16.dp)
                    ) {
                        items(menuItems) { item ->
                            if (item.icon != null) {
                                OutlinedRoundButton(
                                    icon = {
                                        Icon(
                                            imageVector = item.icon,
                                            tint = Color.White,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .fillMaxSize(0.4f)
                                            /*.scale(
                                                if (isRound()) 1.5f else 1f
                                            )*/
                                        )
                                    },
                                    text = item.text,
                                    modifier = Modifier/*.weight(1f)*/,
                                    buttonModifier = Modifier.aspectRatio(1f),
                                    onClick = {
                                        coroutineScope.launch {
                                            item.onClick(pagerState, context)
                                            isDropdownMenuShowing = false
                                        }
                                    }
                                )
                            } else if (item.customIcon) {
                                OutlinedRoundButton(
                                    icon = {
                                        Icon(
                                            painter = painterResource(id = item.iconResId),
                                            tint = Color.White,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .fillMaxSize(0.4f)
                                        )
                                    },
                                    text = item.text,
                                    modifier = Modifier/*.weight(1f)*/,
                                    buttonModifier = Modifier.aspectRatio(1f),
                                    onClick = {
                                        coroutineScope.launch {
                                            item.onClick(pagerState, context)
                                            isDropdownMenuShowing = false
                                        }
                                    }
                                )
                            }
                        }
                    }
                } else {
                    HorizontalPager(
                        //pageCount = 3 /* 推荐页，动态页，个人页 */,
                        modifier = Modifier.fillMaxSize(),
                        state = pagerState
                    ) {
                        when (it) {
                            0 -> RecommendScreen(
                                viewModel = recommendViewModel,
                                context = context,
                                updatesResult = updateInfo,
                                onFetch = { isRefresh ->
                                    recommendViewModel.getRecommendVideos(
                                        isRefresh,
                                        recommendSource
                                    )
                                }
                            )

                            1 -> DynamicScreen(viewModel = dynamicViewModel, context = context)
                            2 -> ProfileScreen(
                                state = profileScreenState,
                                context = context,
                                onRetry = onProfileRetry
                            )
                        }
                    }
                }
            }
        }
    }
}