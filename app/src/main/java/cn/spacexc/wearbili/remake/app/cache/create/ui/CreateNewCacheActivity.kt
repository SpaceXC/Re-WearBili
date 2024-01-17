package cn.spacexc.wearbili.remake.app.cache.create.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by XC-Qan on 2023/9/6.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */
const val PARAM_VIDEO_BVID = "videoBvid"
const val PARAM_VIDEO_COVER_URL = "videoCoverUrl"

@AndroidEntryPoint
class CreateNewCacheActivity : ComponentActivity() {
    val viewModel by viewModels<CreateNewCacheViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bvid = intent.getStringExtra(PARAM_VIDEO_BVID) ?: ""
        viewModel.getVideoParts(videoBvid = bvid)
        setContent {
            CreateNewCacheScreen(viewModel = viewModel)
        }
    }
}