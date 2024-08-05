package cn.spacexc.wearbili.remake.common

interface HeartbeatListenable<T> {
    fun onHeartbeat(): T
}