package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Basic(
    @SerializedName("comment_id_str")
    val commentIdStr: String,
    @SerializedName("comment_type")
    val commentType: Int,
    @SerializedName("like_icon")
    val likeIcon: LikeIcon,
    @SerializedName("rid_str")
    val ridStr: String
)