package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class ModuleMore(
    @SerializedName("three_point_items")
    val threePointItems: List<ThreePointItem>
)