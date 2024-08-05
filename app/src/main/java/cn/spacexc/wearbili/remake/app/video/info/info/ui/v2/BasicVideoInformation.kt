package cn.spacexc.wearbili.remake.app.video.info.info.ui.v2

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationViewModel
import cn.spacexc.wearbili.remake.app.video.info.info.ui.v2.basic.DetailedVideoInformation
import cn.spacexc.wearbili.remake.app.video.info.info.ui.v2.basic.SimpleVideoInformation

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Activity.BasicVideoInformation(
    modifier: Modifier = Modifier,
    isDetail: Boolean = false,
    viewModel: VideoInformationViewModel,
    onGoToDetail: () -> Unit
) {
    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(targetState = isDetail, label = "") { isDetailShowing ->
            if (isDetailShowing) {
                DetailedVideoInformation(context = this@BasicVideoInformation, viewModel = viewModel, sharedTransitionScope = this@SharedTransitionLayout, animatedContentScope = this)
            } else {
                SimpleVideoInformation(viewModel = viewModel, context = this@BasicVideoInformation, sharedTransitionScope = this@SharedTransitionLayout, animatedContentScope = this) {
                    onGoToDetail()
                }
            }
        }
    }
}