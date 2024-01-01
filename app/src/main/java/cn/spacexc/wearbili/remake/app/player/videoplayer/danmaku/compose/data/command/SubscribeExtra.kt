package cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.command


import com.google.gson.annotations.SerializedName

data class SubscribeExtra(
    @SerializedName("arc_type")
    val arcType: Int,
    val duration: Int,
    val icon: String,
    val posX: Double,
    @SerializedName("posX_2")
    val posX2: Int,
    val posY: Double,
    @SerializedName("posY_2")
    val posY2: Int,
    val type: Int,
    @SerializedName("upower_button_map")
    val upowerButtonMap: Any,
    @SerializedName("upower_guide")
    val upowerGuide: String,
    @SerializedName("upower_icon")
    val upowerIcon: String,
    @SerializedName("upower_icon_web")
    val upowerIconWeb: String,
    @SerializedName("upower_jump_url")
    val upowerJumpUrl: String,
    @SerializedName("upower_open")
    val upowerOpen: Boolean,
    @SerializedName("upower_state")
    val upowerState: Int
)