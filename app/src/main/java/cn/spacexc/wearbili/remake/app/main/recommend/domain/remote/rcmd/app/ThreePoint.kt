package cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.app

data class ThreePoint(
    val dislike_reasons: List<DislikeReason>,
    val feedbacks: List<Feedback>?,
    val watch_later: Int?
)