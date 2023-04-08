package cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.app

import cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.app.Reason

data class ThreePointV2(
    val icon: String?,
    val reasons: List<cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.app.Reason>?,
    val subtitle: String?,
    val title: String?,
    val type: String
)