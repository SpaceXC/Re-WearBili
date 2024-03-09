package cn.spacexc.wearbili.remake.app.video.info.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.remember
import cn.spacexc.wearbili.remake.app.video.info.comment.ui.CommentViewModel
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
const val PARAM_VIDEO_CID = "videoCid"
const val VIDEO_TYPE_AID = "aid"
const val VIDEO_TYPE_BVID = "bvid"
const val PARAM_WEBI_SIGNATURE_KEY = "webi_signature_key"

@AndroidEntryPoint
class VideoInformationActivity : ComponentActivity() {
    private val videoInfoViewModel by viewModels<VideoInformationViewModel>()
    private val commentViewModel by viewModels<CommentViewModel>()

    /*@UnstableApi*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoIdType = intent.getStringExtra(PARAM_VIDEO_ID_TYPE) ?: VIDEO_TYPE_BVID
        val videoId = intent.getStringExtra(PARAM_VIDEO_ID)
        if (videoId != null) {
            videoInfoViewModel.getVideoInfo(videoIdType = videoIdType, videoId = videoId, activity = this)
        }
        setContent {
            val commentDataFlow = remember(key1 = videoInfoViewModel.state.videoData?.view?.aid) {
                commentViewModel.commentListFlow(
                    videoInfoViewModel.state.videoData?.view?.aid?.toString() ?: ""
                )
            }
            val pagerState = rememberPagerState {
                3
            }

            VideoInformationActivityScreen(
                context = this,
                state = pagerState,
                videoInformationScreenState = videoInfoViewModel.state,
                commentViewModel = commentViewModel,
                uploaderMid = videoInfoViewModel.state.videoData?.view?.owner?.mid ?: 0,
                oid = videoInfoViewModel.state.videoData?.view?.aid ?: 0,
                onBack = ::finish,
                commentDataFlow = commentDataFlow,
                videoInformationViewModel = videoInfoViewModel,
                videoIdType = videoIdType,
                videoId = videoId ?: ""
            )
        }
    }
}