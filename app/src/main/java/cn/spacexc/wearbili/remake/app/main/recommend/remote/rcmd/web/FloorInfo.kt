package cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.web


import com.google.gson.annotations.SerializedName

data class FloorInfo(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("rows")
    val rows: Int
)