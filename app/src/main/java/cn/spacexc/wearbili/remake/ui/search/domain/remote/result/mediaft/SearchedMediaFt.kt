package cn.spacexc.wearbili.remake.ui.search.domain.remote.result.mediaft


import com.google.gson.annotations.SerializedName

data class SearchedMediaFt(
    @SerializedName("areas")
    val areas: String,
    @SerializedName("badges")
    val badges: List<Badge>?,
    @SerializedName("button_text")
    val buttonText: String,
    @SerializedName("corner")
    val corner: Int,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("cv")
    val cv: String,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("display_info")
    val displayInfo: List<DisplayInfo>,
    @SerializedName("ep_size")
    val epSize: Int,
    @SerializedName("eps")
    val eps: List<Ep>,
    @SerializedName("fix_pubtime_str")
    val fixPubtimeStr: String,
    @SerializedName("goto_url")
    val gotoUrl: String,
    @SerializedName("hit_columns")
    val hitColumns: Any?,
    @SerializedName("hit_epids")
    val hitEpids: String,
    @SerializedName("index_show")
    val indexShow: String,
    @SerializedName("is_avid")
    val isAvid: Boolean,
    @SerializedName("is_follow")
    val isFollow: Int,
    @SerializedName("is_selection")
    val isSelection: Int,
    @SerializedName("media_id")
    val mediaid: Long,
    @SerializedName("media_mode")
    val mediaMode: Int,
    @SerializedName("media_score")
    val mediaScore: MediaScore,
    @SerializedName("media_type")
    val mediaType: Int,
    @SerializedName("org_title")
    val orgTitle: String,
    @SerializedName("pgc_season_id")
    val pgcSeasonid: Long,
    @SerializedName("pubtime")
    val pubtime: Int,
    @SerializedName("season_id")
    val seasonid: Long,
    @SerializedName("season_type")
    val seasonType: Int,
    @SerializedName("season_type_name")
    val seasonTypeName: String,
    @SerializedName("selection_style")
    val selectionStyle: String,
    @SerializedName("staff")
    val staff: String,
    @SerializedName("styles")
    val styles: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String
)