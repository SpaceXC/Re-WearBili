package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Remote(
    @SerializedName("bfs_style")
    val bfsStyle: String,
    @SerializedName("url")
    val url: String
)