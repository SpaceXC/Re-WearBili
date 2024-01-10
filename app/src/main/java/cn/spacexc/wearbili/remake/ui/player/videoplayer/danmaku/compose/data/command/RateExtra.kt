package cn.spacexc.wearbili.remake.ui.player.videoplayer.danmaku.compose.data.command


import com.google.gson.annotations.SerializedName

data class RateExtra(
    @SerializedName("avg_score")
    val avgScore: Int,
    val count: Int,
    val duration: Int,
    @SerializedName("grade_id")
    val gradeId: Int,
    val icon: String,
    @SerializedName("mid_score")
    val midScore: Int,
    val msg: String,
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
    val shrinkTitle: String,
    val skin: Int,
    @SerializedName("skin_font_color")
    val skinFontColor: String,
    @SerializedName("skin_selected")
    val skinSelected: String,
    @SerializedName("skin_unselected")
    val skinUnselected: String,
    @SerializedName("summary_duration")
    val summaryDuration: Int
)