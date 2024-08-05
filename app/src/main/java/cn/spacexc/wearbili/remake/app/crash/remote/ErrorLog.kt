package cn.spacexc.wearbili.remake.app.crash.remote

import cn.leancloud.LCObject
import cn.spacexc.wearbili.remake.app.Application
import kotlinx.serialization.Serializable

@Serializable
data class ErrorLog(
    val reportTime: Long,
    val mid: Long?,
    val exceptionDescription: String,
    val stacktrace: String,
    val shortId: String = "",
    val manufacture: String = android.os.Build.MANUFACTURER,
    val brand: String = android.os.Build.BRAND,
    val device: String = android.os.Build.DEVICE,
    val model: String = android.os.Build.MODEL,
    val status: String = "pending",
    val versionInt: Int = Application.getReleaseNumber().toInt(),
    val versionName: String = Application.getVersionName(),
)

fun ErrorLog.toLCObject() = LCObject("ErrorLogReport").apply {
    put("reportTime", reportTime)
    put("mid", mid.toString())
    put("description", exceptionDescription)
    put("stacktrace", stacktrace)
    put("manufacture", manufacture)
    put("brand", brand)
    put("device", device)
    put("model", model)
    put("versionInt", versionInt)
    put("versionName", versionName)
}

fun LCObject.toErrorLog() = ErrorLog(
    reportTime = getLong("reportTime"),
    mid = getLong("mid"),
    exceptionDescription = getString("description"),
    stacktrace = getString("stacktrace")
)