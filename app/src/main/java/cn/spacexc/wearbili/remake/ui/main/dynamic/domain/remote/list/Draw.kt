package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Draw(
    @SerializedName("id")
    val id: Long,
    @SerializedName("items")
    val items: List<DrawItem>
)