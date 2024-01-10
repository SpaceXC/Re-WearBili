package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Badge(
    @SerializedName("bg_color")
    val bgColor: String,
    @SerializedName("color")
    val color: String,
    @SerializedName("text")
    val text: String
)