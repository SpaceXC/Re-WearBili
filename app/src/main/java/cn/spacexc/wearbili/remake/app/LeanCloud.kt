package cn.spacexc.wearbili.remake.app

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class LeanCloudUserSearch(
    val results: List<UserResult>?
)

@Parcelize
data class LeanCloudAppUpdatesSearch(
    val results: List<AppUpdatesResult>?
) : Parcelable

data class UserResult(
    val createdAt: String,
    val objectId: String,
    val uid: String,
    val addSource: String,
    val updatedAt: String
)

@Parcelize
data class AppUpdatesResult(
    val createdAt: String,
    val objectId: String,
    val channel: String,
    val versionName: String,
    val versionCode: Int,
    val downloadAddress: String,
    val mandatory: Boolean,
    val updateLog: String,
    val updatedAt: String
) : Parcelable