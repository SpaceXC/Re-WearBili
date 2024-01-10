package cn.spacexc.wearbili.remake.ui.main.recommend.domain.remote.rcmd.web


import com.google.gson.annotations.SerializedName

data class Inline(
    @SerializedName("inline_barrage_switch")
    val inlineBarrageSwitch: Int,
    @SerializedName("inline_type")
    val inlineType: Int,
    @SerializedName("inline_url")
    val inlineUrl: String,
    @SerializedName("inline_use_same")
    val inlineUseSame: Int
)