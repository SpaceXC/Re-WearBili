package cn.spacexc.wearbili.videoplayer.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

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
    MaterialTheme(
        typography = wearbiliTypography
    ) {
        content()
    }
}