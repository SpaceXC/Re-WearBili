package cn.spacexc.wearbili.remake.app.message.at.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

class AtMessageActivity : ComponentActivity() {
    val viewModel by viewModels<AtMessageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { 
            AtMessageScreen(viewModel = viewModel)
        }
    }
}