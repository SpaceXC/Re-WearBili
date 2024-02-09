package cn.spacexc.wearbili.remake.app.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import cn.spacexc.wearbili.remake.common.ui.theme.WearBiliTheme

class UITest : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearBiliTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    //OffsetBigIcon(imageVector = Icons.Outlined.Policy, offset = Offset(0.2f, 0.3f))
                    AnimationDemo()
                }
            }
        }
    }
}