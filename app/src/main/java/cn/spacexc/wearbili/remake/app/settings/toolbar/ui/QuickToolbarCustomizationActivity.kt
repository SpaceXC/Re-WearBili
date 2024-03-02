package cn.spacexc.wearbili.remake.app.settings.toolbar.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import cn.spacexc.wearbili.remake.app.settings.SettingsManager
import cn.spacexc.wearbili.remake.proto.settings.copy
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/7/11.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class QuickToolbarCustomizationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            SettingsManager.updateConfiguration {
                copy {
                    haveToolBarTipDisplayed = true
                }
            }
        }
        setContent { QuickToolbarCustomizationScreen() }
    }
}