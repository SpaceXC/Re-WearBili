package cn.spacexc.wearbili.remake.app.search.domain.remote.hot


data class TrendingWordList(
    val code: Int,
    val `data`: Data,
    val message: String,
    val ttl: Int
)