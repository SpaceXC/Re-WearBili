package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class ModuleInteraction(
    @SerializedName("items")
    val items: List<InteractionItem>
)