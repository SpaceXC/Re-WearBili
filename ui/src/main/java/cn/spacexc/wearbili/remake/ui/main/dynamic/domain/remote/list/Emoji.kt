package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Emoji(
    @SerializedName("icon_url")
    val iconUrl: String,
    @SerializedName("size")
    val size: Int,
    @SerializedName("text")
    val text: String,
    @SerializedName("type")
    val type: Int
)