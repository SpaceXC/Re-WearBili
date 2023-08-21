package cn.spacexc.wearbili.remake.app.settings.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatShapes
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cn.spacexc.wearbili.remake.app.settings.domain.SettingsItem

/**
 * Created by XC-Qan on 2023/8/7.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

val SETTINGS_ITEMS = listOf(
    SettingsItem(
        name = "字体大小",
        description = "设置应用的字体大小",
        icon = {
            Icon(
                imageVector = Icons.Outlined.FormatShapes,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.fillMaxSize()
            )
        }
    )
)

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingsActivityScreen(items = SETTINGS_ITEMS)
        }
    }
}