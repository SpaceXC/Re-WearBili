package cn.spacexc.wearbili.remake.app.login.ui

import androidx.compose.ui.graphics.ImageBitmap

/**
 * Created by XC-Qan on 2023/4/4.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

sealed class LoginStatus(code: Int) {
    object Loading: LoginStatus(1)
    object Success: LoginStatus(0)
    object Pending: LoginStatus(-4)  //用户没有扫码
    object Waiting: LoginStatus(-5)  //用户扫了码但是还没有确认
    object Timeout: LoginStatus(-2)
    object Failed: LoginStatus(2)
    object GettingKey: LoginStatus(3)
    object FailedGettingKey: LoginStatus(4)
}

data class LoginScreenState(
    val qrCodeBitmap: ImageBitmap?,
    val currentLoginStatus: LoginStatus
)
