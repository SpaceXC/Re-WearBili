package cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.app

import cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.app.Data

data class RecommendVideo(
    val code: Int,
    val `data`: cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.app.Data,
    val message: String,
    val ttl: Int
)