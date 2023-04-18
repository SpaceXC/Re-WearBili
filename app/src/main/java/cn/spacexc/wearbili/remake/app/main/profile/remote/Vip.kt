package cn.spacexc.wearbili.remake.app.main.profile.remote

data class Vip(
    val avatar_subscript: Int,
    val avatar_subscript_url: String,
    val due_date: Long,
    val label: Label,
    val nickname_color: String?,
    val role: Int,
    val status: Int,
    val theme_type: Int,
    val tv_vip_pay_type: Int,
    val tv_vip_status: Int,
    val type: Int,
    val vip_pay_type: Int
)