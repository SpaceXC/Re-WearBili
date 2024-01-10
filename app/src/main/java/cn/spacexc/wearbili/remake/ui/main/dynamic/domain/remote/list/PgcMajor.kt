package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class PgcMajor(
    @SerializedName("badge")
    val badge: BadgeX,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("epid")
    val epid: Long,
    @SerializedName("jump_url")
    val jumpUrl: String,
    @SerializedName("season_id")
    val seasonid: Long,
    @SerializedName("stat")
    val stat: StatX,
    @SerializedName("sub_type")
    val subType: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: Int
)