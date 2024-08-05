package cn.spacexc.wearbili.remake.app.message.direct.history.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

const val PARAM_TALKER_MID = "talkerMid"
const val PARAM_TALKER_NAME = "talkerName"

class DirectMessageActivity : ComponentActivity() {
    val viewModel by viewModels<DirectMessageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val talkerMid = intent.getLongExtra(PARAM_TALKER_MID, 0)
        val talkerName = intent.getStringExtra(PARAM_TALKER_NAME) ?: ""
        viewModel.getMessages(talkerMid)
        setContent {
            DirectMessageScreen(talkerName = talkerName, talkerMid = talkerMid, viewModel = viewModel)
        }
    }
}