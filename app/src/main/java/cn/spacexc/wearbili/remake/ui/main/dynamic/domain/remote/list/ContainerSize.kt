package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class ContainerSize(
    @SerializedName("height")
    val height: Double,
    @SerializedName("width")
    val width: Double
)