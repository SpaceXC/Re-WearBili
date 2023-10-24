package cn.spacexc.wearbili.remake.app.player.videoplayer.mirroring.dlna.controller.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.PARAM_IS_BANGUMI
import cn.spacexc.wearbili.remake.app.player.videoplayer.mirroring.dlna.PARAM_DLNA_VIDEO_NAME
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_CID

/**
 * Created by XC-Qan on 2023/7/18.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class DlnaControllerActivity : ComponentActivity() {
    private val viewModel by viewModels<DlnaControllerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoName = intent.getStringExtra(PARAM_DLNA_VIDEO_NAME) ?: ""
        val cid = intent.getLongExtra(PARAM_VIDEO_CID, 0L)
        val isBangumi = intent.getBooleanExtra(PARAM_IS_BANGUMI, false)
        cid.logd("cid2")
        viewModel.castVideo(cid, videoName, isBangumi)
        setContent {
            DlnaControllerScreen(videoName, viewModel)
        }
    }
}