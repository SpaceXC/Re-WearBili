package cn.spacexc.wearbili.common.domain.color

import androidx.compose.ui.graphics.Color

/*
 * Created by XC on 2022/11/2
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

fun parseColor(rgb: Int): Color {
    val alpha = 255
    // 从 RGB 中提取红、绿、蓝分量
    val red = (rgb shr 16) and 0xFF
    val green = (rgb shr 8) and 0xFF
    val blue = rgb and 0xFF
    // 合并为 ARGB 整数值

    return Color((alpha shl 24) or (red shl 16) or (green shl 8) or blue)
}


fun String?.parseColor(): Color? {
    if (this == null) return null
    return try {
        Color(android.graphics.Color.parseColor(this))
    } catch (e: Exception) {
        e.printStackTrace()
        Color.White
    }
}