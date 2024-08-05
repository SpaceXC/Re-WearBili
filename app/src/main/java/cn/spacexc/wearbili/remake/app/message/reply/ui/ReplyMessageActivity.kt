package cn.spacexc.wearbili.remake.app.message.reply.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

class ReplyMessageActivity : ComponentActivity() {
    val viewModel by viewModels<ReplyMessageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { 
            ReplyMessageScreen(viewModel = viewModel)
        }
    }
}