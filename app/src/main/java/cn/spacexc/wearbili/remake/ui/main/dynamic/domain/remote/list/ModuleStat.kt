package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class ModuleStat(
    @SerializedName("comment")
    val comment: Comment?,
    @SerializedName("forward")
    val forward: Forward?,
    @SerializedName("like")
    val like: Like?
)