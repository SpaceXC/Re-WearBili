package cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.web


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("av_feature")
    val avFeature: Any?,
    @SerializedName("business_info")
    val businessInfo: cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.web.BusinessInfo?,
    @SerializedName("bvid")
    val bvid: String,
    @SerializedName("cid")
    val cid: Long,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("goto")
    val goto: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("is_followed")
    val isFollowed: Int,
    @SerializedName("is_stock")
    val isStock: Int,
    @SerializedName("ogv_info")
    val ogvInfo: Any?,
    @SerializedName("owner")
    val owner: cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.web.Owner?,
    @SerializedName("pic")
    val pic: String,
    @SerializedName("pos")
    val pos: Int,
    @SerializedName("pubdate")
    val pubdate: Int,
    @SerializedName("rcmd_reason")
    val rcmdReason: cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.web.RcmdReason?,
    @SerializedName("room_info")
    val roomInfo: Any?,
    @SerializedName("show_info")
    val showInfo: Int,
    @SerializedName("stat")
    val stat: cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.web.Stat?,
    @SerializedName("title")
    val title: String,
    @SerializedName("track_id")
    val trackId: String,
    @SerializedName("uri")
    val uri: String
)