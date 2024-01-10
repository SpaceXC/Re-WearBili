package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class DynamicItem(
    @SerializedName("basic")
    val basic: Basic,
    @SerializedName("id_str")
    val idStr: String,
    @SerializedName("modules")
    val modules: Modules,
    @SerializedName("orig")
    val orig: DynamicItem?,
    @SerializedName("type")
    val type: String,
    @SerializedName("visible")
    val visible: Boolean
)