package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Uncheck(
    @SerializedName("icon_url")
    val iconUrl: String,
    @SerializedName("text")
    val text: String
)