package cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.app

import cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.app.DislikeReason
import cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.app.Feedback

data class ThreePoint(
    val dislike_reasons: List<cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.app.DislikeReason>,
    val feedbacks: List<cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.app.Feedback>?,
    val watch_later: Int?
)