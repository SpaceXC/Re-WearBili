package cn.spacexc.wearbili.remake.app.video.info.info.ui.v2

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.IjkVideoPlayerViewModel
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationViewModel
import cn.spacexc.wearbili.remake.app.video.info.info.ui.v2.basic.DetailedVideoInformation
import cn.spacexc.wearbili.remake.app.video.info.info.ui.v2.basic.SimpleVideoInformation

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.BasicVideoInformation(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: VideoInformationViewModel,
    videoPlayerViewModel: IjkVideoPlayerViewModel
) {
    var isDetail by remember {
        mutableStateOf(false)
    }
    var infoButtonSize by remember {
        mutableStateOf(DpSize(0.dp, 0.dp))
    }
    SharedTransitionLayout {
        AnimatedContent(targetState = isDetail, label = "") { isDetailShowing ->
            if (isDetailShowing) {
                DetailedVideoInformation(
                    navController = navController,
                    viewModel = viewModel,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this,
                    infoButtonSize = infoButtonSize
                ) {
                    isDetail = false
                }
            } else {
                SimpleVideoInformation(
                    viewModel = viewModel,
                    navController = navController,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this,
                    globalSharedTransitionScope = this@BasicVideoInformation,
                    globalAnimatedVisibilityScope = animatedVisibilityScope,
                    videoPlayerViewModel = videoPlayerViewModel
                ) {
                    infoButtonSize = it
                    isDetail = true
                }
            }
        }
    }
}