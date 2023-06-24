package cn.spacexc.wearbili.common.domain.time

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by XC-Qan on 2023/4/12.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

fun Long.secondToTime(): String {
    var second = this
    val hours = second / 3600 //转换小时数
    second %= 3600 //剩余秒数
    val minutes = second / 60 //转换分钟
    second %= 60 //剩余秒数
    return if (hours > 0) {
        unitFormat(hours) + ":" + unitFormat(minutes) + ":" + unitFormat(second)
    } else {
        unitFormat(minutes) + ":" + unitFormat(second)
    }
}

fun Int.secondToTime(): String {
    var second = this
    val hours = second / 3600 //转换小时数
    second %= 3600 //剩余秒数
    val minutes = second / 60 //转换分钟
    second %= 60 //剩余秒数
    return if (hours > 0) {
        unitFormat(hours) + ":" + unitFormat(minutes) + ":" + unitFormat(second)
    } else {
        unitFormat(minutes) + ":" + unitFormat(second)
    }
}

private fun unitFormat(i: Long): String {
    return if (i in 0..9) "0$i" else "" + i
}

private fun unitFormat(i: Int): String {
    return if (i in 0..9) "0$i" else "" + i
}

fun getCurrentTime(): String {
    var minute: String = Calendar.getInstance().get(Calendar.MINUTE).toString()
    var hour: String = Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString()
    if (minute.length == 1) {
        minute = "0$minute"
    }
    if (hour.length == 1) {
        hour = "0$hour"
    }
    return "$hour:$minute"
}

@SuppressLint("SimpleDateFormat")
fun Long.toDateStr(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val date = Date(this)
    val format = SimpleDateFormat(pattern)
    return format.format(date)
}