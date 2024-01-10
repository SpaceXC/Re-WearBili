package cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.command.vote


import com.google.gson.annotations.SerializedName

data class VoteExtra(
    val cnt: Int,
    val duration: Int,
    val icon: String,
    @SerializedName("my_vote")
    val myVote: Int,
    val options: List<Option>,
    val posX: Double,
    @SerializedName("posX_2")
    val posX2: Int,
    val posY: Double,
    @SerializedName("posY_2")
    val posY2: Int,
    @SerializedName("pub_dynamic")
    val pubDynamic: Boolean,
    val question: String,
    @SerializedName("show_status")
    val showStatus: Int,
    @SerializedName("shrink_icon")
    val shrinkIcon: String,
    @SerializedName("shrink_title")
    val shrinkTitle: String,
    @SerializedName("vote_id")
    val voteId: Int
)