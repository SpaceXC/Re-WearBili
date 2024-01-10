package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Additional(
    @SerializedName("reserve")
    val reserve: Reserve,
    @SerializedName("type")
    val type: String
)