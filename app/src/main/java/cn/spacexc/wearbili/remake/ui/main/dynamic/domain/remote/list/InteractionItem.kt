package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class InteractionItem(
    @SerializedName("desc")
    val desc: Desc,
    @SerializedName("type")
    val type: Int
)