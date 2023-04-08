package cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.web


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("business_card")
    val businessCard: Any?,
    @SerializedName("floor_info")
    val floorInfo: List<cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.web.FloorInfo>,
    @SerializedName("item")
    val item: List<cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.web.Item>,
    @SerializedName("mid")
    val mid: Long,
    @SerializedName("preload_expose_pct")
    val preloadExposePct: Double,
    @SerializedName("preload_floor_expose_pct")
    val preloadFloorExposePct: Double,
    @SerializedName("user_feature")
    val userFeature: Any?
)