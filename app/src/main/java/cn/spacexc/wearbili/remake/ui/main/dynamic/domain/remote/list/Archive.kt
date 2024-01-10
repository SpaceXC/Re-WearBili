package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Archive(
    @SerializedName("aid")
    val aid: String,
    @SerializedName("badge")
    val badge: Badge,
    @SerializedName("bvid")
    val bvid: String,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("disable_preview")
    val disablePreview: Int,
    @SerializedName("duration_text")
    val durationText: String,
    @SerializedName("jump_url")
    val jumpUrl: String,
    @SerializedName("stat")
    val stat: Stat,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: Int
)