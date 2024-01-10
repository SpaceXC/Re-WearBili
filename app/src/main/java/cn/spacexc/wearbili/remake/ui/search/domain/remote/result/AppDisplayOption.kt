package cn.spacexc.wearbili.remake.ui.search.domain.remote.result


import com.google.gson.annotations.SerializedName

data class AppDisplayOption(
    @SerializedName("is_search_page_grayed")
    val isSearchPageGrayed: Int
)