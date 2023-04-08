package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Created by XC-Qan on 2023/3/21.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */
 
private const val fontSizeMultiple = 1f

val Int.spx: TextUnit
    get() = (this * fontSizeMultiple).sp

val Float.spx: TextUnit
    get() = (this * fontSizeMultiple).sp