package cn.spacexc.wearbili.remake.ui.splash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.common.ui.CirclesBackground
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.ui.APP_VERSION_CODE
import cn.spacexc.wearbili.remake.ui.APP_VERSION_NAME

/**
 * Created by XC-Qan on 2023/3/21.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val SplashScreenRoute = "SplashScreen"

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
        Text(
            text = "$APP_VERSION_NAME\nRelease $APP_VERSION_CODE\n此软件正处在测试阶段\n请勿传播安装包\nThis app is still in test phase\nDO NOT DISTRIBUTE",
            fontFamily = wearbiliFontFamily,
            fontSize = 10.spx,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .alpha(0.2f)
                .align(Alignment.Center),
            textAlign = TextAlign.Center
        )
    }
}

fun NavGraphBuilder.splashScreen(onInitApp: () -> Unit) {
    composable(SplashScreenRoute) {
        LaunchedEffect(key1 = Unit, block = {
            onInitApp()
        })
        SplashScreen()
    }
}