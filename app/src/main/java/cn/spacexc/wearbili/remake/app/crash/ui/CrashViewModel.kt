package cn.spacexc.wearbili.remake.app.crash.ui

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.wearbili.common.EncryptUtils
import cn.spacexc.wearbili.common.domain.user.UserManager
import cn.spacexc.wearbili.remake.app.Application
import cn.spacexc.wearbili.remake.app.crash.remote.ErrorLog
import cn.spacexc.wearbili.remake.app.crash.remote.toLCObject
import cn.spacexc.wearbili.remake.app.link.qrcode.PARAM_QRCODE_CONTENT
import cn.spacexc.wearbili.remake.app.link.qrcode.PARAM_QRCODE_MESSAGE
import cn.spacexc.wearbili.remake.app.link.qrcode.QrCodeActivity
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrashViewModel @Inject constructor(
    private val userManager: UserManager
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
                mid = userManager.mid(),
                exceptionDescription = exceptionDescription,
                stacktrace = stacktrace
            )
            val lcObject = errorLog.toLCObject()
            val id = convertToShortId("$stacktrace${Application.getVersionCode()}")
            lcObject.put("shortId", id)
            lcObject.saveInBackground().subscribe({
                context.startActivity(Intent(context, QrCodeActivity::class.java).apply {
                    putExtra(PARAM_QRCODE_CONTENT, it.objectId)
                    putExtra(PARAM_QRCODE_MESSAGE, "请将此二维码提供给开发者\nIssue id: $id")
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
            })
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