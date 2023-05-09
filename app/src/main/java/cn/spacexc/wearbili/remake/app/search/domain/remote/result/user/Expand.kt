package cn.spacexc.wearbili.remake.app.search.domain.remote.result.user


import com.google.gson.annotations.SerializedName

data class Expand(
    @SerializedName("is_power_up")
    val isPowerUp: Boolean,
    @SerializedName("system_notice")
    val systemNotice: Any?
)