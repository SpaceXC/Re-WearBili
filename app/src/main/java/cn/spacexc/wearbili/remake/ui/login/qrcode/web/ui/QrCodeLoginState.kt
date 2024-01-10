package cn.spacexc.wearbili.remake.ui.login.qrcode.web.ui

import androidx.compose.ui.graphics.ImageBitmap

/**
 * Created by XC-Qan on 2023/4/4.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

sealed class QrCodeLoginStatus(val code: Int) {
    data object Loading : QrCodeLoginStatus(1)
    data object Success : QrCodeLoginStatus(0)
    data object Pending : QrCodeLoginStatus(86101)  //用户没有扫码
    data object Waiting : QrCodeLoginStatus(86090)  //用户扫了码但是还没有确认
    data object Timeout : QrCodeLoginStatus(86038)
    data object Failed : QrCodeLoginStatus(2)
    data object GettingKey : QrCodeLoginStatus(3)
    data object FailedGettingKey : QrCodeLoginStatus(4)
}

data class QrCodeLoginScreenState(
    val qrCodeBitmap: ImageBitmap?,
    val currentLoginStatus: QrCodeLoginStatus
)
