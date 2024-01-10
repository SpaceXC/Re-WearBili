package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Resource(
    @SerializedName("res_image")
    val resImage: ResImage,
    @SerializedName("res_type")
    val resType: Int
)