package cn.spacexc.wearbili.remake.app.video.info.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by XC-Qan on 2023/4/12.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val PARAM_VIDEO_ID_TYPE = "videoIdType"
const val PARAM_VIDEO_ID = "videoId"
const val VIDEO_TYPE_AID = "aid"
const val VIDEO_TYPE_BVID = "bvid"

@AndroidEntryPoint
class VideoInformationActivity : ComponentActivity() {
    private val viewModel by viewModels<VideoInformationActivityViewModel>()
    private val videoInfoViewModel by viewModels<VideoInformationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoIdType = intent.getStringExtra(PARAM_VIDEO_ID_TYPE) ?: VIDEO_TYPE_BVID
        val videoId = intent.getStringExtra(PARAM_VIDEO_ID)
        videoInfoViewModel.getVideoInfo(videoIdType = videoIdType, videoId = videoId)
        setContent {
            VideoInformationActivityScreen(
                context = this,
                state = viewModel.state,
                videoInformationScreenState = videoInfoViewModel.state,
                onBack = ::finish
            )
        }
    }
}