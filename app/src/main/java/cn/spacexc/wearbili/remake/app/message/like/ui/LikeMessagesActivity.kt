package cn.spacexc.wearbili.remake.app.message.like.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

class LikeMessagesActivity : ComponentActivity() {
    val viewModel by viewModels<LikeMessagesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { 
            LikeMessagesScreen(viewModel = viewModel)
        }
    }
}