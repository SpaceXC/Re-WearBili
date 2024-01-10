package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Desc2(
    @SerializedName("style")
    val style: Int,
    @SerializedName("text")
    val text: String,
    @SerializedName("visible")
    val visible: Boolean
)