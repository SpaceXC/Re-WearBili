package cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.command


import com.google.gson.annotations.SerializedName

data class AdvertiseExtra(
    @SerializedName("cmtime_type")
    val cmtimeType: Int,
    @SerializedName("dm_key_word")
    val dmKeyWord: String,
    val duration: Int,
    @SerializedName("end_time")
    val endTime: Int,
    val icon: String,
    @SerializedName("material_id")
    val materialid: Long,
    val posX: Double,
    @SerializedName("posX_2")
    val posX2: Int,
    val posY: Double,
    @SerializedName("posY_2")
    val posY2: Int,
    @SerializedName("slogan_icon")
    val sloganIcon: String,
    @SerializedName("start_time")
    val startTime: Int,
    @SerializedName("up_slogan")
    val upSlogan: String
)