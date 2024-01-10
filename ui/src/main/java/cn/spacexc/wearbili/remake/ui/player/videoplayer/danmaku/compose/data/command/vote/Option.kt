package cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.command.vote


import com.google.gson.annotations.SerializedName

data class Option(
    val cnt: Int,
    val desc: String,
    @SerializedName("has_self_def")
    val hasSelfDef: Boolean,
    val idx: Int
)