package cn.spacexc.wearbili.remake.app.search.domain.remote.result


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("data")
    val `data`: List<Any>,
    @SerializedName("result_type")
    val resultType: String
)