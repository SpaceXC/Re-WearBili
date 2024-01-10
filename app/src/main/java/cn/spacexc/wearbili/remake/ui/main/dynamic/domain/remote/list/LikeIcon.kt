package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class LikeIcon(
    @SerializedName("action_url")
    val actionUrl: String,
    @SerializedName("end_url")
    val endUrl: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("start_url")
    val startUrl: String
)