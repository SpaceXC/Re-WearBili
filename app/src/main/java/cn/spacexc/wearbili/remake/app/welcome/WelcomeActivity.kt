package cn.spacexc.wearbili.remake.app.welcome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cn.spacexc.wearbili.remake.app.settings.ProvideConfiguration

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvideConfiguration {
                WelcomeScreen()
            }
        }
    }
}