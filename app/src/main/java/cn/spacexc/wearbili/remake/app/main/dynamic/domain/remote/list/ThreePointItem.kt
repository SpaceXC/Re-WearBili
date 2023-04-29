package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class ThreePointItem(
    @SerializedName("label")
    val label: String,
    @SerializedName("type")
    val type: String
)