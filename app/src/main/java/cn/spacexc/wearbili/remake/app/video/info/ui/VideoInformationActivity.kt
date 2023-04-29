package cn.spacexc.wearbili.remake.app.video.info.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import cn.spacexc.wearbili.remake.app.video.info.comment.ui.CommentViewModel
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationViewModel
import cn.spacexc.wearbili.remake.common.domain.video.VideoUtils
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
    private val commentViewModel by viewModels<CommentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoIdType = intent.getStringExtra(PARAM_VIDEO_ID_TYPE) ?: VIDEO_TYPE_BVID
        val videoId = intent.getStringExtra(PARAM_VIDEO_ID)
        videoInfoViewModel.getVideoInfo(videoIdType = videoIdType, videoId = videoId)
        val aid = (if (!videoId.isNullOrEmpty()) {
            if (videoIdType == VIDEO_TYPE_AID) videoId.replace("av", "") else VideoUtils.bv2av(
                videoId
            ).replace("av", "")
        } else "0")
        val commentDataFlow = commentViewModel.commentListFlow(aid)
        setContent {
            VideoInformationActivityScreen(
                context = this,
                state = viewModel.state,
                videoInformationScreenState = videoInfoViewModel.state,
                commentViewModel = commentViewModel,
                uploaderMid = videoInfoViewModel.state.videoData?.owner?.mid ?: 0,
                oid = videoInfoViewModel.state.videoData?.aid ?: 0,
                onBack = ::finish,
                commentDataFlow = commentDataFlow
            )
        }
    }
}