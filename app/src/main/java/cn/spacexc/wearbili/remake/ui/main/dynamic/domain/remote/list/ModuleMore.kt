package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class ModuleMore(
    @SerializedName("three_point_items")
    val threePointItems: List<ThreePointItem>
)