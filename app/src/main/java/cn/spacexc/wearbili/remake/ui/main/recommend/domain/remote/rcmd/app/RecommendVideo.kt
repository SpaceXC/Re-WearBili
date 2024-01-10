package cn.spacexc.wearbili.remake.ui.main.recommend.domain.remote.rcmd.app

data class RecommendVideo(
    val code: Int,
    val `data`: cn.spacexc.wearbili.remake.ui.main.recommend.domain.remote.rcmd.app.Data,
    val message: String,
    val ttl: Int
)