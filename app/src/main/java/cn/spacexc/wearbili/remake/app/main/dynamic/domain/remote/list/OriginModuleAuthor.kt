package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class OriginModuleAuthor(
    @SerializedName("face")
    val face: String,
    @SerializedName("face_nft")
    val faceNft: Boolean,
    @SerializedName("following")
    val following: Boolean?,
    @SerializedName("jump_url")
    val jumpUrl: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("mid")
    val mid: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("official_verify")
    val officialVerify: OfficialVerify?,
    @SerializedName("pendant")
    val pendant: Pendant?,
    @SerializedName("pub_action")
    val pubAction: String,
    @SerializedName("pub_time")
    val pubTime: String,
    @SerializedName("pub_ts")
    val pubTs: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("vip")
    val vip: Vip?
)