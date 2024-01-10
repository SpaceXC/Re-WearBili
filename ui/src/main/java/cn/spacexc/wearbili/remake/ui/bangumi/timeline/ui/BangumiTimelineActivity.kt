package cn.spacexc.wearbili.remake.app.bangumi.timeline.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

/**
 * Created by XC-Qan on 2023/8/9.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class BangumiTimelineActivity : ComponentActivity() {
    val viewModel by viewModels<BangumiTimelineViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getBangumiTimeline()
        setContent {
            BangumiTimelineScreen(viewModel = viewModel)
        }
    }
}