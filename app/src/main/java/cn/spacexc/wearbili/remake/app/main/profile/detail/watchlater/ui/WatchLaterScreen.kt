package cn.spacexc.wearbili.remake.app.main.profile.detail.watchlater.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.common.domain.time.secondToTime
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.VideoCard

/**
 * Created by XC-Qan on 2023/6/24.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */
@Composable
fun WatchLaterScreen(
    viewModel: WatchLaterViewModel,
    onBack: () -> Unit
) {
    TitleBackground(title = "稍后再看", onBack = onBack, uiState = viewModel.uiState) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(4.dp)) {
            viewModel.watchLaterList.forEach { item ->
                item {
                    VideoCard(
                        videoName = item.title,
                        uploader = item.owner.name,
                        views = item.duration.secondToTime(),
                        coverUrl = item.pic,
                        videoIdType = VIDEO_TYPE_BVID,
                        videoId = item.bvid,
                        badge = if (item.viewed) "已看完" else null
                    )
                }
            }
        }
    }
}