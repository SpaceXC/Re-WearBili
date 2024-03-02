package cn.spacexc.wearbili.remake.common.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import cn.spacexc.wearbili.common.ifNullOrZero
import cn.spacexc.wearbili.remake.app.settings.LocalConfiguration

@Composable
fun ProvideLocalDensity(
    content: @Composable () -> Unit
) {
    val scale =
        LocalConfiguration.current.screenDisplayScaleFactor.ifNullOrZero { 1f }.toFloat() //用户自行设置的
    val fontScale = LocalDensity.current.fontScale
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val widthPixels = displayMetrics.widthPixels.toFloat()
    //算出来的，可在其基础上微调
    val scaleFactor = widthPixels / 372.0f  //Oppo Watch基准数据
    val density = 2f * scaleFactor * scale
    CompositionLocalProvider(
        LocalDensity provides Density(
            density = density,
            fontScale = fontScale
        )
    ) {
        Box {
            content()
        }
    }
}