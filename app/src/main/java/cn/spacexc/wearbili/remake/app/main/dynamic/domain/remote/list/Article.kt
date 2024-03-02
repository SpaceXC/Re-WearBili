package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Article(
    val covers: List<String>,
    val desc: String,
    val id: Long,
    @SerializedName("jump_url") val jumpUrl: String,
    val label: String,
    val title: String
)