package cn.spacexc.wearbili.remake.app.crash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.wearbili.common.domain.user.UserManager
import cn.spacexc.wearbili.remake.app.crash.remote.ErrorLog
import cn.spacexc.wearbili.remake.app.crash.remote.toLCObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.security.MessageDigest
import javax.inject.Inject

@HiltViewModel
class CrashViewModel @Inject constructor(
    private val userManager: UserManager
) : ViewModel() {
    fun uploadLog(
        exceptionDescription: String,
        stacktrace: String
    ) {
        viewModelScope.launch {
            val errorLog = ErrorLog(
                reportTime = System.currentTimeMillis(),
                mid = userManager.mid(),
                exceptionDescription = exceptionDescription,
                stacktrace = stacktrace
            )
            val lcObject = errorLog.toLCObject()
            val id = convertToShortId(lcObject.objectId)
            lcObject.put("shortId", id)
            lcObject.saveInBackground().subscribe {

            }
        }
    }

    private fun convertToShortId(longId: String): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        val hashBytes = messageDigest.digest(longId.toByteArray())

        // 取哈希值的前6个字节，并将其转换为16进制字符串

        return hashBytes.take(6).joinToString("") { "%02x".format(it) }
    }

}