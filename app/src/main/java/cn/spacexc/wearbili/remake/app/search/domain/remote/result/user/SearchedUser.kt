package cn.spacexc.wearbili.remake.app.search.domain.remote.result.user


import com.google.gson.annotations.SerializedName

data class SearchedUser(
    @SerializedName("expand")
    val expand: Expand,
    @SerializedName("face_nft")
    val faceNft: Int,
    @SerializedName("face_nft_type")
    val faceNftType: Int,
    @SerializedName("fans")
    val fans: Int,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("hit_columns")
    val hitColumns: List<Any>,
    @SerializedName("is_live")
    val isLive: Int,
    @SerializedName("is_senior_member")
    val isSeniorMember: Int,
    @SerializedName("is_upuser")
    val isUpuser: Int,
    @SerializedName("level")
    val level: Int,
    @SerializedName("mid")
    val mid: Long,
    @SerializedName("official_verify")
    val officialVerify: OfficialVerify,
    @SerializedName("res")
    val res: List<Re>,
    @SerializedName("room_id")
    val roomid: Long,
    @SerializedName("type")
    val type: String,
    @SerializedName("uname")
    val uname: String,
    @SerializedName("upic")
    val upic: String,
    @SerializedName("usign")
    val usign: String,
    @SerializedName("verify_info")
    val verifyInfo: String,
    @SerializedName("videos")
    val videos: Int
)