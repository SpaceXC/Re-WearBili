package cn.spacexc.wearbili.remake.ui.search.domain.remote.result.video


import com.google.gson.annotations.SerializedName

data class SearchedVideo(
    @SerializedName("aid")
    val aid: Long,
    @SerializedName("arcrank")
    val arcrank: String,
    @SerializedName("arcurl")
    val arcurl: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("badgepay")
    val badgepay: Boolean,
    @SerializedName("biz_data")
    val bizData: Any?,
    @SerializedName("bvid")
    val bvid: String,
    @SerializedName("corner")
    val corner: String,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("danmaku")
    val danmaku: Int,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("favorites")
    val favorites: Int,
    @SerializedName("hit_columns")
    val hitColumns: List<String>,
    @SerializedName("id")
    val id: Long,
    @SerializedName("is_pay")
    val isPay: Int,
    @SerializedName("is_union_video")
    val isUnionVideo: Int,
    @SerializedName("like")
    val like: Int,
    @SerializedName("mid")
    val mid: Long,
    @SerializedName("new_rec_tags")
    val newRecTags: List<Any>,
    @SerializedName("pic")
    val pic: String,
    @SerializedName("play")
    val play: Int,
    @SerializedName("pubdate")
    val pubdate: Int,
    @SerializedName("rank_score")
    val rankScore: Int,
    @SerializedName("rec_reason")
    val recReason: String,
    @SerializedName("rec_tags")
    val recTags: Any?,
    @SerializedName("review")
    val review: Int,
    @SerializedName("senddate")
    val senddate: Int,
    @SerializedName("tag")
    val tag: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("typeid")
    val typeid: String,
    @SerializedName("typename")
    val typename: String,
    @SerializedName("upic")
    val upic: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("video_review")
    val videoReview: Int,
    @SerializedName("view_type")
    val viewType: String
)