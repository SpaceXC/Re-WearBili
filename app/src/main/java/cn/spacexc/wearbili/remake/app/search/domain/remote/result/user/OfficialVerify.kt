package cn.spacexc.wearbili.remake.app.search.domain.remote.result.user


import com.google.gson.annotations.SerializedName

data class OfficialVerify(
    @SerializedName("desc")
    val desc: String,
    @SerializedName("type")
    val type: Int
)