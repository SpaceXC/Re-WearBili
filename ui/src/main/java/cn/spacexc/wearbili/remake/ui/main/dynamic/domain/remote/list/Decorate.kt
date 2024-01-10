package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Decorate(
    @SerializedName("card_url")
    val cardUrl: String,
    @SerializedName("fan")
    val fan: Fan,
    @SerializedName("id")
    val id: Long,
    @SerializedName("jump_url")
    val jumpUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: Int
)