package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class BadgeX(
    @SerializedName("bg_color")
    val bgColor: String,
    @SerializedName("color")
    val color: String,
    @SerializedName("text")
    val text: String
)