package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Topic(
    @SerializedName("id")
    val id: Long,
    @SerializedName("jump_url")
    val jumpUrl: String,
    @SerializedName("name")
    val name: String
)