package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("has_more")
    val hasMore: Boolean,
    @SerializedName("items")
    val items: List<DynamicItem>,
    @SerializedName("offset")
    val offset: String,
    @SerializedName("update_baseline")
    val updateBaseline: String,
    @SerializedName("update_num")
    val updateNum: Int
)