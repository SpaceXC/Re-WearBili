package cn.spacexc.wearbili.common.domain.color

import androidx.compose.ui.graphics.Color

/*
 * Created by XC on 2022/11/2.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

fun parseColor(colorHex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(colorHex))
    } catch (e: Exception) {
        e.printStackTrace()
        Color.White
    }
}