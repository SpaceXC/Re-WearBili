package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class InteractionItem(
    @SerializedName("desc")
    val desc: Desc,
    @SerializedName("type")
    val type: Int
)