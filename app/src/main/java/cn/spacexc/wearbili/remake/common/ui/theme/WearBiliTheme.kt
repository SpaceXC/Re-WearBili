package cn.spacexc.wearbili.remake.common.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cn.spacexc.wearbili.remake.app.settings.ProvideConfiguration

/**
 * Created by XC-Qan on 2023/3/21.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

typealias AppTheme = MaterialTheme

@Composable
fun WearBiliTheme(content: @Composable () -> Unit) {
    ProvideConfiguration {
        MaterialTheme(
            typography = wearbiliTypography
        ) {
            content()
        }
    }
}