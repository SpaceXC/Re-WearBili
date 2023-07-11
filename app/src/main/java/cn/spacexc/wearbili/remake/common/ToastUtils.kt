package cn.spacexc.wearbili.remake.common

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cn.spacexc.wearbili.remake.app.Application
import cn.spacexc.wearbili.remake.app.settings.SettingsManager

/**
 * Created by XC-Qan on 2023/4/13.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

object ToastUtils {
    var Context.toastContent by mutableStateOf("")

    fun showText(content: String, context: Context = Application.getApplication()) {
        context.toastContent = content
        //makeText(content, context).show()
    }

    fun debugShowText(content: String, context: Context = Application.getApplication()) {
        if (SettingsManager.isDebug) {
            showText(content, context)
        }
    }

    fun Any.debugToast(description: String?) {
        if (SettingsManager.isDebug) {
            showText("$description: $this")
        }
    }

    fun <T> T.debugToast(): T {
        if (SettingsManager.isDebug) {
            showText("$this")
        }
        return this
    }

    fun debugToast(content: String?) {
        if (SettingsManager.isDebug) {
            showText("$content")
        }
    }

    fun <T> T.debugToastWithGeneric(): T {
        if (SettingsManager.isDebug) showText("$this")
        return this
    }
}