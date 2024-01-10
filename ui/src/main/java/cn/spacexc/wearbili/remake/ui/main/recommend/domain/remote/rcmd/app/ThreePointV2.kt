package cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.app

data class ThreePointV2(
    val icon: String?,
    val reasons: List<cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.app.Reason>?,
    val subtitle: String?,
    val title: String?,
    val type: String
)