package cn.spacexc.wearbili.remake.app.search.domain.remote.result


import com.google.gson.annotations.SerializedName

data class Search(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("ttl")
    val ttl: Int
)