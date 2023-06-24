package cn.spacexc.wearbili.common.domain.video

/**
 * Created by XC-Qan on 2022/6/23.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

fun Long.toShortChinese(): String {
    if (this <= 0) {
        return "0"
    }
    if (this < 10000) {
        return this.toString()
    }
    if (this in 10000..99999999) {
        val numStr = this.toString()
        val tenThousand = numStr.substring(0, numStr.length - 4)
        val numAfterTenThousand: String = numStr[numStr.length - 4].toString()
        return if (numAfterTenThousand != "0") {
            "$tenThousand.$numAfterTenThousand" + "万"
        } else tenThousand + "万"
    }
    if (this > 100000000) {
        val numStr = this.toString()
        val yi = numStr.substring(0, numStr.length - 8)
        val numAfterYi: String = numStr[numStr.length - 8].toString()
        return if (numAfterYi != "0") {
            "$yi.$numAfterYi" + "亿"
        } else yi + "亿"
        //return this.toString()
    }
    return ""
}

fun Int.toShortChinese(): String {
    if (this <= 0) {
        return "0"
    }
    if (this < 10000) {
        return this.toString()
    }
    if (this in 10000..99999999) {
        val numStr = this.toString()
        val tenThousand = numStr.substring(0, numStr.length - 4)
        val numAfterTenThousand: String = numStr[numStr.length - 4].toString()
        return if (numAfterTenThousand != "0") {
            "$tenThousand.$numAfterTenThousand" + "万"
        } else tenThousand + "万"
    }
    if (this > 100000000) {
        val numStr = this.toString()
        val yi = numStr.substring(0, numStr.length - 8)
        val numAfterYi: String = numStr[numStr.length - 8].toString()
        return if (numAfterYi != "0") {
            "$yi.$numAfterYi" + "亿"
        } else yi + "亿"
        //return this.toString()
    }
    return ""
}