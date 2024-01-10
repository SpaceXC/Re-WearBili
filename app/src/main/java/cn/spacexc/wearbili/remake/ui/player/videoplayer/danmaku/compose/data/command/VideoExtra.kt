package cn.spacexc.wearbili.remake.ui.player.videoplayer.danmaku.compose.data.command


import com.google.gson.annotations.SerializedName

data class VideoExtra(
    val aid: Int,
    @SerializedName("arc_duration")
    val arcDuration: Int,
    @SerializedName("arc_pic")
    val arcPic: String,
    @SerializedName("arc_type")
    val arcType: Int,
    val bvid: String,
    val duration: Int,
    val icon: String,
    @SerializedName("jump_url")
    val jumpUrl: String,
    val posX: Double,
    @SerializedName("posX_2")
    val posX2: Int,
    val posY: Double,
    @SerializedName("posY_2")
    val posY2: Int,
    @SerializedName("show_status")
    val showStatus: Int,
    @SerializedName("shrink_icon")
    val shrinkIcon: String,
    @SerializedName("shrink_title")
    val shrinkTitle: String
)