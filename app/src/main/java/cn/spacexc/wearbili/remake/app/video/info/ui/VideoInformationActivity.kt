package cn.spacexc.wearbili.remake.app.video.info.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.media3.common.util.UnstableApi
import cn.spacexc.wearbili.remake.app.video.info.comment.ui.CommentViewModel
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationViewModel
import cn.spacexc.wearbili.remake.app.video.info.related.RelatedVideosViewModel
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
const val PARAM_VIDEO_CID = "videoCid"
const val VIDEO_TYPE_AID = "aid"
const val VIDEO_TYPE_BVID = "bvid"
const val PARAM_WEBI_SIGNATURE_KEY = "webi_signature_key"

@AndroidEntryPoint
class VideoInformationActivity : ComponentActivity() {
    private val viewModel by viewModels<VideoInformationActivityViewModel>()
    private val videoInfoViewModel by viewModels<VideoInformationViewModel>()
    private val commentViewModel by viewModels<CommentViewModel>()
    private val relatedVideoViewModel by viewModels<RelatedVideosViewModel>()

    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoIdType = intent.getStringExtra(PARAM_VIDEO_ID_TYPE) ?: VIDEO_TYPE_BVID
        val videoId = intent.getStringExtra(PARAM_VIDEO_ID)
        if (videoId != null) {
            videoInfoViewModel.getVideoInfo(videoIdType = videoIdType, videoId = videoId)
            //videoInfoViewModel.getVideoSanLianState(videoIdType = videoIdType, videoId = videoId) //FIXME: 这个方法有时候会导致uiState因不明原因改变，过两天修（20230708）
            relatedVideoViewModel.getRelatedVideos(videoIdType = videoIdType, videoId = videoId)
        }
        val aid = (if (!videoId.isNullOrEmpty()) {
            if (videoIdType == VIDEO_TYPE_AID) videoId.replace(
                "av",
                ""
            ) else cn.spacexc.wearbili.common.domain.video.VideoUtils.bv2av(
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
                commentDataFlow = commentDataFlow,
                videoInformationViewModel = videoInfoViewModel,
                relatedVideosViewModel = relatedVideoViewModel
            )
        }
    }
}