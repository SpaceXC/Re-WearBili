package cn.spacexc.wearbili.remake.app.main.profile.remote

data class CurrentUserInfo(
    val code: Int,
    val `data`: Data,
    val message: String,
    val ttl: Int
)