package cn.spacexc.wearbili.remake.ui.search.domain.remote.result.mediaft


import com.google.gson.annotations.SerializedName

data class Badge(
    @SerializedName("bg_color")
    val bgColor: String,
    @SerializedName("bg_color_night")
    val bgColorNight: String,
    @SerializedName("bg_style")
    val bgStyle: Int,
    @SerializedName("border_color")
    val borderColor: String,
    @SerializedName("border_color_night")
    val borderColorNight: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("text_color")
    val textColor: String,
    @SerializedName("text_color_night")
    val textColorNight: String
)