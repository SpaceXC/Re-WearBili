package cn.spacexc.wearbili.remake.app.message

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cn.spacexc.wearbili.remake.app.message.ui.MessageScreen

class MessageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessageScreen()
        }
    }
}