package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class ThreePointItem(
    @SerializedName("label")
    val label: String,
    @SerializedName("type")
    val type: String
)