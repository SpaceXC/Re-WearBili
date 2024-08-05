package cn.spacexc.wearbili.remake.app.message

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import cn.spacexc.wearbili.remake.app.message.direct.sessions.DirectMessagesListViewModel
import cn.spacexc.wearbili.remake.app.message.ui.MessageScreen

class MessageActivity : ComponentActivity() {
    val viewModel by viewModels<DirectMessagesListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessageScreen(viewModel)
        }
    }
}