package cn.spacexc.wearbili.remake.common.domain.manager.remote

data class UserExitResult(
    val code: Int,
    val `data`: Data,
    val status: Boolean,
    val ts: Int
)