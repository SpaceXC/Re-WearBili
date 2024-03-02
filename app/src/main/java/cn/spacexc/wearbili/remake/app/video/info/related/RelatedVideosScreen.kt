package cn.spacexc.wearbili.remake.app.video.info.related

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.common.domain.video.toShortChinese
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationViewModel
import cn.spacexc.wearbili.remake.common.ui.LoadableBox
import cn.spacexc.wearbili.remake.common.ui.VideoCard

/**
 * Created by XC-Qan on 2023/7/7.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun RelatedVideosScreen(
    viewModel: VideoInformationViewModel
) {
    LoadableBox(uiState = viewModel.state.uiState, onRetry = {}) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 6.dp, end = 6.dp, bottom = 6.dp, top = 2.dp)
        ) {
            viewModel.state.videoData?.related?.forEach { video ->
                item {
                    VideoCard(
                        videoName = video.title,
                        uploader = video.owner.name,
                        views = video.stat.view.toShortChinese(),
                        coverUrl = video.pic,
                        videoIdType = VIDEO_TYPE_BVID,
                        videoId = video.bvid
                    )
                }
            }
        }
    }
} 