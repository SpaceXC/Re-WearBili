package cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.app.cache.domain.database.VideoCacheRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

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
const val PARAM_IS_CACHE = "isCache"
const val VIDEO_TYPE_BVID = "bvid"
const val PARAM_IS_BANGUMI = "isBangumi"

/*@UnstableApi*/
@AndroidEntryPoint
class Media3PlayerActivity : ComponentActivity() {
    private val viewModel by viewModels<Media3VideoPlayerViewModel>()

    @Inject
    lateinit var repository: VideoCacheRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application.logd("videoPlayerApplicationContext")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        val isCache = intent.getBooleanExtra(PARAM_IS_CACHE, false)

        val videoIdType = intent.getStringExtra(PARAM_VIDEO_ID_TYPE) ?: VIDEO_TYPE_BVID
        val videoId = intent.getStringExtra(PARAM_VIDEO_ID)
        val videoCid = intent.getLongExtra(PARAM_VIDEO_CID, 0L)
        val isBangumi = intent.getBooleanExtra(PARAM_IS_BANGUMI, false)

        if (isCache) {
            lifecycleScope.launch {
                repository.getTaskInfoByVideoCid(videoCid)?.let {
                    viewModel.cacheVideoInfo = it
                }
            }
            viewModel.playVideoFromLocalFile(videoCid)
        } else {
            viewModel.playVideoFromId(videoIdType, videoId!!, videoCid, isBangumi)
        }

        setContent {
            Media3PlayerScreen(
                viewModel = viewModel,
                displaySurface = VideoDisplaySurface.TEXTURE_VIEW,
                onBack = ::finish,
                context = this,
                isCacheVideo = isCache,
                isBangumi = isBangumi
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.httpPlayer.release()
        viewModel.cachePlayer.release()
    }
}