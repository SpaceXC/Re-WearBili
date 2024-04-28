package cn.spacexc.wearbili.remake.app.message.system

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import cn.spacexc.wearbili.remake.app.message.system.ui.SystemNotificationScreen
import cn.spacexc.wearbili.remake.app.message.system.ui.SystemNotificationViewModel

class SystemMessageActivity : ComponentActivity() {
    val viewModel by viewModels<SystemNotificationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SystemNotificationScreen(viewModel = viewModel)
        }
    }
}