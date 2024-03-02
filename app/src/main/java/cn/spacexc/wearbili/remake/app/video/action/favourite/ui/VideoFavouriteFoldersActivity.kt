package cn.spacexc.wearbili.remake.app.video.action.favourite.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

/**
 * Created by XC-Qan on 2023/8/17.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val PARAM_VIDEO_AID = "videoAid"

class VideoFavouriteFoldersActivity : ComponentActivity() {
    val viewModel by viewModels<VideoFavouriteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoAid = intent.getLongExtra(PARAM_VIDEO_AID, 0L)
        viewModel.getFolders(videoAid = videoAid)
        setContent {
            VideoFavouriteFoldersScreen(viewModel = viewModel, videoAid = videoAid)
        }
    }
}