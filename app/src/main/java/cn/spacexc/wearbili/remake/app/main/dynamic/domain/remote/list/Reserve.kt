package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Reserve(
    @SerializedName("button")
    val button: Button,
    @SerializedName("desc1")
    val desc1: Desc1,
    @SerializedName("desc2")
    val desc2: Desc2,
    @SerializedName("jump_url")
    val jumpUrl: String,
    @SerializedName("reserve_total")
    val reserveTotal: Int,
    @SerializedName("rid")
    val rid: Long,
    @SerializedName("state")
    val state: Int,
    @SerializedName("stype")
    val stype: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("up_mid")
    val upMid: Long
)