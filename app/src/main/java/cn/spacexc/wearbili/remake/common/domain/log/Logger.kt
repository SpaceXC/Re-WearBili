package cn.spacexc.wearbili.remake.common.domain.log

import android.util.Log
import cn.spacexc.wearbili.remake.app.TAG

/**
 * Created by XC-Qan on 2023/3/29.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

fun logd(content: String?) = Log.d(TAG, "$content")

fun logd(content: Any?) = Log.d(TAG, "$content")

fun <T> T?.logd(content: String? = ""): T? {
    if(content.isNullOrEmpty()) {
        Log.d(TAG, "$this")
    }
    else {
        Log.d(TAG, "$content: $this")
    }
    return this
}