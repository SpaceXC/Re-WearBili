package cn.spacexc.wearbili.remake.app.image

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.spacexc.wearbili.remake.common.ui.ArrowTitleBackgroundWithCustomBackground
import cn.spacexc.wearbili.remake.common.ui.BiliImage
import cn.spacexc.wearbili.remake.common.ui.WearBiliAnimatedContent
import cn.spacexc.wearbili.remake.common.ui.WearBiliAnimatedVisibility
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import kotlinx.coroutines.delay
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

/**
 * Created by XC-Qan on 2023/11/19.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@kotlinx.serialization.Serializable
data class ImageViewerScreen(
    val images: List<String>,
    val selectedIndex: Int = 0
)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ImageViewerScreen(
    navController: NavController,
    images: List<String>,
    selectedIndex: Int,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    ArrowTitleBackgroundWithCustomBackground(background = {
        val pagerState = rememberPagerState {
            images.size
        }
        var isPageIndicatorVisible by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(key1 = Unit, block = {
            pagerState.scrollToPage(selectedIndex)
        })
        LaunchedEffect(key1 = pagerState.currentPage) {
            isPageIndicatorVisible = true
            delay(1500)
            isPageIndicatorVisible = false
        }
        Box {
            HorizontalPager(state = pagerState) { page ->
                BiliImage(
                    url = images[page], contentDescription = null, modifier = Modifier
                        .sharedElement(
                            state = rememberSharedContentState(key = "image$page"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .fillMaxSize()
                        .zoomable(
                            rememberZoomState()
                        )
                )
            }
            WearBiliAnimatedVisibility(
                visible = isPageIndicatorVisible,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
                modifier = Modifier
                    .padding(bottom = 26.dp)
                    .align(Alignment.BottomCenter)) {
                Box(
                    modifier = Modifier
                        .background(Color(26, 26, 26, 255), CircleShape)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                        WearBiliAnimatedContent(
                            targetState = pagerState.currentPage,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    slideInVertically { height -> height } + fadeIn() togetherWith
                                            slideOutVertically { height -> -height } + fadeOut()
                                } else {
                                    slideInVertically { height -> -height } + fadeIn() togetherWith
                                            slideOutVertically { height -> height } + fadeOut()
                                }.using(SizeTransform(clip = false))
                            }
                        ) { targetCount ->
                            Text(
                                text = "${targetCount + 1}",
                                fontFamily = wearbiliFontFamily,
                                fontSize = 11.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Text(
                            text = "/${images.size}",
                            fontFamily = wearbiliFontFamily,
                            fontSize = 11.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }, onBack = navController::navigateUp) {

    }
}