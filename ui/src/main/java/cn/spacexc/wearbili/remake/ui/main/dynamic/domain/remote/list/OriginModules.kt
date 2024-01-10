package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class OriginModules(
    @SerializedName("module_author")
    val moduleAuthor: OriginModuleAuthor,
    @SerializedName("module_dynamic")
    val moduleDynamic: ModuleDynamic
)