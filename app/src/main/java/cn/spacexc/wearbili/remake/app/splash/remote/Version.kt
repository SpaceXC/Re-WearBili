package cn.spacexc.wearbili.remake.app.splash.remote

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Version(
    val channel: String,
    val versionName: String,
    val versionCode: Int,
    val updateLog: String,
    val downloadAddress: String,
    val updateTime: Long,
    val mandatory: Boolean
): Parcelable