package cn.spacexc.wearbili.remake.ui.main.recommend.domain.remote.rcmd.web


import com.google.gson.annotations.SerializedName

data class RcmdReason(
    @SerializedName("content")
    val content: String?,
    @SerializedName("reason_type")
    val reasonType: Int
)