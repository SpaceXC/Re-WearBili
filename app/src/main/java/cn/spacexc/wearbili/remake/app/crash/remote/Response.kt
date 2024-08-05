package cn.spacexc.wearbili.remake.app.crash.remote

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    val code: Int,
    val message: String,
    val body: T?
)
