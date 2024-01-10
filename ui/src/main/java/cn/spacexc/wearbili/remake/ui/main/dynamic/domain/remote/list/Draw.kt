package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Draw(
    @SerializedName("id")
    val id: Long,
    @SerializedName("items")
    val items: List<DrawItem>
)