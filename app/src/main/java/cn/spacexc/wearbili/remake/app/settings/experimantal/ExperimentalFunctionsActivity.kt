package cn.spacexc.wearbili.remake.app.settings.experimantal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class ExperimentalFunctionsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExperimentalFunctionsScreen()
        }
    }
}