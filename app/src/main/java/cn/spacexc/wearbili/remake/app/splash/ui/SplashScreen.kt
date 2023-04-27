package cn.spacexc.wearbili.remake.app.splash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.common.ui.CirclesBackground

/**
 * Created by XC-Qan on 2023/3/21.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun SplashScreen() {
    CirclesBackground(modifier = Modifier) {
        Image(
            painter = painterResource(id = R.drawable.icon_app_main_reverse),
            contentDescription = null,
            modifier = Modifier
                .align(
                    Alignment.Center
                )
                .fillMaxWidth(0.5f)
        )
    }
}