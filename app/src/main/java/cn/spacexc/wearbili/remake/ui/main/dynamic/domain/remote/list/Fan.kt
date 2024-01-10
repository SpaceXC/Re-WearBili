package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Fan(
    @SerializedName("color")
    val color: String,
    @SerializedName("is_fan")
    val isFan: Boolean,
    @SerializedName("num_str")
    val numStr: String,
    @SerializedName("number")
    val number: Int
)