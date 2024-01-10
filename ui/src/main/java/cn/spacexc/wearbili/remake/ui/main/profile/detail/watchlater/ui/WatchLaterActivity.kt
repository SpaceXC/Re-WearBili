package cn.spacexc.wearbili.remake.app.main.profile.detail.watchlater.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

/**
 * Created by XC-Qan on 2023/6/24.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class WatchLaterActivity : ComponentActivity() {
    private val viewModel by viewModels<WatchLaterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getWatchLaterItems()
        setContent {
            WatchLaterScreen(viewModel = viewModel, onBack = ::finish)
        }
    }
}