package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class ModuleInteraction(
    @SerializedName("items")
    val items: List<InteractionItem>
)