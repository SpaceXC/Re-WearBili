package cn.spacexc.wearbili.remake.app.search.domain.remote.result.user


import com.google.gson.annotations.SerializedName

data class Re(
    @SerializedName("aid")
    val aid: Long,
    @SerializedName("arcurl")
    val arcurl: String,
    @SerializedName("bvid")
    val bvid: String,
    @SerializedName("coin")
    val coin: Int,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("dm")
    val dm: Int,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("fav")
    val fav: Int,
    @SerializedName("is_pay")
    val isPay: Int,
    @SerializedName("is_union_video")
    val isUnionVideo: Int,
    @SerializedName("pic")
    val pic: String,
    @SerializedName("play")
    val play: String,
    @SerializedName("pubdate")
    val pubdate: Int,
    @SerializedName("title")
    val title: String
)