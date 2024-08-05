package cn.spacexc.wearbili.remake.app.crash.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.common.EncryptUtils
import cn.spacexc.wearbili.remake.app.TAG
import cn.spacexc.wearbili.remake.app.crash.remote.ErrorLog
import cn.spacexc.wearbili.remake.app.crash.remote.Response
import cn.spacexc.wearbili.remake.app.link.qrcode.PARAM_QRCODE_CONTENT
import cn.spacexc.wearbili.remake.app.link.qrcode.PARAM_QRCODE_MESSAGE
import cn.spacexc.wearbili.remake.app.link.qrcode.QrCodeActivity
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.networking.KtorNetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val FEEDBACK_SERVER_BASE_URL = "http://43.139.182.127"

@HiltViewModel
class CrashViewModel @Inject constructor(
    private val networkUtils: KtorNetworkUtils,
) : ViewModel() {
    var uiState: UIState by mutableStateOf(UIState.Success)
        private set

    fun uploadLog(
        exceptionDescription: String,
        stacktrace: String,
        context: Context
    ) {
        viewModelScope.launch {
            uiState = UIState.Loading
            val errorLog = ErrorLog(
                reportTime = System.currentTimeMillis(),
                mid = UserUtils.mid(),
                exceptionDescription = exceptionDescription,
                stacktrace = stacktrace
            )

            val response = networkUtils.post<Response<String>, ErrorLog>(
                "$FEEDBACK_SERVER_BASE_URL/log/upload",
                errorLog
            )
            Log.d(TAG, "uploadLog: ${response.data?.message}")
            if (response.data?.code == 0) {
                val id = response.data.body!!
                context.startActivity(Intent(context, QrCodeActivity::class.java).apply {
                    putExtra(PARAM_QRCODE_CONTENT, id)
                    putExtra(PARAM_QRCODE_MESSAGE, "Issue id: $id\n请在必要时提供此ID")
                })
                uiState = UIState.Success
            } else {
                uiState = UIState.Success
                ToastUtils.showSnackBar(
                    response.data?.message ?: "日志上传失败了...",
                    Icons.Default.Close,
                    Icons.Default.Refresh
                ) {
                    uploadLog(exceptionDescription, stacktrace, context)
                }
            }
            /*val lcObject = errorLog.toLCObject()
            val id = convertToShortId("$stacktrace${Application.getReleaseNumber()}")
            lcObject.put("shortId", id)
            lcObject.saveInBackground().subscribe({
                context.startActivity(Intent(context, QrCodeActivity::class.java).apply {
                    putExtra(PARAM_QRCODE_CONTENT, it.objectId)
                    putExtra(PARAM_QRCODE_MESSAGE, "Issue id: $id\n请在必要时提供此ID")
                })
                uiState = UIState.Success
            }, {
                uiState = UIState.Success
                ToastUtils.showSnackBar(
                    it.message ?: "日志上传失败了...",
                    Icons.Default.Close,
                    Icons.Default.Refresh
                ) {
                    uploadLog(exceptionDescription, stacktrace, context)
                }
            })*/
        }
    }

    private fun convertToShortId(content: String): String {
        return EncryptUtils.md5(content).substring(0..5)
        /*val messageDigest = MessageDigest.getInstance("MD5")
        val hashBytes = messageDigest.digest(longId.toByteArray())

        // 取哈希值的前6个字节，并将其转换为16进制字符串

        return hashBytes.take(6).joinToString("") { "%02x".format(it) }*/
    }

}