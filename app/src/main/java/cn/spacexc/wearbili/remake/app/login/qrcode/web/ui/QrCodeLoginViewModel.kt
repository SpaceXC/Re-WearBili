package cn.spacexc.wearbili.remake.app.login.qrcode.web.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import cn.spacexc.bilibilisdk.data.DataManager
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils
import cn.spacexc.wearbili.common.domain.network.NetworkResponse
import cn.spacexc.wearbili.common.domain.qrcode.QRCodeUtil
import cn.spacexc.wearbili.remake.app.login.qrcode.web.domain.remote.qrcode.QrCodeKey
import cn.spacexc.wearbili.remake.app.login.qrcode.web.domain.remote.state.QrCodeScanState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/4/2.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@HiltViewModel
class QrCodeLoginViewModel @Inject constructor(
    private val networkUtils: KtorNetworkUtils,
    private val qrCodeUtil: QRCodeUtil,
    private val dataManager: DataManager
) : ViewModel() {
    var screenState by mutableStateOf(
        QrCodeLoginScreenState(
            qrCodeBitmap = null,
            currentLoginStatus = QrCodeLoginStatus.Loading
        )
    )

    suspend fun startLogin(onFinishedLogin: () -> Unit) {
        screenState = screenState.copy(currentLoginStatus = QrCodeLoginStatus.Loading)
        //val qrCode = networkUtils.get<QrCode>("https://passport.bilibili.com/qrcode/getLoginUrl")
        val qrCode: NetworkResponse<QrCodeKey> =
            networkUtils.get("https://passport.bilibili.com/x/passport-login/web/qrcode/generate")
        if (qrCode.code != 0) {
            screenState = screenState.copy(currentLoginStatus = QrCodeLoginStatus.Failed)
            return
        }
        val qrCodeKey = qrCode.data?.data?.qrcodeKey
        val url = qrCode.data?.data?.url
        val bitmap = qrCodeUtil.createQRCodeBitmap(
            url, 512, 512,
            cn.spacexc.wearbili.common.domain.qrcode.ERROR_CORRECTION_M
        )
        screenState = screenState.copy(
            qrCodeBitmap = bitmap?.asImageBitmap(),
            currentLoginStatus = QrCodeLoginStatus.Pending
        )
        while (true) {
            UserUtils.setCurrentUid(null)
            val authState =
                networkUtils.get<QrCodeScanState>("https://passport.bilibili.com/x/passport-login/web/qrcode/poll?qrcode_key=$qrCodeKey")

            if (authState.code != 0) {
                screenState = screenState.copy(currentLoginStatus = QrCodeLoginStatus.Failed)
                return
            }
            authState.data.logd()
            if (authState.data?.data?.refreshToken.isNullOrEmpty()) {
                when (authState.data?.code) {
                    QrCodeLoginStatus.Pending.code -> {
                        screenState =
                            screenState.copy(currentLoginStatus = QrCodeLoginStatus.Pending)
                    }

                    QrCodeLoginStatus.Waiting.code -> {
                        screenState =
                            screenState.copy(currentLoginStatus = QrCodeLoginStatus.Waiting)
                    }

                    QrCodeLoginStatus.Timeout.code -> {
                        screenState =
                            screenState.copy(currentLoginStatus = QrCodeLoginStatus.Timeout)
                        break
                    }

                    else -> {}
                }
            } else {
                val refreshToken = authState.data?.data?.refreshToken
                dataManager.saveString("refreshToken", refreshToken ?: "")
                onFinishedLogin()
                break
            }
            delay(1500)
        }
    }
}