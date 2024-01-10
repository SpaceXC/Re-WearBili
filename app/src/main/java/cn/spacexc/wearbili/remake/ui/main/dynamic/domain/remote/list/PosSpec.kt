package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class PosSpec(
    @SerializedName("axis_x")
    val axisX: Double,
    @SerializedName("axis_y")
    val axisY: Double,
    @SerializedName("coordinate_pos")
    val coordinatePos: Int
)