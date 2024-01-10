package cn.spacexc.wearbili.remake.ui.search.domain.remote.result


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("app_display_option")
    val appDisplayOption: AppDisplayOption,
    @SerializedName("in_black_key")
    val inBlackKey: Int,
    @SerializedName("in_white_key")
    val inWhiteKey: Int,
    @SerializedName("is_search_page_grayed")
    val isSearchPageGrayed: Int,
    @SerializedName("numPages")
    val numPages: Int,
    @SerializedName("numResults")
    val numResults: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("pagesize")
    val pagesize: Int,
    @SerializedName("result")
    val result: List<Result>,
    @SerializedName("rqt_type")
    val rqtType: String,
    @SerializedName("seid")
    val seid: String,
    @SerializedName("show_module_list")
    val showModuleList: List<String>,
    @SerializedName("suggest_keyword")
    val suggestKeyword: String
)