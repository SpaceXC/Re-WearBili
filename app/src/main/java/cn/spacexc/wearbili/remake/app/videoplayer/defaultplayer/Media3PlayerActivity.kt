package cn.spacexc.wearbili.remake.app.videoplayer.defaultplayer

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.media3.common.util.UnstableApi
import cn.spacexc.wearbili.common.domain.log.logd

/**
 * Created by XC-Qan on 2023/5/20.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val PARAM_VIDEO_ID_TYPE = "videoIdType"
const val PARAM_VIDEO_ID = "videoId"
const val PARAM_VIDEO_CID = "videoCid"
const val PARAM_WEBI_SIGNATURE_KEY = "webi_signature_key"
const val VIDEO_TYPE_AID = "aid"
const val VIDEO_TYPE_BVID = "bvid"

@UnstableApi
class Media3PlayerActivity : ComponentActivity() {
    private val viewModel by viewModels<Media3PlayerViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application.logd("videoPlayerApplicationContext")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        val videoIdType = intent.getStringExtra(PARAM_VIDEO_ID_TYPE) ?: VIDEO_TYPE_BVID
        val videoId = intent.getStringExtra(PARAM_VIDEO_ID)
        val videoCid = intent.getLongExtra(PARAM_VIDEO_CID, 0L)

        viewModel.playVideoFromId(videoIdType, videoId!!, videoCid)

        setContent {
            Media3PlayerScreen(
                viewModel = viewModel,
                displaySurface = VideoDisplaySurface.TEXTURE_VIEW,
                onBack = ::finish
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.player.release()
    }
}