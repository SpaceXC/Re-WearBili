package cn.spacexc.wearbili.remake.common

import android.content.Context
import android.widget.Toast
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
    private fun makeText(content: String, context: Context = Application.getApplication()): Toast {
        /*val toast = Toast(context)
        val view: View = ComposeView(context).apply {
            setContent { Text(content) }
        }
        toast.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view*/
        return Toast.makeText(context, content, Toast.LENGTH_SHORT)
    }

    fun showText(content: String, context: Context = Application.getApplication()) {
        makeText(content, context).show()
    }

    fun debugShowText(content: String, context: Context = Application.getApplication()) {
        if (SettingsManager.getInstance().isDebug) {
            showText(content, context)
        }
    }

    fun Any.debugToast(description: String?) {
        if (SettingsManager.getInstance().isDebug) {
            makeText("$description: $this").show()
        }
    }

    fun <T> T.debugToast(): T {
        if (SettingsManager.getInstance().isDebug) {
            makeText("$this").show()
        }
        return this
    }

    fun debugToast(content: String?) {
        if (SettingsManager.getInstance().isDebug) {
            makeText("$content").show()
        }
    }

    fun <T> T.debugToastWithGeneric(): T {
        if (SettingsManager.getInstance().isDebug) makeText("$this")
        return this
    }
}