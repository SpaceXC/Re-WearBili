package cn.spacexc.wearbili.common

data class WearBiliResponse<T>(
    val code: Int,
    val message: String,
    val body: T?
)
