package cn.spacexc.wearbili.remake.app.feedback.ui.issues

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllIssuesActivity : ComponentActivity() {
    val viewModel by viewModels<AllIssuesViewModel>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { 
            AllIssuesScreen(viewModel = viewModel)
        }
    }
}