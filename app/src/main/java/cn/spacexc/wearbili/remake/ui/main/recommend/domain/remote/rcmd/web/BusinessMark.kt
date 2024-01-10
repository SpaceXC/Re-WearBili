package cn.spacexc.wearbili.remake.ui.main.recommend.domain.remote.rcmd.web


import com.google.gson.annotations.SerializedName

data class BusinessMark(
    @SerializedName("bg_border_color")
    val bgBorderColor: String,
    @SerializedName("bg_color")
    val bgColor: String,
    @SerializedName("bg_color_night")
    val bgColorNight: String,
    @SerializedName("border_color")
    val borderColor: String,
    @SerializedName("border_color_night")
    val borderColorNight: String,
    @SerializedName("img_height")
    val imgHeight: Int,
    @SerializedName("img_url")
    val imgUrl: String,
    @SerializedName("img_width")
    val imgWidth: Int,
    @SerializedName("text")
    val text: String,
    @SerializedName("text_color")
    val textColor: String,
    @SerializedName("text_color_night")
    val textColorNight: String,
    @SerializedName("type")
    val type: Int
)