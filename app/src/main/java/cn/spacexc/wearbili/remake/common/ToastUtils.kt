package cn.spacexc.wearbili.remake.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Created by XC-Qan on 2023/4/13.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

object ToastUtils {
    var toastContent by mutableStateOf("")
    var action by mutableStateOf("")
    var onActionClicked: () -> Unit = {}

    fun showText(content: String) {
        toastContent = content
    }

    fun showSnackBar(content: String, action: String, onClick: () -> Unit) {

    }
}