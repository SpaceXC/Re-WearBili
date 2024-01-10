package cn.spacexc.wearbili.remake.common.ui

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

/**
 * Created by XC-Qan on 2023/4/26.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun isRound() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) LocalConfiguration.current.isScreenRound else false