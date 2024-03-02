package cn.spacexc.wearbili.remake.app.space.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import cn.spacexc.wearbili.remake.app.season.ui.PARAM_MID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserSpaceActivity : ComponentActivity() {
    val viewModel by viewModels<UserSpaceViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mid = intent.getLongExtra(PARAM_MID, 0L)
        viewModel.getUserSpace(mid)
        setContent {
            UserSpaceScreen(viewModel = viewModel, mid = mid)
        }
    }
}