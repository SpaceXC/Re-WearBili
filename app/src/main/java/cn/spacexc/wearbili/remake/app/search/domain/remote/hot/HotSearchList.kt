package cn.spacexc.wearbili.remake.app.search.domain.remote.hot

data class HotSearchList(
    val code: Int,
    val cost: Cost,
    val exp_str: String,
    val hotword_egg_info: String,
    val list: List<HotSearch>,
    val message: String,
    val seid: String,
    val timestamp: Int
)