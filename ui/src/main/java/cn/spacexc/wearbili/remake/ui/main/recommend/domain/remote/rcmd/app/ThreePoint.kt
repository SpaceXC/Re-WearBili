package cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.app

data class ThreePoint(
    val dislike_reasons: List<cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.app.DislikeReason>,
    val feedbacks: List<cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.app.Feedback>?,
    val watch_later: Int?
)