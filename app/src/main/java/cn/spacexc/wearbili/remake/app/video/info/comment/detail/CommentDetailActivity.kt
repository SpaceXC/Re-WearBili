package cn.spacexc.wearbili.remake.app.video.info.comment.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import cn.spacexc.wearbili.remake.app.season.ui.PARAM_MID
import cn.spacexc.wearbili.remake.app.video.action.favourite.ui.PARAM_VIDEO_AID
import cn.spacexc.wearbili.remake.app.video.info.comment.detail.ui.CommentRepliesDetailScreen
import cn.spacexc.wearbili.remake.app.video.info.comment.detail.ui.CommentRepliesDetailViewModel

const val PARAM_ROOT_COMMENT_RPID = "rootRpid"

class CommentDetailActivity : ComponentActivity() {
    val viewModel: CommentRepliesDetailViewModel by viewModels<CommentRepliesDetailViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootRpid = intent.getLongExtra(PARAM_ROOT_COMMENT_RPID, 0L)
        val videoAid = intent.getLongExtra(PARAM_VIDEO_AID, 0L)
        val uploaderMid = intent.getLongExtra(PARAM_MID, 0L)
        viewModel.initPaging(rootRpid, videoAid)
        setContent {
            CommentRepliesDetailScreen(viewModel = viewModel, uploaderMid = uploaderMid)
        }
    }
}